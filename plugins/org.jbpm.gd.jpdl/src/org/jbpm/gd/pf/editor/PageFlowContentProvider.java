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
package org.jbpm.gd.pf.editor;

import org.dom4j.Element;
import org.jbpm.gd.common.editor.AbstractContentProvider;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NodeContainer;
import org.jbpm.gd.pf.model.NodeElement;
import org.jbpm.gd.pf.model.PageFlowDefinition;
import org.jbpm.gd.pf.model.Transition;

public class PageFlowContentProvider extends AbstractContentProvider{
	
	protected void addNodes(NodeContainer nodeContainer, Element notationInfo) {
		PageFlowDefinition pageFlowDefinition = (PageFlowDefinition)nodeContainer.getSemanticElement();
		addNodes(nodeContainer, pageFlowDefinition.getNodeElements(), notationInfo);
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
		PageFlowDefinition pageFlowDefinition = (PageFlowDefinition)nodeContainer.getSemanticElement();
		return pageFlowDefinition.getNodeElementByName(notationInfo.attributeValue("name"));
	}
	
	
	protected SemanticElement findDestination(Edge edge, Node source) {
		String nodeName = ((Transition)edge.getSemanticElement()).getTo();		
		PageFlowDefinition pageFlowDefinition = (PageFlowDefinition)source.getContainer().getSemanticElement();
		return pageFlowDefinition.getNodeElementByName(nodeName);
	}

}
