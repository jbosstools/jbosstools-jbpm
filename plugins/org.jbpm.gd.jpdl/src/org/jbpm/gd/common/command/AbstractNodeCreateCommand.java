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
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NotationElement;

public abstract class AbstractNodeCreateCommand extends Command {
	
	protected Node node;
	protected Point location;
	protected NotationElement parent;
	
	public void execute() {
		setConstraint();
	}
	
	protected void setConstraint() {
		if (location != null) {
			Dimension dimension = new Dimension (-1, -1);
			Rectangle rectangle = node.getConstraint();
			if (rectangle != null && rectangle.getSize() != null) {
				dimension = rectangle.getSize();
			}
			node.setConstraint(new Rectangle(location, dimension));
		}		
	}
	
	public void setNode(Node node) {
		this.node = node;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	public void setParent(NotationElement parent) {
		this.parent = parent;
	}
	
}
