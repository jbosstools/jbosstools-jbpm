package org.jbpm.gd.jpdl.model;

public class Action extends Delegation 
implements ActionElement, AsyncableElement, AcceptPropagatedEventsElement {
	
	private String name;
	private String refName;
	private String acceptPropagatedEvents = "true";
	private String expression;
	private String async = "false";
	
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		firePropertyChange("name", oldName, newName);
	}
	
	public String getName() {
		return name;
	}
	
	public void setRefName(String newRefName) {
		String oldRefName = refName;
		refName = newRefName;
		firePropertyChange("refName", oldRefName, newRefName);
	}
	
	public String getRefName() {
		return refName;
	}
	
	public void setAcceptPropagatedEvents(String newAcceptPropagatedEvents) {
		String oldAcceptPropagatedEvents = acceptPropagatedEvents;
		acceptPropagatedEvents = newAcceptPropagatedEvents;
		firePropertyChange("acceptPropagatedEvents", oldAcceptPropagatedEvents, newAcceptPropagatedEvents);
	}
	
	public String getAcceptPropagatedEvents() {
		return acceptPropagatedEvents;
	}
	
	public void setExpression(String newExpression) {
		String oldExpression = expression;
		expression = newExpression == null ? null : newExpression.trim();
		firePropertyChange("expression", oldExpression, expression);
	}
	
	public String getExpression() {
		return expression;
	}
	
	public void setAsync(String newAsync) {
		String oldAsync = async;
		async = newAsync;
		firePropertyChange("async", oldAsync, newAsync);
	}
	
	public String getAsync() {
		return async;
	}
	
	public boolean isNameMandatory() {
		return false;
	}

}
