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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.jbpm.gd.common.notation.BendPoint;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;

public abstract class AbstractEdgeCreateCommand extends Command {
	
	protected Node source;
	protected Node target;
	protected Edge edge;
	
	public void execute() {
		if (source == target && edge.getBendPoints().isEmpty()) {
			addBendPoints();
		}
	}
	
	private void addBendPoints() {
		Rectangle constraint = source.getConstraint();
		int horizontal = - (constraint.width / 2 + 25);
		int vertical = horizontal * constraint.height / constraint.width;
		BendPoint first = new BendPoint();
		first.setRelativeDimensions(new Dimension(horizontal, 0), new Dimension(horizontal, 0));
		BendPoint second = new BendPoint();
		second.setRelativeDimensions(new Dimension(horizontal, vertical), new Dimension(horizontal, vertical));
		edge.addBendPoint(first);
		edge.addBendPoint(second);
	}
	
	public boolean canExecute() {
		if (source == null || target == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setSource(Node newSource) {
		source = newSource;
	}
	
	public void setEdge(Edge newEdge) {
		edge = newEdge;
	}
	
	public void setTarget(Node newTarget) {
		target = newTarget;
	}
	
}
