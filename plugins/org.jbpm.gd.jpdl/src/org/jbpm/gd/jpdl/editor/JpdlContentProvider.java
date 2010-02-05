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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jbpm.gd.common.editor.AbstractContentProvider;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NodeContainer;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.notation.RootContainer;
import org.jbpm.gd.jpdl.deployment.DeploymentInfo;
import org.jbpm.gd.jpdl.model.NodeElement;
import org.jbpm.gd.jpdl.model.NodeElementContainer;
import org.jbpm.gd.jpdl.model.Transition;

public class JpdlContentProvider extends AbstractContentProvider{
	
	JpdlEditor jpdlEditor;
	
	public JpdlContentProvider(JpdlEditor jpdlEditor) {
		this.jpdlEditor = jpdlEditor;
	}
	
	public String getNotationInfoFileName(String semanticInfoFileName) {
		if ("processdefinition.xml".equals(semanticInfoFileName)) {
			return "gpd.xml";
		} else {
			return super.getNotationInfoFileName(semanticInfoFileName);
		}
	}

	public String getDiagramImageFileName(String semanticInfoFileName) {
		if ("processdefinition.xml".equals(semanticInfoFileName)) {
			return "processimage.jpg";
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
		Element deploymentElement = addElement(element, "deployment");
		addAttribute(deploymentElement, "serverName", deploymentInfo.getServerName());
		addAttribute(deploymentElement, "serverPort", deploymentInfo.getServerPort());
		addAttribute(deploymentElement, "serverDeployer", deploymentInfo.getServerDeployer());
		Element classesAndResourcesElement = addElement(deploymentElement, "classesAndResources");
		Object[] classesAndResources = deploymentInfo.getClassesAndResources();
		for (int i = 0; i < classesAndResources.length; i++) {
			Object object = classesAndResources[i];
			String value = null;
			if (object instanceof ICompilationUnit) {
				value = ((ICompilationUnit)object).getResource().getFullPath().toString();
			} else if (object instanceof IResource) {
				value = ((IResource)object).getFullPath().toString();
			}
			if (value != null) {
				Element el = addElement(classesAndResourcesElement, "element");
				addAttribute(el, "value", value);
			}
		}
		Element filesAndFoldersElement = addElement(deploymentElement, "filesAndFolders");
		Object[] filesAndFolders = deploymentInfo.getFilesAndFolders();
		for (int i = 0; i < filesAndFolders.length; i++) {
			Object object = filesAndFolders[i];
			if (object instanceof IFile) {
				Element el = addElement(filesAndFoldersElement, "element");
				addAttribute(el, "value", ((IFile)object).getFullPath().toString());
			}
		}
	}
	
	protected void addDeploymentInfo(DeploymentInfo deploymentInfo, IEditorInput editorInput) {
		try {
			IFile file = getNotationInfoFile(((FileEditorInput)editorInput).getFile());
			// the file should exist as this is performed by the addNotationInfo previously
			InputStreamReader reader = new InputStreamReader(file.getContents());
			Element rootElement = new SAXReader().read(reader).getRootElement();
			processDeploymentInfo(deploymentInfo, rootElement);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	protected void processDeploymentInfo(DeploymentInfo deploymentInfo, Element element) {
		Element deploymentElement = element.element("deployment");
		if (deploymentElement == null) return;
		processServerName(deploymentInfo, deploymentElement.attribute("serverName"));
		processServerPort(deploymentInfo, deploymentElement.attribute("serverPort"));
		processServerDeployer(deploymentInfo, deploymentElement.attribute("serverDeployer"));
		processClassesAndResources(deploymentInfo, deploymentElement.element("classesAndResources"));
		processFilesAndFolders(deploymentInfo, deploymentElement.element("filesAndFolders"));
	}
	
	protected void processServerName(DeploymentInfo deploymentInfo, Attribute attribute) {
		if (attribute == null) return;
		String value = attribute.getValue();
		if (value == null) return;
		deploymentInfo.setServerName(value);
	}
	
	protected void processServerPort(DeploymentInfo deploymentInfo, Attribute attribute) {
		if (attribute == null) return;
		String value = attribute.getValue();
		if (value == null) return;
		deploymentInfo.setServerPort(value);
	}
	
	protected void processServerDeployer(DeploymentInfo deploymentInfo, Attribute attribute) {
		if (attribute == null) return;
		String value = attribute.getValue();
		if (value == null) return;
		deploymentInfo.setServerDeployer(value);
	}
	
	protected void processClassesAndResources(DeploymentInfo deploymentInfo, Element element) {
		if (element == null) return;
		ArrayList classesAndResources = new ArrayList();
		List elements = element.elements("element");
		for (int i = 0; i < elements.size(); i++) {
			String value = ((Element)elements.get(i)).attributeValue("value");
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(value));
			if (!file.exists()) continue;
			IJavaElement javaElement = JavaCore.create(file);
			if (javaElement != null) {
				classesAndResources.add(javaElement);
			} else {
				classesAndResources.add(file);
			}
		}
		deploymentInfo.setClassesAndResources(classesAndResources.toArray());
	}
	
	protected void processFilesAndFolders(DeploymentInfo deploymentInfo, Element element) {
		if (element == null) return;
		ArrayList filesAndFolders = new ArrayList();
		List elements = element.elements("element");
		for (int i = 0; i < elements.size(); i++) {
			String value = ((Element)elements.get(i)).attributeValue("value");
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(value));
			if (!file.exists()) continue;
			filesAndFolders.add(file);
		}
		deploymentInfo.setFilesAndFolders(filesAndFolders.toArray());
	}
	
	
	
}
