package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Description;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.Task;
import org.jbpm.gd.jpdl.model.TaskNode;
import org.jbpm.gd.jpdl.model.Timer;
import org.jbpm.gd.jpdl.model.Transition;

public class TaskNodeDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"description", "task", "event", "exception-handler", "timer", "transition"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("description", "description");
			NODE_TYPES.put("task", "task");
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
		TaskNode taskNode = (TaskNode)getSemanticElement();
		if (taskNode != null) {
			setAttribute("async", taskNode.getAsync());
			setAttribute("name", taskNode.getName());
			setAttribute("signal", taskNode.getSignal());
			setAttribute("create-tasks", taskNode.getCreateTasks());
			setAttribute("end-tasks", taskNode.getEndTasks());
			addElement(taskNode.getDescription());
			addElements(taskNode.getTasks());
			addElements(taskNode.getEvents());
			addElements(taskNode.getExceptionHandlers());
			addElements(taskNode.getTimers());
			addElements(taskNode.getTransitions());
		}
	}

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		TaskNode taskNode = (TaskNode)jpdlElement;
		taskNode.setAsync(getAttribute("async"));
		taskNode.setName(getAttribute("name"));
		taskNode.setSignal(getAttribute("signal"));
		taskNode.setCreateTasks(getAttribute("create-tasks"));
		taskNode.setEndTasks(getAttribute("end-tasks"));
		taskNode.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("taskAdd".equals(evt.getPropertyName())) {
			addElement((Task)evt.getNewValue());
		} else if ("taskRemove".equals(evt.getPropertyName())) {
			removeElement((Task)evt.getOldValue());
		} else if ("timerAdd".equals(evt.getPropertyName())) {
			addElement((Timer)evt.getNewValue());
		} else if ("timerRemove".equals(evt.getPropertyName())) {
			removeElement((Timer)evt.getOldValue());
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
		} else if ("description".equals(evt.getPropertyName())) {
			setElement("description", (SemanticElement)evt.getOldValue(), (Description)evt.getNewValue());
		} else if ("createTasks".equals(evt.getPropertyName())) {
			setAttribute("create-tasks", (String)evt.getNewValue()); 
		} else if ("endTasks".equals(evt.getPropertyName())) {
			setAttribute("end-tasks", (String)evt.getNewValue());
		} else if ("signal".equals(evt.getPropertyName())) {
			setAttribute("signal", (String)evt.getNewValue());
		} else if ("async".equals(evt.getPropertyName())) {
			setAttribute("async", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}

	protected void doModelUpdate(String name, String newValue) {
		TaskNode taskNode = (TaskNode)getSemanticElement();
		if ("name".equals(name)) {
			taskNode.setName(newValue);
		} else if ("async".equals(name)) {
			taskNode.setAsync(newValue);
		} else if ("signal".equals(name)) {
			taskNode.setSignal(newValue);
		} else if ("end-tasks".equals(name)) {
			taskNode.setEndTasks(newValue);
		} else if ("create-tasks".equals(name)) {
			taskNode.setCreateTasks(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		TaskNode taskNode = (TaskNode)getSemanticElement();
		if ("event".equals(type)) {
			taskNode.addEvent((Event)jpdlElement);
		} else if ("exception-handler".equals(type)) {
			taskNode.addExceptionHandler((ExceptionHandler)jpdlElement);
		} else if ("timer".equals(type)) {
			taskNode.addTimer((Timer)jpdlElement);
		} else if ("transition".equals(type)) {
			taskNode.addTransition((Transition)jpdlElement);
		} else if ("task".equals(type)) {
			taskNode.addTask((Task)jpdlElement);
		} else if ("description".equals(getNodeType(type))) {
			taskNode.setDescription((Description)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		TaskNode taskNode = (TaskNode)getSemanticElement();
		if ("event".equals(type)) {
			taskNode.removeEvent((Event)child.getSemanticElement());
		} else if ("exception-handler".equals(type)) {
			taskNode.removeExceptionHandler((ExceptionHandler)child.getSemanticElement());
		} else if ("timer".equals(type)) {
			taskNode.removeTimer((Timer)child.getSemanticElement());
		} else if ("transition".equals(type)) {
			taskNode.removeTransition((Transition)child.getSemanticElement());
		} else if ("task".equals(type)) {
			taskNode.removeTask((Task)child.getSemanticElement());
		} else if ("description".equals(getNodeType(type))) {
			taskNode.setDescription(null);
		}
	}

}