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
package org.jbpm.gd.pf.command;

import org.jbpm.gd.common.command.AbstractNodeCreateCommand;
import org.jbpm.gd.pf.model.NodeElement;
import org.jbpm.gd.pf.model.PageFlowDefinition;
import org.jbpm.gd.pf.model.StartPage;

public class NodeCreateCommand extends AbstractNodeCreateCommand {
		
	public void execute() {
		super.execute();
		setName();
		addNode(getPageFlowDefinition(), getNodeElement());
	}
	
	private void addNode(PageFlowDefinition pageFlowDefinition, NodeElement nodeElement) {
		if (nodeElement instanceof StartPage) {
			pageFlowDefinition.addStartPage((StartPage)nodeElement);
		} else {
			pageFlowDefinition.addNodeElement(nodeElement);
		}
	}
	
	private void removeNodeElement(PageFlowDefinition pageFlowDefinition, NodeElement nodeElement) {
		if (nodeElement instanceof StartPage) {
			pageFlowDefinition.removeStartPage((StartPage)nodeElement);
		} else {
			pageFlowDefinition.removeNodeElement(nodeElement);
		}
	}
	
	private PageFlowDefinition getPageFlowDefinition() {
		return (PageFlowDefinition)parent.getSemanticElement();
	}
	
	private NodeElement getNodeElement() {
		return (NodeElement)node.getSemanticElement();
	}
	
	private void setName() {
		if (getNodeElement().getName() == null) {
			getNodeElement().initializeName(getPageFlowDefinition());
		}
	}
	
	public void undo() {
		removeNodeElement(getPageFlowDefinition(), getNodeElement());
	}
	
}
