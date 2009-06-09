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
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.EventContainer;

public class EventCreateCommand extends Command {
	
	private EventContainer eventContainer;
	private Event event;
	private SemanticElementFactory factory;
	
	public EventCreateCommand(SemanticElementFactory factory) {
		this.factory = factory;
	}
	
	public void execute() {
		if (event == null) {
			event = (Event)factory.createById("org.jbpm.gd.jpdl.event");
		}
		eventContainer.addEvent(event);
	}
	
	public void undo() {
		eventContainer.removeEvent(event);
	}
	
	public void setEventContainer(EventContainer eventContainer) {
		this.eventContainer = eventContainer;
	}
	
}
