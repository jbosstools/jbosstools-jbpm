package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.AbstractSemanticElement;

public class Script extends AbstractSemanticElement implements ActionElement, AcceptPropagatedEventsElement {
	
	private String script;
	private String name;
	private String acceptPropagatedEvents = "true";
	
	public void setScript(String newScript) {
		String oldScript = script;
		script = newScript;
		firePropertyChange("script", oldScript, newScript);
	}
	
	public String getScript() {
		return script;
	}
	
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		firePropertyChange("name", oldName, newName);
	}
	
	public String getName() {
		return name;
	}
	
	public void setAcceptPropagatedEvents(String newAcceptPropagatedEvents) {
		String oldAcceptPropagatedEvents = acceptPropagatedEvents;
		acceptPropagatedEvents = newAcceptPropagatedEvents;
		firePropertyChange("acceptPropagatedEvents", oldAcceptPropagatedEvents, newAcceptPropagatedEvents);
	}
	
	public String getAcceptPropagatedEvents() {
		return acceptPropagatedEvents;
	}
	
	public boolean isNameMandatory() {
		return false;
	}

}
