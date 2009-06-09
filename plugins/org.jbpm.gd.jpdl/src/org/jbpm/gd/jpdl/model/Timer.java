package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.AbstractNamedElement;

public class Timer extends AbstractNamedElement {
	
	private ActionElement actionElement;
	private String dueDate = "";
	private String repeat;
	private String transition;
		
	public void setAction(Action newActionElement) {
		ActionElement oldActionElement = actionElement;
		actionElement = newActionElement;
		firePropertyChange("action", oldActionElement, newActionElement);
	}
	
	public Action getAction() {
		if (actionElement instanceof Action) {
			return (Action)actionElement;
		}
		return null;
	}
	
	public void setScript(Script newActionElement) {
		ActionElement oldActionElement = actionElement;
		actionElement = newActionElement;
		firePropertyChange("script", oldActionElement, newActionElement);
	}
	
	public Script getScript() {
		if (actionElement instanceof Script) {
			return (Script)actionElement;
		}
		return null;
	}
	
	public void setDueDate(String newDueDate) {
		String oldDueDate = dueDate;
		dueDate = newDueDate;
		firePropertyChange("duedate", oldDueDate, newDueDate);
	}
	
	public String getDueDate() {
		return dueDate;
	}
	
	public void setRepeat(String newRepeat) {
		String oldRepeat = repeat;
		repeat = newRepeat;
		firePropertyChange("repeat", oldRepeat, newRepeat);
	}
	
	public String getRepeat() {
		return repeat;
	}
	
	public void setTransition(String newTransition) {
		String oldTransition = transition;
		transition = newTransition;
		firePropertyChange("transition", oldTransition, newTransition);
	}
	
	public String getTransition() {
		return transition;
	}
	
	public boolean isNameMandatory() {
		return false;
	}

}
