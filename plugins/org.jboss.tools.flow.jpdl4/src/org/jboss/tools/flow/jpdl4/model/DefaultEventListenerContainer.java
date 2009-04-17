package org.jboss.tools.flow.jpdl4.model;

import java.util.ArrayList;
import java.util.List;

public class DefaultEventListenerContainer {
	
	private String eventName;
	private List<EventListener> eventListeners = new ArrayList<EventListener>();

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public void addEventListener(EventListener eventListener) {
		eventListeners.add(eventListener);
	}
	
	public void removeEventListener(EventListener eventListener) {
		eventListeners.remove(eventListener);
	}
	
}
