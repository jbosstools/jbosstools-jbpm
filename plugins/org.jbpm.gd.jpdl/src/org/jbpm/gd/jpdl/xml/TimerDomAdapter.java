package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.Script;
import org.jbpm.gd.jpdl.model.Timer;

public class TimerDomAdapter extends XmlAdapter {
	
	private static HashMap NODE_TYPES = null;
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("action", "action-element");
			NODE_TYPES.put("script", "action-element");
		}
		return NODE_TYPES;
	}
	
	protected void initialize() {
		super.initialize();
		Timer timer = (Timer)getSemanticElement();
		if (timer != null) {
			setAttribute("duedate", timer.getDueDate());
			setAttribute("name", timer.getName());
			setAttribute("repeat", timer.getRepeat());
			setAttribute("transition", timer.getTransition());
			addElement(timer.getAction());
			addElement(timer.getScript());
		}
	}
		
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Timer timer = (Timer)jpdlElement;
		timer.setDueDate(getAttribute("duedate"));
		timer.setName(getAttribute("name"));
		timer.setRepeat(getAttribute("repeat"));
		timer.setTransition(getAttribute("transition"));
		timer.addPropertyChangeListener(this);
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
		Timer timer = (Timer)getSemanticElement();
		if ("name".equals(name)) {
			timer.setName(newValue);
		} else if ("duedate".equals(name)) {
			timer.setDueDate(newValue);
		} else if ("repeat".equals(name)) {
			timer.setRepeat(newValue);
		} else if ("transition".equals(name)) {
			timer.setTransition(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		Timer timer = (Timer)getSemanticElement();
		if ("action".equals(type)) {
			timer.setAction((Action)jpdlElement);
		} else if ("script".equals(type)) {
			timer.setScript((Script)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		Timer timer = (Timer)getSemanticElement();
		if ("action".equals(type)) {
			timer.setAction(null);
		} else if ("script".equals(type)) {
			timer.setScript(null);
		}
	}
	
}
