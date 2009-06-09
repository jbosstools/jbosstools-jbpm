package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.gd.common.model.AbstractNamedElement;

public class AbstractNode extends AbstractNamedElement implements NodeElement, DescribableElement {

	private List events = new ArrayList();
	private List exceptionHandlers = new ArrayList();
	private List transitions = new ArrayList();
	private Description description;
	
	public void setDescription(Description newDescription) {
		Description oldDescription = description;
		description = newDescription;
		firePropertyChange("description", oldDescription, newDescription);
	}
	
	public Description getDescription() {
		return description;
	}
	
	public void addEvent(Event event) {
		events.add(event);
		firePropertyChange("eventAdd", null, event);
	}
	
	public void removeEvent(Event event) {
		events.remove(event);
		firePropertyChange("eventRemove", event, null);
	}
	
	public Event[] getEvents() {
		return (Event[])events.toArray(new Event[events.size()]);
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
	
	public void addTransition(Transition transition) {
		if (transitions.contains(transition)) return;
		transitions.add(transition);
		firePropertyChange("transitionAdd", null, transition);
	}
	
	public void removeTransition(Transition transition) {
		if (!(transitions.contains(transition))) return;
		transitions.remove(transition);
		firePropertyChange("transitionRemove", transition, null);
	}
	
	public Transition[] getTransitions() {
		return (Transition[])transitions.toArray(new Transition[transitions.size()]);
	}
	
	public boolean isPossibleChildOf(NodeElementContainer nodeElementContainer) {
		return true;
	}
	
	public void initializeName(NodeElementContainer nodeElementContainer) {
		int runner = 1;
		String prefix = getNamePrefix();
		while (true) {
			String candidate = prefix + runner;
			if (nodeElementContainer.getNodeElementByName(candidate) == null) {
				setName(candidate);
				return;
			}
			runner ++;
		}
	}
	
}
