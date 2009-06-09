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
import org.jbpm.gd.jpdl.model.AbstractNode;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.ProcessDefinition;
import org.jbpm.gd.jpdl.model.Task;

public class JpdlElementAddActionCommand extends Command {
	
	private SemanticElement target;
	private String eventType;
	private Event event;
	private Action action;
	
	public void setTarget(SemanticElement target) {
		this.target = target;
	}
	
	public void setActionId(String actionId) {
		this.eventType = getEventType(actionId);
	}
	
	public void execute() {
		if (action == null) {
			createAction();
		}
		if (target instanceof Event) {
			addAction((Event)target);
		} else {
			addAction(target);
		} 
}
	
	public void undo() {
		if (target instanceof Event) {
			removeAction((Event)target);
		} else {
			removeAction(event);
			if (event.getActionElements().length == 0) {
				removeEvent(target);
			}
		}
	}
	
	private void createAction() {
		action = (Action)target.getFactory().createById("org.jbpm.gd.jpdl.action");
	}
	
	private void createEvent() {
		event = (Event)target.getFactory().createById("org.jbpm.gd.jpdl.event");
		event.setType(eventType);
	}
	
	private void addAction(Event event) {
		event.addActionElement(action);
	}
	
	private void removeAction(Event event) {
		event.removeActionElement(action);
	}
	
	private void removeEvent(SemanticElement element) {
		if (element instanceof ProcessDefinition) {
			((ProcessDefinition)element).removeEvent(event);
		} else if (element instanceof AbstractNode) {
			((AbstractNode)element).removeEvent(event);
		} else if (element instanceof Task) {
			((Task)element).removeEvent(event);
		}
	}
	
	private void addAction(SemanticElement element) {
		if (event == null) {
			prepareEvent(element);
		}
		if (event.getActionElements().length == 0) {
			addEvent(element);
		}
		addAction(event);
	}
	
	private void addEvent(SemanticElement element) {
		if (element instanceof ProcessDefinition) {
			((ProcessDefinition)element).addEvent(event);
		} else if (element instanceof AbstractNode) {
			((AbstractNode)element).addEvent(event);
		} else if (element instanceof Task) {
			((Task)element).addEvent(event);
		}
	}
	
	private Event[] getEvents(SemanticElement element) {
		if (element instanceof ProcessDefinition) {
			return ((ProcessDefinition)element).getEvents();
		} else if (element instanceof AbstractNode) {
			return ((AbstractNode)element).getEvents();
		} else if (element instanceof Task) {
			return ((Task)element).getEvents();
		} else {
			return new Event[0];
		}		
	}
	
	private void prepareEvent(SemanticElement element) {
		Event[] events = getEvents(element);
		for (int i = 0; i < events.length; i++) {
			if (events[i].getType().equals(eventType)) {
				event = events[i];
			}
		}
		if (event == null) {
			createEvent();
		}		
	}
	
	
	private String getEventType(String actionId) {
		if ("beforeSignal".equals(actionId)) return "before-signal";
		if ("afterSignal".equals(actionId)) return "after-signal";
		if ("nodeEnter".equals(actionId)) return "node-enter";
		if ("nodeLeave".equals(actionId)) return "node-leave";
		if ("processStart".equals(actionId)) return "process-start";
		if ("processEnd".equals(actionId)) return "process-end";
		return null;
	}
	
}
