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

import org.eclipse.gef.commands.Command;
import org.jbpm.gd.common.model.SemanticElementFactory;
import org.jbpm.gd.jpdl.model.Assignment;
import org.jbpm.gd.jpdl.model.Swimlane;

public class AssignmentCreateCommand extends Command {
	
	private Swimlane swimlane;
	private Assignment assignment;
	private SemanticElementFactory factory;
	
	public AssignmentCreateCommand(Swimlane swimlane, SemanticElementFactory factory) {
		this.swimlane = swimlane;
		this.factory = factory;
	}
	
	public void execute() {
		if (assignment == null) {
			assignment = (Assignment)factory.createById("org.jbpm.gd.jpdl.assignment");
		}
		swimlane.setAssignment(assignment);
	}
	
	public void undo() {
		swimlane.setAssignment(null);
	}
	
}
