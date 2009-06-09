package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.CancelTimer;
import org.jbpm.gd.jpdl.model.CreateTimer;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.MailAction;
import org.jbpm.gd.jpdl.model.Script;

public class EventDomAdapter extends XmlAdapter {
	
	private static HashMap NODE_TYPES = null;
	private static String[] CHILD_ELEMENTS = { "action-element" };	
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("action", "action-element");
			NODE_TYPES.put("script", "action-element");
			NODE_TYPES.put("create-timer", "action-element");
			NODE_TYPES.put("cancel-timer", "action-element");
			NODE_TYPES.put("mail", "action-element");
		}
		return NODE_TYPES;
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Event event = (Event)jpdlElement;
		event.setType(getAttribute("type"));
		event.addPropertyChangeListener(this);
	}

	protected void initialize() {
		super.initialize();
		Event event = (Event)getSemanticElement();
		if (event != null) {
			setAttribute("type", event.getType());
			addElements(event.getActionElements());
		}
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("actionElementAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("actionElementRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		} else if ("actionAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("actionRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		} else if ("scriptAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("scriptRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		} else if ("createTimerAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("createTimerRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		} else if ("cancelTimerAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("cancelTimerRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		} else if ("mailAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("mailRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		} else if ("type".equals(evt.getPropertyName())) {
			setAttribute("type", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Event event = (Event)getSemanticElement();
		if ("type".equals(name)) {
			event.setType(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		Event event = (Event)getSemanticElement();
		if ("action".equals(type)) {
			event.addActionElement((Action)jpdlElement);
		} else if ("script".equals(type)) {
			event.addActionElement((Script)jpdlElement);
		} else if ("create-timer".equals(type)) {
			event.addActionElement((CreateTimer)jpdlElement);
		} else if ("cancel-timer".equals(type)) {
			event.addActionElement((CancelTimer)jpdlElement);
		} else if ("mail".equals(type)) {
			event.addActionElement((MailAction)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		Event event = (Event)getSemanticElement();
		if ("action".equals(type)) {
			event.removeActionElement((Action)child.getSemanticElement());
		} else if ("script".equals(type)) {
			event.removeActionElement((Script)child.getSemanticElement());
		} else if ("create-timer".equals(type)) {
			event.removeActionElement((CreateTimer)child.getSemanticElement());
		} else if ("cancel-timer".equals(type)) {
			event.removeActionElement((CancelTimer)child.getSemanticElement());
		} else if ("mail".equals(type)) {
			event.removeActionElement((MailAction)child.getSemanticElement());
		}
	}

}
