package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Description;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.State;
import org.jbpm.gd.jpdl.model.Timer;
import org.jbpm.gd.jpdl.model.Transition;

public class StateDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"description", "event", "exception-handler", "timer", "transition"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("description", "description");
			NODE_TYPES.put("event", "event");
			NODE_TYPES.put("exception-handler", "exception-handler");
			NODE_TYPES.put("timer", "timer");
			NODE_TYPES.put("transition", "transition");
		}
		return NODE_TYPES;
	}
	
	protected String getDefaultValue(String attributeName) {
		if ("async".equals(attributeName)) {
			return "false";
		} else {
			return super.getDefaultValue(attributeName);
		}
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		State state = (State)jpdlElement;
		state.setAsync(getAttribute("async"));
		state.setName(getAttribute("name"));
		state.addPropertyChangeListener(this);
	}

	protected void initialize() {
		super.initialize();
		State state = (State)getSemanticElement();
		if (state != null) {
			setAttribute("async", state.getAsync());
			setAttribute("name", state.getName());
			addElement(state.getDescription());
			addElements(state.getEvents());
			addElements(state.getExceptionHandlers());
			addElements(state.getTimers());
			addElements(state.getTransitions());
		}
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("eventAdd".equals(evt.getPropertyName())) {
			addElement((Event)evt.getNewValue());
		} else if ("eventRemove".equals(evt.getPropertyName())) {
			removeElement((Event)evt.getOldValue());
		} else if ("exceptionHandlerAdd".equals(evt.getPropertyName())) {
			addElement((ExceptionHandler)evt.getNewValue());
		} else if ("exceptionHandlerRemove".equals(evt.getPropertyName())) {
			removeElement((ExceptionHandler)evt.getOldValue());
		} else if ("timerAdd".equals(evt.getPropertyName())) {
			addElement((Timer)evt.getNewValue());
		} else if ("timerRemove".equals(evt.getPropertyName())) {
			removeElement((Timer)evt.getOldValue());
		} else if ("transitionAdd".equals(evt.getPropertyName())) {
			addElement((Transition)evt.getNewValue());
		} else if ("transitionRemove".equals(evt.getPropertyName())) {
			removeElement((Transition)evt.getOldValue());
		} else if ("description".equals(evt.getPropertyName())) {
			setElement("description", (SemanticElement)evt.getOldValue(), (Description)evt.getNewValue());
		} else if ("async".equals(evt.getPropertyName())) {
			setAttribute("async", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		State state = (State)getSemanticElement();
		if ("name".equals(name)) {
			state.setName(newValue);
		} else if ("async".equals(name)) {
			state.setAsync(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		State state = (State)getSemanticElement();
		if ("event".equals(type)) {
			state.addEvent((Event)jpdlElement);
		} else if ("exception-handler".equals(type)) {
			state.addExceptionHandler((ExceptionHandler)jpdlElement);
		} else if ("timer".equals(type)) {
			state.addTimer((Timer)jpdlElement);
		} else if ("transition".equals(type)) {
			state.addTransition((Transition)jpdlElement);
		} else if ("description".equals(getNodeType(type))) {
			state.setDescription((Description)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		State state = (State)getSemanticElement();
		if ("event".equals(type)) {
			state.removeEvent((Event)child.getSemanticElement());
		} else if ("exception-handler".equals(type)) {
			state.removeExceptionHandler((ExceptionHandler)child.getSemanticElement());
		} else if ("timer".equals(type)) {
			state.removeTimer((Timer)child.getSemanticElement());
		} else if ("transition".equals(type)) {
			state.removeTransition((Transition)child.getSemanticElement());
		} else if ("description".equals(getNodeType(type))) {
			state.setDescription(null);
		}
	}
}
