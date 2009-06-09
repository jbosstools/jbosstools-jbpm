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
package org.jbpm.gd.common.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.jbpm.gd.common.command.AbstractEdgeCreateCommand;
import org.jbpm.gd.common.command.AbstractEdgeMoveCommand;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;

public abstract class GraphicalNodeEditPolicy extends org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy {
	
	protected abstract boolean canStart();
	protected abstract boolean canStop();
	protected abstract AbstractEdgeCreateCommand createEdgeCreateCommand();
	protected abstract AbstractEdgeMoveCommand createEdgeMoveCommand();

	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		if (canStop()) {
			AbstractEdgeCreateCommand command = (AbstractEdgeCreateCommand)request.getStartCommand();
			command.setTarget(getNode());
			return command;
		} else {
			return null;
		}
	}
	
	protected Node getNode() {
		return (Node)getHost().getModel();		
	}
	
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		if (canStart()) {
			AbstractEdgeCreateCommand command = createEdgeCreateCommand();
			command.setSource(getNode());
			command.setEdge((Edge)request.getNewObject());
			request.setStartCommand(command);
			return command;
		} else {
			return null;
		}
	}
	
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		if (request.getTarget() != null) {
			AbstractEdgeMoveCommand command = createEdgeMoveCommand();
			command.setEdge((Edge)request.getConnectionEditPart().getModel());
			command.setTarget((Node)request.getTarget().getModel());
			return command;
		}
		return null;
	}

	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		if (request.getTarget() != null) {
			AbstractEdgeMoveCommand command = createEdgeMoveCommand();
			command.setEdge((Edge)request.getConnectionEditPart().getModel());
			command.setSource((Node)request.getTarget().getModel());
			return command;
		}
		return null;
	}	
	
}
