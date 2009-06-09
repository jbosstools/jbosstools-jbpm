package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.gd.common.model.AbstractNamedElement;

public class ProcessDefinition extends AbstractNamedElement implements 
DescribableElement, NodeElementContainer, EventContainer, ActionElementContainer, TaskContainer, ExceptionHandlerContainer {
	
	private List swimlanes = new ArrayList();
	private StartState startState;
	private List nodeElements = new ArrayList();
	private List actionElements = new ArrayList();
	private List events = new ArrayList();
	private List exceptionHandlers = new ArrayList();
	private List tasks = new ArrayList();
	private HashMap customProperties = new HashMap();
	private Description description;
	
	public void setDescription(Description newDescription) {
		Description oldDescription = description;
		description = newDescription;
		firePropertyChange("description", oldDescription, newDescription);
	}
	
	public Description getDescription() {
		return description;
	}
		
	public void setProperty(String name, String newValue) {
		String oldValue = (String)customProperties.get(name);
		customProperties.put(name, newValue);
		firePropertyChange("custom", new String[] { name, oldValue }, new String[] {name, newValue});
	}
	
	public String getProperty(String name) {
		return (String)customProperties.get(name);
	}
	
	public Map getProperties() {
		return new HashMap(customProperties);
	}
	
	public void addSwimlane(Swimlane swimlane) {
		swimlanes.add(swimlane);
		firePropertyChange("swimlaneAdd", null, swimlane);
	}
	
	public void removeSwimlane(Swimlane swimlane) {
		swimlanes.remove(swimlane);
		firePropertyChange("swimlaneRemove", swimlane, null);
	}
	
	public Swimlane[] getSwimlanes() {
		return (Swimlane[])swimlanes.toArray(new Swimlane[swimlanes.size()]);
	}
	
	public void addStartState(StartState startState) {
		if (this.startState != null) return;
		this.startState = startState;
		nodeElements.add(0, startState);
		firePropertyChange("startStateAdd", null, startState);
	}
	
	public void removeStartState(StartState startState) {
		if (this.startState != startState || this.startState == null) return;
		this.startState = null;
		nodeElements.remove(0);
		firePropertyChange("startStateRemove", startState, null);
	}
	
	public StartState getStartState() {
		return startState;
	}
	
	public void addNodeElement(NodeElement nodeElement) {
		nodeElements.add(nodeElement);
		firePropertyChange("nodeElementAdd", null, nodeElement);
	}
	
	public void removeNodeElement(NodeElement nodeElement) {
		nodeElements.remove(nodeElement);
		firePropertyChange("nodeElementRemove", nodeElement, null);
	}
	
	public NodeElement[] getNodeElements() {
		return (NodeElement[])nodeElements.toArray(new NodeElement[nodeElements.size()]);
	}
	
	public void addActionElement(ActionElement actionElement) {
		actionElements.add(actionElement);
		firePropertyChange("actionElementAdd", null, actionElement);
	}
	
	public void removeActionElement(ActionElement actionElement) {
		actionElements.remove(actionElement);
		firePropertyChange("actionElementRemove", actionElement, null);
	}
	
	public ActionElement[] getActionElements() {
		return (ActionElement[])actionElements.toArray(new ActionElement[actionElements.size()]);
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
	
	public void addTask(Task task) {
		tasks.add(task);
		firePropertyChange("taskAdd", null, task);
	}
	
	public void removeTask(Task task) {
		tasks.remove(task);
		firePropertyChange("taskRemove", task, null);
	}
	
	public Task[] getTasks() {
		return (Task[])tasks.toArray(new Task[tasks.size()]);
	}
	
	public boolean canAdd(NodeElement node) {
		return !nodeElements.contains(node) && node.isPossibleChildOf(this);
	}
	
	public NodeElement getNodeElementByName(String name) {
		if (name == null) return null;
		NodeElement[] nodeElements = getNodeElements();
		for (int i = 0; i < nodeElements.length; i++) {
			if (name.equals(nodeElements[i].getName())) {
				return nodeElements[i];
			}
		}
		return null;
	}
	
	public Swimlane getSwimlaneByName(String name) {
		Swimlane[] swimlanes = getSwimlanes();
		for (int i = 0; i < swimlanes.length; i++) {
			if (swimlanes[i].getName().equals(name)) {
				return swimlanes[i];
			}
		}
		return null;
	}
	
	public String getFullyQualifiedName() {
		return "/";
	}
	
}
