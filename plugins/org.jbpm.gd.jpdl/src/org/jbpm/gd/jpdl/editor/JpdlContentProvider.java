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

import java.util.StringTokenizer;

import org.dom4j.Element;
import org.jbpm.gd.common.editor.AbstractContentProvider;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NodeContainer;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.jpdl.model.NodeElement;
import org.jbpm.gd.jpdl.model.NodeElementContainer;
import org.jbpm.gd.jpdl.model.Transition;

public class JpdlContentProvider extends AbstractContentProvider{
	
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

//	public String getSemanticInfoFileName(String notationInfoFileName) {
//		if ("gpd.xml".equals(notationInfoFileName)) {
//			return "processdefinition.xml";
//		} else {
//			return super.getSemanticInfoFileName(notationInfoFileName);
//		}
//	}

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
	
}
