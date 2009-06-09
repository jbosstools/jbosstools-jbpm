package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.SemanticElement;


public interface EventContainer extends SemanticElement {
	
	public void addEvent(Event event);	
	public void removeEvent(Event event);	
	public Event[] getEvents();
}
