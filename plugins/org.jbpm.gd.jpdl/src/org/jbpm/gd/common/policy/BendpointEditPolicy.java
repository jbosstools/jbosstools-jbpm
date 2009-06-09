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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.BendpointRequest;
import org.jbpm.gd.common.command.AbstractBendpointCommand;
import org.jbpm.gd.common.command.BendpointCreateCommand;
import org.jbpm.gd.common.command.BendpointDeleteCommand;
import org.jbpm.gd.common.command.BendpointMoveCommand;
import org.jbpm.gd.common.notation.Edge;

public class BendpointEditPolicy extends
org.eclipse.gef.editpolicies.BendpointEditPolicy {

	protected Command getCreateBendpointCommand(BendpointRequest request) {
		BendpointCreateCommand command = new BendpointCreateCommand();
		fillCommand(request, command);
		return command;
	}

	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		BendpointDeleteCommand command = new BendpointDeleteCommand();
		fillCommand(request, command);
		return command;
	}

	protected Command getMoveBendpointCommand(BendpointRequest request) {
		BendpointMoveCommand command = new BendpointMoveCommand();
		fillCommand(request, command);
		return command;
	}

	private void fillCommand(BendpointRequest request, AbstractBendpointCommand command) {
		Point p = request.getLocation();
		Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
		Point ref2 = getConnection().getTargetAnchor().getReferencePoint();
		command.setRelativeDimensions(p.getDifference(ref1), p.getDifference(ref2));
		command.setEdge((Edge)request.getSource().getModel());
		command.setIndex(request.getIndex());
	}

}
