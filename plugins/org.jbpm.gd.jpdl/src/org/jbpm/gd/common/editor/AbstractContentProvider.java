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
package org.jbpm.gd.common.editor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.jbpm.gd.common.model.NamedElement;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.notation.AbstractNodeContainer;
import org.jbpm.gd.common.notation.BendPoint;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NodeContainer;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.notation.NotationMapping;
import org.jbpm.gd.common.notation.RootContainer;
import org.jbpm.gd.jpdl.Logger;

public abstract class AbstractContentProvider implements ContentProvider{
	
	protected abstract SemanticElement getEdgeSemanticElement(Node node, Element notationInfo, int index);
	protected abstract SemanticElement getNodeSemanticElement(NodeContainer node, Element notationInfo, int index);
	protected abstract void addNodes(NodeContainer nodeContainer, Element notationInfo);
	protected abstract void addEdges(Node node, Element notationInfo);
	protected abstract SemanticElement findDestination(Edge edge, Node source);
	
	protected String getRootNotationInfoElement() {
		return "<root-container/>";
	}
	
	protected String createInitialNotationInfo() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("\n\n");
		buffer.append(getRootNotationInfoElement());
		return buffer.toString();
	}

	public String getNotationInfoFileName(String semanticInfoFileName) {
		return ".gpd." + semanticInfoFileName;
	}
	
	public String getDiagramImageFileName(String semanticInfoFileName) {
		String result;
		int index = semanticInfoFileName.indexOf(".xml");
		result = index > -1 ? semanticInfoFileName.substring(0, index) : semanticInfoFileName;
		return result + ".jpg";
	}

//	public String getSemanticInfoFileName(String notationInfoFileName) {
//		return notationInfoFileName.substring(5);
//	}

	protected void processRootContainer(RootContainer rootContainer, Element notationInfo) {
		addDimension(rootContainer, notationInfo);
		addNodes(rootContainer, notationInfo);
		postProcess(rootContainer);
	}
	
	
	protected void addNodes(NodeContainer nodeContainer, SemanticElement[] semanticElements, Element notationInfo) {
		List notationInfoElements = notationInfo == null ? new ArrayList() : notationInfo.elements();
		for (int i = 0; i < semanticElements.length; i++) {
			Element notationInfoElement = null;
			String nodeName = ((NamedElement)semanticElements[i]).getName();
			for (int j = 0; j < notationInfoElements.size(); j++) {
				Element element = (Element)notationInfoElements.get(j);
				String elementName = element.attributeValue("name");
				if ((elementName != null && elementName.equals(nodeName)) || (elementName == null && nodeName == null)) {
					notationInfoElement = element;
				}
			}
//			if (notationInfoElements.size() >= i + 1) {
//				notationInfoElement = (Element)notationInfoElements.get(i);
//			}
			addNode(nodeContainer, semanticElements[i], notationInfoElement);
		}
	}
	
	protected void addEdges(Node node, SemanticElement[] semanticElements, Element notationInfo) {
		List notationInfoElements = notationInfo == null ? new ArrayList() : notationInfo.elements();
		for (int i = 0; i < semanticElements.length; i++) {
			Element notationInfoElement = null;
			if (notationInfoElements.size() >= i + 1) {
				notationInfoElement = (Element)notationInfoElements.get(i);
			}
			addEdge(node, semanticElements[i], notationInfoElement);
		}
	}
	
	protected void addNode(NodeContainer nodeContainer, SemanticElement semanticElement, Element notationInfoElement) {
		String notationElementId = NotationMapping.getNotationElementId(semanticElement.getElementId());
		Node notationElement = (Node)nodeContainer.getFactory().create(notationElementId);
		notationElement.setSemanticElement(semanticElement);
		notationElement.register();
		nodeContainer.addNode(notationElement);
		semanticElement.addPropertyChangeListener(notationElement);
		processNode(notationElement, notationInfoElement);
		if (notationElement instanceof NodeContainer) {
			addNodes((NodeContainer)notationElement, notationInfoElement);
		}
	}
	
	protected void addEdge(Node node, SemanticElement semanticElement, Element notationInfoElement) {
		NotationElement notationElement = node.getRegisteredNotationElementFor(semanticElement);
		if (notationElement == null) {
			String notationElementId = NotationMapping.getNotationElementId(semanticElement.getElementId());
			notationElement = (NotationElement)node.getFactory().create(notationElementId);
			notationElement.setSemanticElement(semanticElement);
			notationElement.register();
			node.addLeavingEdge((Edge)notationElement);
			semanticElement.addPropertyChangeListener(notationElement);
		}
		processEdge((Edge)notationElement, notationInfoElement);
	}
	
	protected void addDimension(
			RootContainer processDefinitionNotationElement, 
			Element processDiagramInfo) {
		String width = processDiagramInfo.attributeValue("width");
		String height = processDiagramInfo.attributeValue("height");
		Dimension dimension = new Dimension(
			width == null ? 0 : Integer.valueOf(width).intValue(),
			height == null ? 0 : Integer.valueOf(height).intValue());
		processDefinitionNotationElement.setDimension(dimension);
	}

	protected void processNode(Node node, Element notationInfoElement) {
		addConstraint(node, notationInfoElement);		
		addEdges(node, notationInfoElement);
	}
	
	protected void processEdge(Edge edge, Element edgeInfo) {
		processLabel(edge, edgeInfo);
		addBendpoints(edge, edgeInfo);		
	}
	
	protected void addBendpoints(Edge edge, Element edgeInfo) {
		if (edgeInfo != null) {
			List list = edgeInfo.elements("bendpoint");
			for (int i = 0; i < list.size(); i++) {
				addBendpoint(edge, (Element)list.get(i), i);
			}
		}
	}
	
	protected BendPoint addBendpoint(Edge edge, Element bendpointInfo, int index) {
		BendPoint result = new BendPoint();
		processBendpoint(result, bendpointInfo);
		edge.addBendPoint(result);
		return result;
	}
	
	protected void processBendpoint(BendPoint bendPoint, Element bendpointInfo) {
		int w1 = Integer.valueOf(bendpointInfo.attributeValue("w1")).intValue();
		int h1 = Integer.valueOf(bendpointInfo.attributeValue("h1")).intValue();
		int w2 = Integer.valueOf(bendpointInfo.attributeValue("w2")).intValue();
		int h2 = Integer.valueOf(bendpointInfo.attributeValue("h2")).intValue();
		Dimension d1 = new Dimension(w1, h1);
		Dimension d2 = new Dimension(w2, h2);
		bendPoint.setRelativeDimensions(d1, d2);		
	}
	
	private void processLabel(Edge edge, Element edgeInfo) {
		Element label = null;
		if (edgeInfo != null) {
			label = edgeInfo.element("label");
		}
		if (label != null) {
			Point offset = new Point();
			offset.x = Integer.valueOf(label.attributeValue("x")).intValue();
			offset.y = Integer.valueOf(label.attributeValue("y")).intValue();
			edge.getLabel().setOffset(offset);
		}
	}
	
	private void addConstraint(Node node, Element nodeInfo) {
		Rectangle constraint = node.getConstraint().getCopy();
		Dimension initialDimension = NotationMapping.getInitialDimension(node.getSemanticElement().getElementId());
		if (initialDimension != null) {
			constraint.setSize(initialDimension);
		}
		if (nodeInfo != null) {
			constraint.x = Integer.valueOf(nodeInfo.attributeValue("x")).intValue();
			constraint.y = Integer.valueOf(nodeInfo.attributeValue("y")).intValue();
			constraint.width = Integer.valueOf(nodeInfo.attributeValue("width")).intValue();
			constraint.height = Integer.valueOf(nodeInfo.attributeValue("height")).intValue();
		}
		node.setConstraint(constraint);
	}
	
	protected void postProcess(NodeContainer nodeContainer) {
		List nodes = nodeContainer.getNodes();
		for (int i = 0; i < nodes.size(); i++) {
			Node node = (Node)nodes.get(i);
			List edges = node.getLeavingEdges();
			for (int j = 0; j < edges.size(); j++) {
				Edge edge = (Edge)edges.get(j);
				SemanticElement destination = findDestination(edge, node);
				Node target = (Node)edge.getFactory().getRegisteredNotationElementFor(destination);
				if (target != null && edge.getTarget() == null) {
					target.addArrivingEdge(edge);
				}
			}
			if (node instanceof NodeContainer) {
				postProcess((NodeContainer)node);
			}
		}
	}
	
	public boolean saveToInput(
			IEditorInput input,
			RootContainer rootContainer) {
		boolean result = true;
		try {
			IFile file = getNotationInfoFile(((IFileEditorInput)input).getFile());
			InputStreamReader reader = new InputStreamReader(file.getContents());
			Element notationInfo = new SAXReader().read(reader).getRootElement();
			if (upToDateCheck(notationInfo)) {
				getNotationInfoFile(((IFileEditorInput)input).getFile()).setContents(
					new ByteArrayInputStream(toNotationInfoXml(rootContainer).getBytes()), true, true, null);
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	 }	
	
	private String toNotationInfoXml(RootContainer rootContainer) {
		StringWriter writer = new StringWriter();
		write(rootContainer, writer);
		return writer.toString();
	}

	private void write(
			RootContainer rootContainer, Writer writer) {
		try {
			Document document = DocumentHelper.createDocument();
			Element root = document.addElement("root-container");
			write(rootContainer, root);
			XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
			xmlWriter.write(document);
		} catch (IOException e) {
			e.printStackTrace(new PrintWriter(writer));
		}
	}
	
	private void write(
			RootContainer rootContainer,
			Element element) {
		addAttribute(element, "name", ((NamedElement)rootContainer.getSemanticElement()).getName());
		addAttribute(element, "width", Integer.toString(rootContainer.getDimension().width));
		addAttribute(element, "height", Integer.toString(rootContainer.getDimension().height));
		Iterator iter = rootContainer.getNodes().iterator();
		while (iter.hasNext()) {
			write((Node) iter.next(), element);
		}
	}
	
	private void write(Node node, Element element) {
		Element newElement = null;
		if (node instanceof AbstractNodeContainer) {
			newElement = addElement(element, "node-container");
		} else {
			newElement = addElement(element, "node");
		}
		addAttribute(newElement, "name", ((NamedElement)node.getSemanticElement()).getName());
		addAttribute(newElement, "x", String.valueOf(node.getConstraint().x));
		addAttribute(newElement, "y", String.valueOf(node.getConstraint().y));
		addAttribute(newElement, "width", String.valueOf(node.getConstraint().width));
		addAttribute(newElement, "height", String.valueOf(node.getConstraint().height));
		if (node instanceof AbstractNodeContainer) {
			Iterator nodes = ((AbstractNodeContainer)node).getNodes().iterator();
			while (nodes.hasNext()) {
				write((Node)nodes.next(), newElement); 
			}
		}
		Iterator edges = node.getLeavingEdges().iterator();
		while (edges.hasNext()) {
			Edge edge = (Edge) edges.next();
			write(edge, addElement(newElement, "edge"));
		}
	}

	private void write(Edge edge,
			Element element) {
		Point offset = edge.getLabel().getOffset();
		if (offset != null) {
			Element label = addElement(element, "label");
			addAttribute(label, "x", String.valueOf(offset.x));
			addAttribute(label, "y", String.valueOf(offset.y));
		}
		Iterator bendpoints = edge.getBendPoints().iterator();
		while (bendpoints.hasNext()) {
			write((BendPoint) bendpoints.next(), addElement(element, "bendpoint"));
		}
	}

	private void write(BendPoint bendpoint, Element bendpointElement) {
		addAttribute(bendpointElement, "w1", String.valueOf(bendpoint
				.getFirstRelativeDimension().width));
		addAttribute(bendpointElement, "h1", String.valueOf(bendpoint
				.getFirstRelativeDimension().height));
		addAttribute(bendpointElement, "w2", String.valueOf(bendpoint
				.getSecondRelativeDimension().width));
		addAttribute(bendpointElement, "h2", String.valueOf(bendpoint
				.getSecondRelativeDimension().height));
	}
	
	private Element addElement(Element element, String elementName) {
		Element newElement = element.addElement(elementName);
		return newElement;
	}

	private void addAttribute(Element e, String attributeName,
			String value) {
		if (value != null) {
			e.addAttribute(attributeName, value);
		}
	}

	private void createNotationInfoFile(IFile notationInfoFile) {
		try {
			notationInfoFile.create(new ByteArrayInputStream(createInitialNotationInfo().toString().getBytes()), true, null);
		} catch (CoreException e) {
			Logger.logError(e);
		}
	}
	
	private IFile getNotationInfoFile(IFile semanticInfoFile) {
		IProject project = semanticInfoFile.getProject();
		IPath semanticInfoPath = semanticInfoFile.getProjectRelativePath();
		IPath notationInfoPath = semanticInfoPath.removeLastSegments(1).append(getNotationInfoFileName(semanticInfoFile.getName()));
		IFile notationInfoFile = project.getFile(notationInfoPath);
		if (!notationInfoFile.exists()) {
			createNotationInfoFile(notationInfoFile);
		}
		return notationInfoFile;
	}
	
	public void addNotationInfo(RootContainer rootContainer, IEditorInput input) { 
		try {
			IFile file = getNotationInfoFile(((FileEditorInput)input).getFile());
			if (file.exists()) {
				InputStreamReader reader = new InputStreamReader(file.getContents());
				Element notationInfo = new SAXReader().read(reader).getRootElement();
				boolean changed = convertCheck(notationInfo);
				processRootContainer(rootContainer, notationInfo);
				if (changed) {
					file.setContents(new ByteArrayInputStream(toNotationInfoXml(rootContainer).getBytes()), true, true, null);
				}
			} else {
				file.create(new ByteArrayInputStream(createInitialNotationInfo().toString().getBytes()), true, null);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private boolean convertCheck(Element notationInfo) {
		boolean changed = false;
		if ("process-diagram".equals(notationInfo.getName()) || "pageflow-diagram".equals(notationInfo.getName())) {
			MessageDialog dialog = new MessageDialog(
					null, 
					"Convert To 3.1.x Format", 
					null, 
					"A file created with an older GPD version was detected. " +
					"If you open this file it will be converted to the 3.1.x " +
					"format and overwritten.\n" +
					"Do you want to continue?",
					MessageDialog.QUESTION,
					new String[] {"Convert And Open", "Continue Without Converting"},
					0);
			if (dialog.open() == 0) {
				convertToRootContainer(notationInfo);
				changed = true;
			}
		}
		return changed;
	}
	
	private boolean upToDateCheck(Element notationInfo) {
		if ("process-diagram".equals(notationInfo.getName()) || "pageflow-diagram".equals(notationInfo.getName())) {
			MessageDialog dialog = new MessageDialog(
					null, 
					"GPD 3.0.x Format Detected", 
					null, 
					"The file you are trying to save contains GPD 3.0.x information." +
					"Saving the file will result in an automatic conversion into the 3.1.x format." +
					"It will be impossible to open it with the old GPD.\n" +
					"Do you want to continue?",
					MessageDialog.QUESTION,
					new String[] {"Save And Convert", "Cancel"},
					0);
			return dialog.open() == 0;
		}
		return true;
	}
	
	private void convertToRootContainer(Element notationInfo) {
		notationInfo.setName("root-container");
		convertChildrenToEdge(notationInfo);
	}

	private void convertChildrenToEdge(Element element) {
		List list = element.elements();
		for (int i = 0; i < list.size(); i++) {
			convertToEdge((Element)list.get(i));
		}
	}
	
	private void convertToEdge(Element element) {
		if ("transition".equals(element.getName())) {
			element.setName("edge");
		}
		convertChildrenToEdge(element);
	}

}
