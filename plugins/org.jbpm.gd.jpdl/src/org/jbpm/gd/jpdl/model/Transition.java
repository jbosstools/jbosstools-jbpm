package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.gd.common.model.NamedElement;

public class Transition extends Event implements NamedElement, ExceptionHandlerContainer, DescribableElement {
	
	private List exceptionHandlers = new ArrayList();
	
	private Condition condition;
	
	private String to;
	private String name;
	private Description description;
	
	
	private NodeElement source;
	
	public void setDescription(Description newDescription) {
		Description oldDescription = description;
		description = newDescription;
		firePropertyChange("description", oldDescription, newDescription);
	}
	
	public Description getDescription() {
		return description;
	}
	
	public Condition getCondition() {
		return condition;
	}
	
	public void setCondition(Condition newCondition) {
		Condition oldCondition = condition;
		condition = newCondition;
		firePropertyChange("condition", oldCondition, newCondition);
	}
	
	public void addExceptionHandler(ExceptionHandler exceptionHandler) {
		exceptionHandlers.add(exceptionHandler);
		firePropertyChange("exceptionHandlerAdd", null, exceptionHandler);
	}
	
	public void removeExceptionHandler(ExceptionHandler exceptionHandler) {
		exceptionHandlers.remove(exceptionHandler);
		firePropertyChange("exceptionHandlerRemove", exceptionHandler, null);
	}
	
	public ExceptionHandler[] getExceptionHandlers() {
		return (ExceptionHandler[])exceptionHandlers.toArray(new ExceptionHandler[exceptionHandlers.size()]);
	}

	public void setType(String newType) {
		// Transitions cannot change their type.
	}
	
	public String getType() {
		return "transition-take";
	}
	
	public void setTo(String newTo) {
		String oldTo = to;
		to = newTo;
		firePropertyChange("to", oldTo, newTo);
	}
	
	public String getTo() {
		return to;
	}
	
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		firePropertyChange("name", oldName, newName);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isNameMandatory() {
		return false;
	}
	
	public void setSource(NodeElement newSource) {
		this.source = newSource;
	}
	
	public NodeElement getSource() {
		return source;
	}
	
}
