package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Description;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.StartState;
import org.jbpm.gd.jpdl.model.Task;
import org.jbpm.gd.jpdl.model.Transition;

public class StartStateDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"description", "task", "transition", "event", "exception-handler"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("description", "description");
			NODE_TYPES.put("task", "task");
			NODE_TYPES.put("transition", "transition");
			NODE_TYPES.put("event", "event");
			NODE_TYPES.put("exception-handler", "exception-handler");
		}
		return NODE_TYPES;
	}
	
	protected void initialize() {
		super.initialize();
		StartState startState = (StartState)getSemanticElement();
		if (startState != null) {
			setAttribute("name", startState.getName());
			addElement(startState.getDescription());
			addElement(startState.getTask());
			addElements(startState.getTransitions());
			addElements(startState.getEvents());
			addElements(startState.getExceptionHandlers());
		}
	}

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		StartState startState = (StartState)jpdlElement;
		startState.setName(getAttribute("name"));
		startState.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("task".equals(evt.getPropertyName())) {
			setElement("task", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("description".equals(evt.getPropertyName())) {
			setElement("description", (SemanticElement)evt.getOldValue(), (Description)evt.getNewValue());
		} else if ("taskRemove".equals(evt.getPropertyName())) {
			removeElement((Task)evt.getOldValue());
		} else if ("transitionAdd".equals(evt.getPropertyName())) {
			addElement((Transition)evt.getNewValue());
		} else if ("transitionRemove".equals(evt.getPropertyName())) {
			removeElement((Transition)evt.getOldValue());
		} else if ("eventAdd".equals(evt.getPropertyName())) {
			addElement((Event)evt.getNewValue());
		} else if ("eventRemove".equals(evt.getPropertyName())) {
			removeElement((Event)evt.getOldValue());
		} else if ("exceptionHandlerAdd".equals(evt.getPropertyName())) {
			addElement((ExceptionHandler)evt.getNewValue());
		} else if ("exceptionHandlerRemove".equals(evt.getPropertyName())) {
			removeElement((ExceptionHandler)evt.getOldValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}

	protected void doModelUpdate(String name, String newValue) {
		StartState startState = (StartState)getSemanticElement();
		if ("name".equals(name)) {
			startState.setName(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		StartState startState = (StartState)getSemanticElement();
		if ("task".equals(type)) {
			startState.setTask((Task)jpdlElement);
		} else if ("description".equals(getNodeType(type))) {
			startState.setDescription((Description)jpdlElement);
		} else if ("transition".equals(type)) {
			startState.addTransition((Transition)jpdlElement);
		} else if ("event".equals(type)) {
			startState.addEvent((Event)jpdlElement);
		} else if ("exception-handler".equals(type)) {
			startState.addExceptionHandler((ExceptionHandler)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		StartState startState = (StartState)getSemanticElement();
		if ("task".equals(type)) {
			startState.setTask(null);
		} else if ("description".equals(getNodeType(type))) {
			startState.setDescription(null);
		} else if ("transition".equals(type)) {
			startState.removeTransition((Transition)child.getSemanticElement());
		} else if ("event".equals(type)) {
			startState.removeEvent((Event)child.getSemanticElement());
		} else if ("exception-handler".equals(type)) {
			startState.removeExceptionHandler((ExceptionHandler)child.getSemanticElement());
		}
	}

}
