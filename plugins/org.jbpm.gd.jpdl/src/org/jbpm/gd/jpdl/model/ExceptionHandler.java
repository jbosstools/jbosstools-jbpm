package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.gd.common.model.AbstractSemanticElement;

public class ExceptionHandler extends AbstractSemanticElement implements ActionElementContainer {
	
	private List actionElements = new ArrayList();
	private String exceptionClass;
	
	public void addActionElement(ActionElement actionElement) {
		actionElements.add(actionElement);
		firePropertyChange("actionElementAdd", null, actionElement);
	}
	
	public void removeActionElement(ActionElement actionElement) {
		actionElements.remove(actionElement);
		firePropertyChange("actionElementRemove", actionElement, null);
	}
	
	public void addAction(Action action) {
		actionElements.add(action);
		firePropertyChange("actionElementAdd", null, action);
	}
	
	public void removeAction(Action action) {
		actionElements.remove(action);
		firePropertyChange("actionElementRemove", action, null);
	}
	
	public void addScript(Script script) {
		actionElements.add(script);
		firePropertyChange("actionElementAdd", null, script);
	}
	
	public void removeScript(Script script) {
		actionElements.remove(script);
		firePropertyChange("actionElementRemove", script, null);
	}
	
	public ActionElement[] getActionElements() {
		return (ActionElement[])actionElements.toArray(new ActionElement[actionElements.size()]);
	}
	
	public String getExceptionClass() {
		return exceptionClass;
	}
	
	public void setExceptionClass(String newExceptionClass) {
		String oldExceptionClass = exceptionClass;
		exceptionClass = newExceptionClass;
		firePropertyChange("exceptionClass", oldExceptionClass, newExceptionClass);
	}

}
