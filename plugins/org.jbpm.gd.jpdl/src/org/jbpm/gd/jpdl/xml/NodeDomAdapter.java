package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.CancelTimer;
import org.jbpm.gd.jpdl.model.CreateTimer;
import org.jbpm.gd.jpdl.model.Description;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.MailAction;
import org.jbpm.gd.jpdl.model.Node;
import org.jbpm.gd.jpdl.model.Script;
import org.jbpm.gd.jpdl.model.Timer;
import org.jbpm.gd.jpdl.model.Transition;

public class NodeDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"action-element", "description", "event", "exception-handler", "timer", "transition"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("description", "description");
			NODE_TYPES.put("action", "action-element");
			NODE_TYPES.put("script", "action-element");
			NODE_TYPES.put("create-timer", "action-element");
			NODE_TYPES.put("cancel-timer", "action-element");
			NODE_TYPES.put("mail", "action-element");
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
		Node node = (Node)getSemanticElement();
		if (node != null) {
			setAttribute("async", node.getAsync());
			setAttribute("name", node.getName());
			addElement(node.getAction());
			addElement(node.getScript());
			addElement(node.getCreateTimer());
			addElement(node.getCancelTimer());
			addElement(node.getMail());
			addElement(node.getDescription());
			addElements(node.getEvents());
			addElements(node.getExceptionHandlers());
			addElements(node.getTimers());
			addElements(node.getTransitions());
		}
	}

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Node node = (Node)jpdlElement;
		node.setAsync(getAttribute("async"));
		node.setName(getAttribute("name"));
		node.addPropertyChangeListener(this);
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("action".equals(evt.getPropertyName())) {
			setElement("action", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("script".equals(evt.getPropertyName())) {
			setElement("script", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("createTimer".equals(evt.getPropertyName())) {
			setElement("create-timer", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("cancelTimer".equals(evt.getPropertyName())) {
			setElement("cancel-timer", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("mail".equals(evt.getPropertyName())) {
			setElement("mail", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("description".equals(evt.getPropertyName())) {
			setElement("description", (SemanticElement)evt.getOldValue(), (Description)evt.getNewValue());
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
		} else if ("async".equals(evt.getPropertyName())) {
			setAttribute("async", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Node node = (Node)getSemanticElement();
		if ("name".equals(name)) {
			node.setName(newValue);
		} else if ("async".equals(name)) {
			node.setAsync(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		Node node = (Node)getSemanticElement();
		if ("action".equals(type)) {
			node.setAction((Action)jpdlElement);
		} else if ("script".equals(type)) {
			node.setScript((Script)jpdlElement);
		} else if ("create-timer".equals(type)) {
			node.setCreateTimer((CreateTimer)jpdlElement);
		} else if ("cancel-timer".equals(type)) {
			node.setCancelTimer((CancelTimer)jpdlElement);
		} else if ("mail".equals(type)) {
			node.setMail((MailAction)jpdlElement);
		} else if ("description".equals(getNodeType(type))) {
			node.setDescription((Description)jpdlElement);
		} else if ("event".equals(type)) {
			node.addEvent((Event)jpdlElement);
		} else if ("exception-handler".equals(type)) {
			node.addExceptionHandler((ExceptionHandler)jpdlElement);
		} else if ("timer".equals(type)) {
			node.addTimer((Timer)jpdlElement);
		} else if ("transition".equals(type)) {
			node.addTransition((Transition)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		Node node = (Node)getSemanticElement();
		if ("action".equals(type)) {
			node.setAction(null);
		} else if ("script".equals(type)) {
			node.setScript(null);
		} else if ("create-timer".equals(type)) {
			node.setCreateTimer(null);
		} else if ("cancel-timer".equals(type)) {
			node.setCancelTimer(null);
		} else if ("mail".equals(type)) {
			node.setMail(null);
		} else if ("description".equals(getNodeType(type))) {
			node.setDescription(null);
		} else if ("event".equals(type)) {
			node.removeEvent((Event)child.getSemanticElement());
		} else if ("exception-handler".equals(type)) {
			node.removeExceptionHandler((ExceptionHandler)child.getSemanticElement());
		} else if ("timer".equals(type)) {
			node.removeTimer((Timer)child.getSemanticElement());
		} else if ("transition".equals(type)) {
			node.removeTransition((Transition)child.getSemanticElement());
		}
	}



}
