package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.gd.common.model.AbstractSemanticElement;

public class Event extends AbstractSemanticElement implements ActionElementContainer {
	
	public static final String[] PREDEFINED_EVENT_TYPES = new String[] {
			"transition", 
			"before-signal", 
			"after-signal", 
			"process-start", 
			"process-end",
			"node-enter",
			"node-leave",
			"superstate-enter",
			"superstate-leave",
			"subprocess-created",
			"subprocess-end",
			"task-create",
			"task-assign",
			"task-start",
			"task-end",
			"timer"
		};
	
	private List actionElements = new ArrayList();
	private String type;
	
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
	
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		firePropertyChange("type", oldType, newType);
	}
	
	public String getType() {
		return type;
	}

}
