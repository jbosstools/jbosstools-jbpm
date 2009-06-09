package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Reminder;

public class ReminderDomAdapter extends XmlAdapter {
	
	private static HashMap NODE_TYPES = null;
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
		}
		return NODE_TYPES;
	}
		
	protected void initialize() {
		super.initialize();
		Reminder reminder = (Reminder)getSemanticElement();
		if (reminder != null) {
			setAttribute("duedate", reminder.getDueDate());
			setAttribute("repeat", reminder.getRepeat());
		}
	}
		
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Reminder reminder = (Reminder)jpdlElement;
		reminder.setDueDate(getAttribute("duedate"));
		reminder.setRepeat(getAttribute("repeat"));
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("duedate".equals(evt.getPropertyName())) {
			setAttribute("duedate", (String)evt.getNewValue());
		} else if ("repeat".equals(evt.getPropertyName())) {
			setAttribute("repeat", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Reminder reminder = (Reminder)getSemanticElement();
		if ("duedate".equals(name)) {
			reminder.setDueDate(newValue);
		} else if ("repeat".equals(name)) {
			reminder.setRepeat(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
	}
	
	protected void doModelRemove(XmlAdapter child) {
	}
	
}
