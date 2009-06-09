package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Description;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.Fork;
import org.jbpm.gd.jpdl.model.Script;
import org.jbpm.gd.jpdl.model.Timer;
import org.jbpm.gd.jpdl.model.Transition;

public class ForkDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"description", "script", "event", "exception-handler", "timer", "transition"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("description", "description");
			NODE_TYPES.put("script", "script");
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
	
	protected void initialize() {
		super.initialize();
		Fork fork = (Fork)getSemanticElement();
		if (fork != null) {
			setAttribute("name", fork.getName());
			setAttribute("async", fork.getAsync());
			addElement(fork.getDescription());
			addElements(fork.getScripts());
			addElements(fork.getEvents());
			addElements(fork.getExceptionHandlers());
			addElements(fork.getTimers());
			addElements(fork.getTransitions());
		}
	}

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Fork fork = (Fork)jpdlElement;
		fork.setAsync(getAttribute("async"));
		fork.setName(getAttribute("name"));
		fork.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("scriptAdd".equals(evt.getPropertyName())) {
			addElement((Script)evt.getNewValue());
		} else if ("scriptRemove".equals(evt.getPropertyName())) {
			removeElement((Script)evt.getOldValue());
		} else if ("eventAdd".equals(evt.getPropertyName())) {
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
		Fork fork = (Fork)getSemanticElement();
		if ("name".equals(name)) {
			fork.setName(newValue);
		} else if ("async".equals(name)) {
			fork.setAsync(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		Fork fork = (Fork)getSemanticElement();
		if ("script".equals(type)) { 
			fork.addScript((Script)jpdlElement);
		} else if ("event".equals(type)) {
			fork.addEvent((Event)jpdlElement);
		} else if ("exception-handler".equals(type)) {
			fork.addExceptionHandler((ExceptionHandler)jpdlElement);
		} else if ("timer".equals(type)) {
			fork.addTimer((Timer)jpdlElement);
		} else if ("transition".equals(type)) {
			fork.addTransition((Transition)jpdlElement);
		} else if ("description".equals(getNodeType(type))) {
			fork.setDescription((Description)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		Fork fork = (Fork)getSemanticElement();
		if ("script".equals(type)) {
			fork.removeScript((Script)child.getSemanticElement());
		} else if ("event".equals(type)) {
			fork.removeEvent((Event)child.getSemanticElement());
		} else if ("exception-handler".equals(type)) {
			fork.removeExceptionHandler((ExceptionHandler)child.getSemanticElement());
		} else if ("timer".equals(type)) {
			fork.removeTimer((Timer)child.getSemanticElement());
		} else if ("transition".equals(type)) {
			fork.removeTransition((Transition)child.getSemanticElement());
		} else if ("description".equals(getNodeType(type))) {
			fork.setDescription(null);
		}
	}


}
