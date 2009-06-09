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
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.jpdl.model.ProcessDefinition;
import org.jbpm.gd.jpdl.model.StartState;
import org.jbpm.gd.jpdl.model.Task;
import org.jbpm.gd.jpdl.model.TaskNode;

public class JpdlElementAddTaskCommand extends Command {
	
	private SemanticElement target;
	private Task task;
	
	public void setTarget(SemanticElement target) {
		this.target = target;
	}
	
	public void execute() {
		if (task == null) {
			task = (Task)target.getFactory().createById("org.jbpm.gd.jpdl.task");
		}
		if (target instanceof ProcessDefinition) {
			((ProcessDefinition)target).addTask(task);
		} else if (target instanceof StartState){
			((StartState)target).setTask(task);
		} else if (target instanceof TaskNode) {
			((TaskNode)target).addTask(task);
		}
}
	
	public void undo() {
		if (target instanceof ProcessDefinition) {
			((ProcessDefinition)target).removeTask(task);
		} else if (target instanceof StartState){
			((StartState)target).setTask(null);
		} else if (target instanceof TaskNode) {
			((TaskNode)target).removeTask(task);
		}
	}
	
	public boolean canExecute() {
		if (target instanceof StartState) {
			return ((StartState)target).getTask() == null;
		}
		return true;
	}
	
}
