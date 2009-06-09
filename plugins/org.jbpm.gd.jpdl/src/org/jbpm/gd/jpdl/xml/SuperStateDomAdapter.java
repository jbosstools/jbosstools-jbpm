package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Decision;
import org.jbpm.gd.jpdl.model.Description;
import org.jbpm.gd.jpdl.model.EndState;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.Fork;
import org.jbpm.gd.jpdl.model.Join;
import org.jbpm.gd.jpdl.model.MailNode;
import org.jbpm.gd.jpdl.model.Node;
import org.jbpm.gd.jpdl.model.ProcessState;
import org.jbpm.gd.jpdl.model.State;
import org.jbpm.gd.jpdl.model.SuperState;
import org.jbpm.gd.jpdl.model.TaskNode;
import org.jbpm.gd.jpdl.model.Timer;
import org.jbpm.gd.jpdl.model.Transition;

public class SuperStateDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"description", "node-element", "end-state", "event", "exception-handler", "timer", "transition"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("description", "description");
			NODE_TYPES.put("node", "node-element");
			NODE_TYPES.put("state", "node-element");
			NODE_TYPES.put("task-node", "node-element");
			NODE_TYPES.put("super-state", "node-element");
			NODE_TYPES.put("process-state", "node-element");
			NODE_TYPES.put("fork", "node-element");
			NODE_TYPES.put("join", "node-element");
			NODE_TYPES.put("decision", "node-element");
			NODE_TYPES.put("mail-node",	"node-element");
			NODE_TYPES.put("end-state", "end-state");
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
		SuperState superState = (SuperState)getSemanticElement();
		if (superState != null) {
			setAttribute("async", superState.getAsync());
			setAttribute("name", superState.getName());
			addElement(superState.getDescription());
			addElements(superState.getNodeElements());
			addElements(superState.getEvents());
			addElements(superState.getExceptionHandlers());
			addElements(superState.getTimers());
			addElements(superState.getTransitions());
		}
	}

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		SuperState superState = (SuperState)jpdlElement;
		superState.setAsync(getAttribute("async"));
		superState.setName(getAttribute("name"));
		superState.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("nodeElementAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("nodeElementRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		} else if ("timerAdd".equals(evt.getPropertyName())) {
			addElement((Timer)evt.getNewValue());
		} else if ("timerRemove".equals(evt.getPropertyName())) {
			removeElement((Timer)evt.getOldValue());
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
		} else if ("exceptionHanlderRemove".equals(evt.getPropertyName())) {
			removeElement((ExceptionHandler)evt.getOldValue());
		} else if ("description".equals(evt.getPropertyName())) {
			setElement("description", (SemanticElement)evt.getOldValue(), (Description)evt.getNewValue());
		} else if ("async".equals(evt.getPropertyName())) {
			setAttribute("async", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		SuperState superState = (SuperState)getSemanticElement();
		if ("name".equals(name)) {
			superState.setName(newValue);
		} else if ("async".equals(name)) {
			superState.setAsync(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		SuperState superState = (SuperState)getSemanticElement();
		if ("event".equals(type)) {
			superState.addEvent((Event)jpdlElement);
		} else if ("exception-handler".equals(type)) {
			superState.addExceptionHandler((ExceptionHandler)jpdlElement);
		} else if ("timer".equals(type)) {
			superState.addTimer((Timer)jpdlElement);
		} else if ("transition".equals(type)) {
			superState.addTransition((Transition)jpdlElement);
		} else if ("node".equals(type)) {
			superState.addNodeElement((Node)jpdlElement);
		} else if ("state".equals(type)) {
			superState.addNodeElement((State)jpdlElement);
		} else if ("task-node".equals(type)) {
			superState.addNodeElement((TaskNode)jpdlElement);
		} else if ("super-state".equals(type)) {
			superState.addNodeElement((SuperState)jpdlElement);
		} else if ("process-state".equals(type)) {
			superState.addNodeElement((ProcessState)jpdlElement);
		} else if ("fork".equals(type)) {
			superState.addNodeElement((Fork)jpdlElement);
		} else if ("join".equals(type)) {
			superState.addNodeElement((Join)jpdlElement);
		} else if ("decision".equals(type)) {
			superState.addNodeElement((Decision)jpdlElement);
		} else if ("mail-node".equals(type)) {
			superState.addNodeElement((MailNode)jpdlElement);
		} else if ("end-state".equals(type)) {
			superState.addNodeElement((EndState)jpdlElement);
		} else if ("description".equals(getNodeType(type))) {
			superState.setDescription((Description)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		SuperState superState = (SuperState)getSemanticElement();
		if ("event".equals(type)) {
			superState.removeEvent((Event)child.getSemanticElement());
		} else if ("exception-handler".equals(type)) {
			superState.removeExceptionHandler((ExceptionHandler)child.getSemanticElement());
		} else if ("timer".equals(type)) {
			superState.removeTimer((Timer)child.getSemanticElement());
		} else if ("transition".equals(type)) {
			superState.removeTransition((Transition)child.getSemanticElement());
		} else if ("node".equals(type)) {
			superState.removeNodeElement((Node)child.getSemanticElement());
		} else if ("state".equals(type)) {
			superState.removeNodeElement((State)child.getSemanticElement());
		} else if ("task-node".equals(type)) {
			superState.removeNodeElement((TaskNode)child.getSemanticElement());
		} else if ("super-state".equals(type)) {
			superState.removeNodeElement((SuperState)child.getSemanticElement());
		} else if ("process-state".equals(type)) {
			superState.removeNodeElement((ProcessState)child.getSemanticElement());
		} else if ("fork".equals(type)) {
			superState.removeNodeElement((Fork)child.getSemanticElement());
		} else if ("join".equals(type)) {
			superState.removeNodeElement((Join)child.getSemanticElement());
		} else if ("decision".equals(type)) {
			superState.removeNodeElement((Decision)child.getSemanticElement());
		} else if ("mail-node".equals(type)) {
			superState.removeNodeElement((MailNode)child.getSemanticElement());
		} else if ("end-state".equals(type)) {
			superState.removeNodeElement((EndState)child.getSemanticElement());
		} else if ("description".equals(getNodeType(type))) {
			superState.setDescription(null);
		}
	}
	
}
