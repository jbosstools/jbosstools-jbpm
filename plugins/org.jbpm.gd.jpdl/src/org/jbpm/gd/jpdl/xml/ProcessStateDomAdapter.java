package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Description;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.ProcessState;
import org.jbpm.gd.jpdl.model.SubProcess;
import org.jbpm.gd.jpdl.model.Timer;
import org.jbpm.gd.jpdl.model.Transition;
import org.jbpm.gd.jpdl.model.Variable;

public class ProcessStateDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"description", "sub-process", "variable", "event", "exception-handler", "timer", "transition"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("description", "description");
			NODE_TYPES.put("sub-process", "sub-process");
			NODE_TYPES.put("variable", "variable");
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
		ProcessState processState = (ProcessState)getSemanticElement();
		if (processState != null) {
			setAttribute("async", processState.getAsync());
			setAttribute("name", processState.getName());
			addElement(processState.getDescription());
			addElement(processState.getSubProcess());
			addElements(processState.getVariables());
			addElements(processState.getEvents());
			addElements(processState.getExceptionHandlers());
			addElements(processState.getTimers());
			addElements(processState.getTransitions());
		}
	}

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		ProcessState processState = (ProcessState)jpdlElement;
		processState.setAsync(getAttribute("async"));
		processState.setName(getAttribute("name"));
		processState.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("variableAdd".equals(evt.getPropertyName())) {
			addElement((Variable)evt.getNewValue());
		} else if ("variableRemove".equals(evt.getPropertyName())) {
			removeElement((Variable)evt.getOldValue());
		} else if ("timerAdd".equals(evt.getPropertyName())) {
			addElement((Timer)evt.getNewValue());
		} else if ("timerRemove".equals(evt.getPropertyName())) {
			removeElement((Timer)evt.getOldValue());
		} else if ("transitionAdd".equals(evt.getPropertyName())) {
			addElement((Transition)evt.getNewValue());
		} else if ("transitionRemove".equals(evt.getPropertyName())) {
			removeElement((Transition)evt.getOldValue());
		} else  if ("eventAdd".equals(evt.getPropertyName())) {
			addElement((Event)evt.getNewValue());
		} else if ("eventRemove".equals(evt.getPropertyName())) {
			removeElement((Event)evt.getOldValue());
		} else if ("exceptionHandlerAdd".equals(evt.getPropertyName())) {
			addElement((ExceptionHandler)evt.getNewValue());
		} else if ("exceptionHanlderRemove".equals(evt.getPropertyName())) {
			removeElement((ExceptionHandler)evt.getOldValue());
		} else if ("subProcess".equals(evt.getPropertyName())) {
			setElement("subProcess", (SemanticElement)evt.getOldValue(), (Description)evt.getNewValue());
		} else if ("description".equals(evt.getPropertyName())) {
			setElement("description", (SemanticElement)evt.getOldValue(), (Description)evt.getNewValue());
		} else if ("async".equals(evt.getPropertyName())) {
			setAttribute("async", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		ProcessState processState = (ProcessState)getSemanticElement();
		if ("name".equals(name)) {
			processState.setName(newValue);
		} else if ("async".equals(name)) {
			processState.setAsync(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		ProcessState processState = (ProcessState)getSemanticElement();
		if ("event".equals(type)) {
			processState.addEvent((Event)jpdlElement);
		} else if ("exception-handler".equals(type)) {
			processState.addExceptionHandler((ExceptionHandler)jpdlElement);
		} else if ("timer".equals(type)) {
			processState.addTimer((Timer)jpdlElement);
		} else if ("transition".equals(type)) {
			processState.addTransition((Transition)jpdlElement);
		} else if ("variable".equals(type)) {
			processState.addVariable((Variable)jpdlElement);
		} else if ("sub-process".equals(type)) {
			processState.setSubProcess((SubProcess)jpdlElement);
		} else if ("description".equals(getNodeType(type))) {
			processState.setDescription((Description)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		ProcessState processState = (ProcessState)getSemanticElement();
		if ("event".equals(type)) {
			processState.removeEvent((Event)child.getSemanticElement());
		} else if ("exception-handler".equals(type)) {
			processState.removeExceptionHandler((ExceptionHandler)child.getSemanticElement());
		} else if ("timer".equals(type)) {
			processState.removeTimer((Timer)child.getSemanticElement());
		} else if ("transition".equals(type)) {
			processState.removeTransition((Transition)child.getSemanticElement());
		} else if ("variable".equals(type)) {
			processState.removeVariable((Variable)child.getSemanticElement());
		} else if ("sub-process".equals(type)) {
			processState.setSubProcess(null);
		} else if ("description".equals(getNodeType(type))) {
			processState.setDescription(null);
		}
	}

}
