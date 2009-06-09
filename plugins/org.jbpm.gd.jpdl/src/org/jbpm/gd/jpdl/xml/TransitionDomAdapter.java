package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.CancelTimer;
import org.jbpm.gd.jpdl.model.Condition;
import org.jbpm.gd.jpdl.model.CreateTimer;
import org.jbpm.gd.jpdl.model.Description;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.MailAction;
import org.jbpm.gd.jpdl.model.Script;
import org.jbpm.gd.jpdl.model.Transition;

public class TransitionDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"description", "condition", "action-element", "exception-handler"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("description", "description");
			NODE_TYPES.put("condition", "condition");
			NODE_TYPES.put("action", "action-element");
			NODE_TYPES.put("script", "action-element");
			NODE_TYPES.put("create-timer", "action-element");
			NODE_TYPES.put("cancel-timer", "action-element");
			NODE_TYPES.put("mail", "action-element");
			NODE_TYPES.put("exception-handler", "exception-handler");
		}
		return NODE_TYPES;
	}
	
	protected void initialize() {
		super.initialize();
		Transition transition = (Transition)getSemanticElement();
		if (transition != null) {
			setAttribute("to", transition.getTo());
			setAttribute("name", transition.getName());
			addElement(transition.getDescription());
			addElements(transition.getActionElements());
			addElements(transition.getExceptionHandlers());
		}
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Transition transition = (Transition)jpdlElement;
		transition.setTo(getAttribute("to"));
		transition.setName(getAttribute("name"));
		transition.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("condition".equals(evt.getPropertyName())) {
			setElement("condition", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("description".equals(evt.getPropertyName())) {
			setElement("description", (SemanticElement)evt.getOldValue(), (Description)evt.getNewValue());
		} else if ("actionElementAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("actionElementRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		} else if ("exceptionHandlerAdd".equals(evt.getPropertyName())) {
			addElement((ExceptionHandler)evt.getNewValue());
		} else if ("exceptionHandlerRemove".equals(evt.getPropertyName())) {
			removeElement((ExceptionHandler)evt.getOldValue());
		} else if ("to".equals(evt.getPropertyName())) {
			setAttribute("to", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Transition transition = (Transition)getSemanticElement();
		if ("to".equals(name)) {
			transition.setTo(newValue);
		} else if ("name".equals(name)) {
			transition.setName(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		Transition transition = (Transition)getSemanticElement();
		if ("condition".equals(type)) {
			transition.setCondition((Condition)jpdlElement);
		} else if ("description".equals(getNodeType(type))) {
			transition.setDescription((Description)jpdlElement);
		} else if ("action".equals(type)) {
			transition.addActionElement((Action)jpdlElement);
		} else if ("script".equals(type)) {
			transition.addActionElement((Script)jpdlElement);
		} else if ("create-timer".equals(type)) {
			transition.addActionElement((CreateTimer)jpdlElement);
		} else if ("cancel-timer".equals(type)) {
			transition.addActionElement((CancelTimer)jpdlElement);
		} else if ("mail".equals(type)) {
			transition.addActionElement((MailAction)jpdlElement);
		} else if ("exception-handler".equals(type)) {
			transition.addExceptionHandler((ExceptionHandler)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		Transition transition = (Transition)getSemanticElement();
		if ("condition".equals(type)) {
			transition.setCondition(null);
		} else if ("description".equals(getNodeType(type))) {
			transition.setDescription(null);
		} else if ("action".equals(type)) {
			transition.removeActionElement((Action)child.getSemanticElement());
		} else if ("script".equals(type)) {
			transition.removeActionElement((Script)child.getSemanticElement());
		} else if ("create-timer".equals(type)) {
			transition.removeActionElement((CreateTimer)child.getSemanticElement());
		} else if ("cancel-timer".equals(type)) {
			transition.removeActionElement((CancelTimer)child.getSemanticElement());
		} else if ("mail".equals(type)) {
			transition.removeActionElement((MailAction)child.getSemanticElement());
		} else if ("exception-handler".equals(type)) {
			transition.removeExceptionHandler((ExceptionHandler)child.getSemanticElement());
		}
	}

}
