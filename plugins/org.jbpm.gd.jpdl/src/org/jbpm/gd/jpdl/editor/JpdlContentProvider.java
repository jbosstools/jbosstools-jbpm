/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jbpm.gd.jpdl.editor;

import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jbpm.gd.common.editor.AbstractContentProvider;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NodeContainer;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.notation.RootContainer;
import org.jbpm.gd.jpdl.Plugin;
import org.jbpm.gd.jpdl.deployment.DeploymentInfo;
import org.jbpm.gd.jpdl.model.NodeElement;
import org.jbpm.gd.jpdl.model.NodeElementContainer;
import org.jbpm.gd.jpdl.model.Transition;
import org.jbpm.gd.jpdl.prefs.PreferencesConstants;

public class JpdlContentProvider extends AbstractContentProvider implements PreferencesConstants {
	
	JpdlEditor jpdlEditor;
	
	public JpdlContentProvider(JpdlEditor jpdlEditor) {
		this.jpdlEditor = jpdlEditor;
	}
	
	public String getNotationInfoFileName(String semanticInfoFileName) {
		if ("processdefinition.xml".equals(semanticInfoFileName)) {
			return "gpd.xml";
		} else if (semanticInfoFileName.endsWith("jpdl.xml")) {
			int index = semanticInfoFileName.indexOf("jpdl.xml");
			return "." + semanticInfoFileName.substring(0, index) + "gpd.xml";
		} else {
			return super.getNotationInfoFileName(semanticInfoFileName);
		}
	}

	public String getDiagramImageFileName(String semanticInfoFileName) {
		if ("processdefinition.xml".equals(semanticInfoFileName)) {
			return "processimage.jpg";
		} else if (semanticInfoFileName.endsWith("jpdl.xml")) {
			int index = semanticInfoFileName.indexOf("jpdl.xml");
			return semanticInfoFileName.substring(0, index) + "jpg";
		} else {
			return super.getDiagramImageFileName(semanticInfoFileName);
		}
	}

	protected void addNodes(NodeContainer nodeContainer, Element notationInfo) {
		NodeElementContainer nodeElementContainer = (NodeElementContainer)nodeContainer.getSemanticElement();
		addNodes(nodeContainer, nodeElementContainer.getNodeElements(), notationInfo);
	}
	
	protected void addEdges(Node node, Element notationInfo) {
		NodeElement nodeElement = (NodeElement)node.getSemanticElement();
		addEdges(node, nodeElement.getTransitions(), notationInfo);
	}
	
	protected SemanticElement getEdgeSemanticElement(Node node, Element notationInfo, int index) {
		Transition[] transitions = ((NodeElement)node.getSemanticElement()).getTransitions();
		return index < transitions.length ? transitions[index] : null;
	}
	
	protected SemanticElement getNodeSemanticElement(NodeContainer nodeContainer, Element notationInfo, int index) {
		NodeElementContainer nodeElementContainer = (NodeElementContainer)nodeContainer.getSemanticElement();
		return nodeElementContainer.getNodeElementByName(notationInfo.attributeValue("name"));
	}
	
	protected SemanticElement findDestination(Edge edge, Node source) {
		NotationElement notationElement = source.getContainer();
		String pathCopy = ((Transition)edge.getSemanticElement()).getTo();
		while (pathCopy.length() > 3 && "../".equals(pathCopy.substring(0, 3)) && notationElement != null) {
			notationElement = ((Node)notationElement).getContainer();
			pathCopy = pathCopy.substring(3);
		}
		if (notationElement == null) return null;
		SemanticElement parent = (SemanticElement)notationElement.getSemanticElement();
		StringTokenizer tokenizer = new StringTokenizer(pathCopy, "/");
		while (parent != null && tokenizer.hasMoreTokens()) {
			if (!(parent instanceof NodeElementContainer)) return null;
			parent = ((NodeElementContainer)parent).getNodeElementByName(tokenizer.nextToken()); 
		}
		return (NodeElement)parent;
	}
	
	protected void write(
			RootContainer rootContainer,
			Element element) {
		super.write(rootContainer, element);
		writeDeploymentInfo(jpdlEditor.getDeploymentInfo(), element);
 	}
	
	private void writeDeploymentInfo(DeploymentInfo deploymentInfo, Element element) {
		if (deploymentInfo.isEmpty()) return;
		Element deploymentElement = addElement(element, "deployment");
		if (deploymentInfo.getGraphicalInfoFile() != null) {
			addAttribute(deploymentElement, "graphicalInfoFile", deploymentInfo.getGraphicalInfoFile().getFullPath().toString());
		}
		if (deploymentInfo.getImageFile() != null) {
			addAttribute(deploymentElement, "imageFile", deploymentInfo.getImageFile().getFullPath().toString());
		}
		if (deploymentInfo.getServerName() != null) {
			addAttribute(deploymentElement, "serverName", deploymentInfo.getServerName());
		}
		if (deploymentInfo.getServerPort() != null) {
			addAttribute(deploymentElement, "serverPort", deploymentInfo.getServerPort());
		}
		if (deploymentInfo.getServerDeployer() != null) {
			addAttribute(deploymentElement, "serverDeployer", deploymentInfo.getServerDeployer());
		}
		Object[] classesAndResources = deploymentInfo.getClassesAndResources();
		if (classesAndResources.length > 0) {
			Element classesAndResourcesElement = addElement(deploymentElement, "classesAndResources");
			for (int i = 0; i < classesAndResources.length; i++) {
				String value = null;
				String type = null;
				if (classesAndResources[i] instanceof ICompilationUnit) {
					value = ((ICompilationUnit)classesAndResources[i]).getPath().toString();
					type = "java";
				} else if (classesAndResources[i] instanceof IClassFile) {
					value = ((IClassFile)classesAndResources[i]).getHandleIdentifier();
					type = "class";
				} else if (classesAndResources[i] instanceof IFile) {
					value = ((IFile)classesAndResources[i]).getFullPath().toString();
					type = "file";
				}
				if (value != null) {
					Element el = addElement(classesAndResourcesElement, "element");
					addAttribute(el, "type", type);
					addAttribute(el, "value", value);
				}
			}
		}
		Object[] additionalFiles = deploymentInfo.getAdditionalFiles();
		if (additionalFiles.length > 0) {
			Element filesAndFoldersElement = addElement(deploymentElement, "additionalFiles");
			for (int i = 0; i < additionalFiles.length; i++) {
				if (additionalFiles[i] instanceof IFile) {
					IFile file = (IFile)additionalFiles[i];
					Element el = addElement(filesAndFoldersElement, "element");
					addAttribute(el, "value", file.getFullPath().toString());
				}
			}
		}
	}
	
	protected void initializeDeploymentInfo(DeploymentInfo deploymentInfo, IEditorInput editorInput) {
		try {
			IFile file = getNotationInfoFile(((IFileEditorInput)editorInput).getFile());
			// the file should exist as this is performed by the addNotationInfo previously
			InputStreamReader reader = new InputStreamReader(file.getContents());
			Element rootElement = new SAXReader().read(reader).getRootElement();
			processDeploymentInfo(deploymentInfo, rootElement, (IFileEditorInput)editorInput);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	protected void processDeploymentInfo(DeploymentInfo deploymentInfo, Element element, IFileEditorInput editorInput) {
		Element deploymentElement = element.element("deployment");
		processProcessInfoFile(deploymentInfo, deploymentElement, editorInput);
		processGraphicalInfoFile(deploymentInfo, deploymentElement, editorInput);
		processImageFile(deploymentInfo, deploymentElement, editorInput);
		processClassesAndResources(deploymentInfo, deploymentElement, editorInput);
		processAdditionalFiles(deploymentInfo, deploymentElement, editorInput);
		processServerInfo(deploymentInfo, deploymentElement, editorInput);
	}
	
	protected void processServerInfo(DeploymentInfo deploymentInfo, Element deploymentElement, IFileEditorInput editorInput) {
		if (deploymentElement == null) return;
		IPreferenceStore prefs = Plugin.getDefault().getPreferenceStore();
		processServerName(deploymentInfo, deploymentElement.attribute("serverName"), prefs);
		processServerPort(deploymentInfo, deploymentElement.attribute("serverPort"), prefs);
		processServerDeployer(deploymentInfo, deploymentElement.attribute("serverDeployer"), prefs);
		
	}
	
	private void processServerName(DeploymentInfo deploymentInfo, Attribute attribute, IPreferenceStore prefs) {
		if (attribute == null) {
			deploymentInfo.setServerName(prefs.getString(SERVER_NAME));
		} else {
			deploymentInfo.setServerName(attribute.getValue());
		}
	}
	
	private void processServerPort(DeploymentInfo deploymentInfo, Attribute attribute, IPreferenceStore prefs) {
		if (attribute == null) {
			deploymentInfo.setServerPort(prefs.getString(SERVER_PORT));
		} else {
			deploymentInfo.setServerPort(attribute.getValue());
		}
	}
	
	private void processServerDeployer(DeploymentInfo deploymentInfo, Attribute attribute, IPreferenceStore prefs) {
		if (attribute == null) {
			deploymentInfo.setServerDeployer(prefs.getString(SERVER_DEPLOYER));
		} else {
			deploymentInfo.setServerDeployer(attribute.getValue());
		}
	}
	
	protected void processProcessInfoFile(DeploymentInfo deploymentInfo, Element deploymentElement, IFileEditorInput editorInput) {
		deploymentInfo.setProcessInfoFile(editorInput.getFile());
	}
	
	protected void processGraphicalInfoFile(DeploymentInfo deploymentInfo, Element deploymentElement, IFileEditorInput editorInput) {
		if (deploymentElement == null) return;
		Attribute attribute = deploymentElement.attribute("graphicalInfoFile");
		if (attribute == null) {
			attribute = deploymentElement.attribute("gpdFile");
		}
		IFile graphicalInfoFile = null;
		if (attribute != null && attribute.getValue() != null) {
			IResource resource = editorInput.getFile().getWorkspace().getRoot().findMember(new Path(attribute.getValue()));
			if (resource instanceof IFile) {
				graphicalInfoFile = (IFile)resource;
			}
		}
		deploymentInfo.setGraphicalInfoFile(graphicalInfoFile);
	}
	
	protected void processImageFile(DeploymentInfo deploymentInfo, Element deploymentElement, IFileEditorInput editorInput) {
		if (deploymentElement == null) return;
		Attribute attribute = deploymentElement.attribute("imageFile");
		IFile imageFile = null;
		if (attribute != null && attribute.getValue() != null) {
			IResource resource = editorInput.getFile().getWorkspace().getRoot().findMember(new Path(attribute.getValue()));
			if (resource instanceof IFile) {
				imageFile = (IFile)resource;
			}
		}
		deploymentInfo.setImageFile(imageFile);
	}
	
	@SuppressWarnings("unchecked")
	protected void processClassesAndResources(DeploymentInfo deploymentInfo, Element deploymentElement, IFileEditorInput editorInput) {
		if (deploymentElement == null) return;
		Element classesAndResourcesElement = deploymentElement.element("classesAndResources");
		if (classesAndResourcesElement == null) return;
		List elements = classesAndResourcesElement.elements("element");
		for (int i = 0; i < elements.size(); i++) {
			Element element = (Element)elements.get(i);
			String type = element.attributeValue("type");
			String value = element.attributeValue("value");
			if ("java".equals(type)) {
				IResource resource = editorInput.getFile().getWorkspace().getRoot().findMember(new Path(value));
				if (resource instanceof IFile) {
					IJavaElement javaElement = JavaCore.create((IFile)resource);
					if (javaElement instanceof ICompilationUnit) {
						deploymentInfo.addToClassesAndResources(javaElement);
					}
				}
			} else if ("class".equals(type)) {
				IJavaElement javaElement = JavaCore.create(value);
				if (javaElement instanceof IClassFile) {
					deploymentInfo.addToClassesAndResources(javaElement);
				}
			} else if ("file".equals(type)) {
				IResource resource = editorInput.getFile().getWorkspace().getRoot().findMember(new Path(value));
				if (resource instanceof IFile) {
					deploymentInfo.addToClassesAndResources(resource);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void processAdditionalFiles(DeploymentInfo deploymentInfo, Element deploymentElement, IFileEditorInput editorInput) {
		if (deploymentElement == null) return;
		Element additionalFilesElement = deploymentElement.element("additionalFiles");
		if (additionalFilesElement == null) {
			additionalFilesElement = deploymentElement.element("filesAndFolders");
		}
		if (additionalFilesElement == null) return;
		List elements = additionalFilesElement.elements("element");
		for (int i = 0; i < elements.size(); i++) {
			String value = ((Element)elements.get(i)).attributeValue("value");
			IResource resource = editorInput.getFile().getWorkspace().getRoot().findMember(new Path(value));
			if (resource instanceof IFile) {
				deploymentInfo.addToAdditionalFiles(resource);
			}
		}
	}
	
	
	
}
