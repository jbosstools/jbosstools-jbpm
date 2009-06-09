package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Decision;
import org.jbpm.gd.jpdl.model.Description;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.Handler;
import org.jbpm.gd.jpdl.model.Transition;

public class DecisionDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"description", "handler", "event", "exception-handler", "transition"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("description", "description");
			NODE_TYPES.put("handler", "handler");
			NODE_TYPES.put("event", "event");
			NODE_TYPES.put("exception-handler", "exception-handler");
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
		Decision decision = (Decision)getSemanticElement();
		if (decision != null) {
			setAttribute("name", decision.getName());
			setAttribute("async", decision.getAsync());
			setAttribute("expression", decision.getExpression());
			addElement(decision.getDescription());
			addElement(decision.getHandler());
			addElements(decision.getEvents());
			addElements(decision.getExceptionHandlers());
			addElements(decision.getTransitions());
		}
	}

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Decision decision = (Decision)jpdlElement;
		decision.setExpression(getAttribute("expression"));
		decision.setAsync(getAttribute("async"));
		decision.setName(getAttribute("name"));
		decision.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("handler".equals(evt.getPropertyName())) {
			setElement("handler", (Handler)evt.getOldValue(), (Handler)evt.getNewValue());
		} else if ("description".equals(evt.getPropertyName())) {
			setElement("description", (SemanticElement)evt.getOldValue(), (Description)evt.getNewValue());
    	} else if ("handlerRemove".equals(evt.getPropertyName())) {
			removeElement((Handler)evt.getOldValue());
		} else if ("eventAdd".equals(evt.getPropertyName())) {
			addElement((Event)evt.getNewValue());
		} else if ("eventRemove".equals(evt.getPropertyName())) {
			removeElement((Event)evt.getOldValue());
		} else if ("exceptionHandlerAdd".equals(evt.getPropertyName())) {
			addElement((ExceptionHandler)evt.getNewValue());
		} else if ("exceptionHandlerRemove".equals(evt.getPropertyName())) {
			removeElement((ExceptionHandler)evt.getOldValue());
		} else if ("transitionAdd".equals(evt.getPropertyName())) {
			addElement((Transition)evt.getNewValue());
		} else if ("transitionRemove".equals(evt.getPropertyName())) {
			removeElement((Transition)evt.getOldValue());
		} else if ("expression".equals(evt.getPropertyName())) {
			setAttribute("expression", (String)evt.getNewValue());
		} else if ("async".equals(evt.getPropertyName())) {
			setAttribute("async", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}
		
	protected void doModelUpdate(String name, String newValue) {
		Decision decision = (Decision)getSemanticElement();
		if ("name".equals(name)) {
			decision.setName(newValue);
		} else if ("async".equals(name)) {
			decision.setAsync(newValue);
		} else if ("expression".equals(name)) {
			decision.setExpression(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		Decision decision = (Decision)getSemanticElement();
		if ("handler".equals(type)) { 
			decision.setHandler((Handler)jpdlElement);
		} else if ("description".equals(getNodeType(type))) {
			decision.setDescription((Description)jpdlElement);
		} else if ("event".equals(type)) {
			decision.addEvent((Event)jpdlElement);
		} else if ("exception-handler".equals(type)) {
			decision.addExceptionHandler((ExceptionHandler)jpdlElement);
		} else if ("transition".equals(type)) {
			decision.addTransition((Transition)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		Decision decision = (Decision)getSemanticElement();
		if ("handler".equals(type)) {
			decision.setHandler(null);
		} else if ("description".equals(getNodeType(type))) {
			decision.setDescription(null);
		} else if ("event".equals(type)) {
			decision.removeEvent((Event)child.getSemanticElement());
		} else if ("exception-handler".equals(type)) {
			decision.removeExceptionHandler((ExceptionHandler)child.getSemanticElement());
		} else if ("transition".equals(type)) {
			decision.removeTransition((Transition)child.getSemanticElement());
		}
	}

}
