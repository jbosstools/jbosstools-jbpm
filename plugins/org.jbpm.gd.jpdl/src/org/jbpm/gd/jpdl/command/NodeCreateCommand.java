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
package org.jbpm.gd.jpdl.command;

import org.jbpm.gd.common.command.AbstractNodeCreateCommand;
import org.jbpm.gd.jpdl.model.AbstractNode;
import org.jbpm.gd.jpdl.model.NodeElementContainer;
import org.jbpm.gd.jpdl.model.ProcessDefinition;
import org.jbpm.gd.jpdl.model.StartState;

public class NodeCreateCommand extends AbstractNodeCreateCommand {
		
	public void execute() {
		super.execute();
		setName();
		addAbstractNode(getNodeElementContainer(), getAbstractNode());
	}
	
	private void addAbstractNode(NodeElementContainer nodeElementContainer, AbstractNode abstractNode) {
		if (abstractNode instanceof StartState && nodeElementContainer instanceof ProcessDefinition) {
			((ProcessDefinition)nodeElementContainer).addStartState((StartState)abstractNode);
		} else {
			nodeElementContainer.addNodeElement(abstractNode);
		}
	}
	
	private void removeAbstractNode(NodeElementContainer nodeElementContainer, AbstractNode abstractNode) {
		if (abstractNode instanceof StartState && nodeElementContainer instanceof ProcessDefinition) {
			((ProcessDefinition)nodeElementContainer).removeStartState((StartState)abstractNode);
		} else {
			nodeElementContainer.removeNodeElement(abstractNode);
		}
	}
	
	private NodeElementContainer getNodeElementContainer() {
		return (NodeElementContainer)parent.getSemanticElement();
	}
	
	private AbstractNode getAbstractNode() {
		return (AbstractNode)node.getSemanticElement();
	}
	
	public boolean canExecute() {
		return getNodeElementContainer().canAdd(getAbstractNode());
	}
	
	private void setName() {
		if (getAbstractNode().getName() == null) {
			getAbstractNode().initializeName(getNodeElementContainer());
		}
	}
	
	public void undo() {
		removeAbstractNode(getNodeElementContainer(), getAbstractNode());
	}
	
}
