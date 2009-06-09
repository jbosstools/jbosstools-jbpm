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
package org.jbpm.gd.common.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NodeContainer;
import org.jbpm.gd.jpdl.Logger;


public abstract class AbstractNodeDeleteCommand extends Command {

	protected Node node;
	protected NodeContainer parent;
	private ArrayList edgeDeleteCommands;
	private ArrayList nodeDeleteCommands;

	public void setNode(Node node) {
		this.node = node;
		this.parent = node.getContainer();
	}
	
	public void execute() {		
		if (edgeDeleteCommands == null) {
			constructEdgeDeleteCommands();
		}
		if (nodeDeleteCommands == null) {
			constructNodeDeleteCommands();
		}
		executeCommands(edgeDeleteCommands);
		executeCommands(nodeDeleteCommands);
		doRemove();
	}
	
	public void undo() {
		doAdd();
		undoCommands(nodeDeleteCommands);
		undoCommands(edgeDeleteCommands);
	}
	
	private void constructNodeDeleteCommands() {
		nodeDeleteCommands = new ArrayList();
		if (node instanceof NodeContainer) {
			List nodes = ((NodeContainer)node).getNodes();
			for (int i = 0; i < nodes.size(); i++) {
				try {
					AbstractNodeDeleteCommand command = (AbstractNodeDeleteCommand)this.getClass().newInstance();
					command.setNode((Node)nodes.get(i));
					nodeDeleteCommands.add(command);
				} catch (IllegalAccessException e) {
					Logger.logError("problem while creating NodeDeleteCommand", e);
				} catch (InstantiationException e) {
					Logger.logError("problem while creating NodeDeleteCommand", e);
				}
			}
		}
	}
	
	private void addEdgeDeleteCommand(Edge edge) {
		AbstractEdgeDeleteCommand command = createEdgeDeleteCommand();
		command.setEdge(edge);
		edgeDeleteCommands.add(command);
	}
	
	private void addEdgeDeleteCommands(List list) {
		for (int i = 0; i < list.size(); i++) {
			addEdgeDeleteCommand((Edge)list.get(i));
		}
	}
	
	private void constructEdgeDeleteCommands() {
		edgeDeleteCommands = new ArrayList();
		addEdgeDeleteCommands(node.getArrivingEdges());
		addEdgeDeleteCommands(node.getLeavingEdges());
	}
	
	private void executeCommands(List commands) {
		for (int i = 0; i < commands.size(); i++) {
			((Command)commands.get(i)).execute();
		}
	}
	
	private void undoCommands(List commands) {
		for (int i = commands.size(); i > 0; i--) {
			((Command)commands.get(i - 1)).undo();
		}
	}
	
	protected abstract AbstractEdgeDeleteCommand createEdgeDeleteCommand();
	protected abstract void doAdd();
	protected abstract void doRemove();
	
}
