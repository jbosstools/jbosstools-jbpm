package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.CreateTimer;
import org.jbpm.gd.jpdl.model.Script;


public class CreateTimerDomAdapter extends XmlAdapter {

	private static HashMap NODE_TYPES = null;
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("action", "action-element");
			NODE_TYPES.put("script", "action-element");
		}
		return NODE_TYPES;
	}
		
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		CreateTimer createTimer = (CreateTimer)jpdlElement;
		createTimer.setDueDate(getAttribute("duedate"));
		createTimer.setName(getAttribute("name"));
		createTimer.setRepeat(getAttribute("repeat"));
		createTimer.setTransition(getAttribute("transition"));
		createTimer.addPropertyChangeListener(this);
	}

	protected void initialize() {
		super.initialize();
		CreateTimer createTimer = (CreateTimer)getSemanticElement();
		if (createTimer != null) {
			setAttribute("duedate", createTimer.getDueDate());
			setAttribute("name", createTimer.getName());
			setAttribute("repeat", createTimer.getRepeat());
			setAttribute("transition", createTimer.getTransition());
			addElement(createTimer.getAction());
			addElement(createTimer.getScript());
		}
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("action".equals(evt.getPropertyName())) {
			setElement("action", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("script".equals(evt.getPropertyName())) {
			setElement("script", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("duedate".equals(evt.getPropertyName())) {
			setAttribute("duedate", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		} else if ("repeat".equals(evt.getPropertyName())) {
			setAttribute("repeat", (String)evt.getNewValue());
		} else if ("transition".equals(evt.getPropertyName())) {
			setAttribute("transition", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		CreateTimer createTimer = (CreateTimer)getSemanticElement();
		if ("name".equals(name)) {
			createTimer.setName(newValue);
		} else if ("duedate".equals(name)) {
			createTimer.setDueDate(newValue);
		} else if ("repeat".equals(name)) {
			createTimer.setRepeat(newValue);
		} else if ("transition".equals(name)) {
			createTimer.setTransition(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		CreateTimer createTimer = (CreateTimer)getSemanticElement();
		if ("action".equals(type)) {
			createTimer.setAction((Action)jpdlElement);
		} else if ("script".equals(type)) {
			createTimer.setScript((Script)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		CreateTimer createTimer = (CreateTimer)getSemanticElement();
		if ("action".equals(type)) {
			createTimer.setAction(null);
		} else if ("script".equals(type)) {
			createTimer.setScript(null);
		}
	}
	
}
