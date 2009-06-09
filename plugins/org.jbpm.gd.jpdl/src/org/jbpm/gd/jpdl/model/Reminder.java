package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.AbstractSemanticElement;

public class Reminder extends AbstractSemanticElement {
	
	private String dueDate = "";
	private String repeat;
		
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
	
}
