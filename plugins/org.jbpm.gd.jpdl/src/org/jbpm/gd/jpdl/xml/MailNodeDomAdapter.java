package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Description;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.MailNode;
import org.jbpm.gd.jpdl.model.Subject;
import org.jbpm.gd.jpdl.model.Text;
import org.jbpm.gd.jpdl.model.Timer;
import org.jbpm.gd.jpdl.model.Transition;

public class MailNodeDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"description", "subject", "text", "event", "exception-handler", "timer", "transition"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("description", "description");
			NODE_TYPES.put("subject", "subject");
			NODE_TYPES.put("text", "text");
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
		MailNode mailNode = (MailNode)getSemanticElement();
		if (mailNode != null) {
			setAttribute("async", mailNode.getAsync());
			setAttribute("name", mailNode.getName());
			setAttribute("template", mailNode.getTemplate());
			setAttribute("actors", mailNode.getActors());
			setAttribute("to", mailNode.getTo());
			addElement(mailNode.getDescription());
			addElement(mailNode.getSubject());
			addElement(mailNode.getText());
			addElements(mailNode.getEvents());
			addElements(mailNode.getExceptionHandlers());
			addElements(mailNode.getTimers());
			addElements(mailNode.getTransitions());			
		}
	}

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		MailNode mailNode = (MailNode)jpdlElement;
		mailNode.setAsync(getAttribute("async"));
		mailNode.setName(getAttribute("name"));
		mailNode.setTemplate(getAttribute("template"));
		mailNode.setActors(getAttribute("actors"));
		mailNode.setTo(getAttribute("to"));
		mailNode.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("eventAdd".equals(evt.getPropertyName())) {
			addElement((Event)evt.getNewValue());
		} else if ("eventRemove".equals(evt.getPropertyName())) {
			removeElement((Event)evt.getOldValue());
		} else if ("exceptionHandlerAdd".equals(evt.getPropertyName())) {
			addElement((ExceptionHandler)evt.getNewValue());
		} else if ("exceptionHandlerRemove".equals(evt.getPropertyName())) {
			removeElement((ExceptionHandler)evt.getOldValue());
		} else if ("timerAdd".equals(evt.getPropertyName())) {
			addElement((Timer)evt.getNewValue());
		} else if ("timerRemove".equals(evt.getPropertyName())) {
			removeElement((Timer)evt.getOldValue());
		} else if ("transitionAdd".equals(evt.getPropertyName())) {
			addElement((Transition)evt.getNewValue());
		} else if ("transitionRemove".equals(evt.getPropertyName())) {
			removeElement((Transition)evt.getOldValue());
		} else if ("description".equals(evt.getPropertyName())) {
			setElement("description", (SemanticElement)evt.getOldValue(), (Description)evt.getNewValue());
		} else if ("subject".equals(evt.getPropertyName())) {
			setElement("subject", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("text".equals(evt.getPropertyName())) {
			setElement("text", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("template".equals(evt.getPropertyName())) {
			setAttribute("template", (String)evt.getNewValue()); 
		} else if ("actors".equals(evt.getPropertyName())) {
			setAttribute("actors", (String)evt.getNewValue());
		} else if ("to".equals(evt.getPropertyName())) {
			setAttribute("to", (String)evt.getNewValue());
		} else if ("subject".equals(evt.getPropertyName())) {
			setAttribute("subject", (String)evt.getNewValue());
		} else if ("text".equals(evt.getPropertyName())) {
			setAttribute("text", (String)evt.getNewValue());
		} else if ("async".equals(evt.getPropertyName())) {
			setAttribute("async", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		MailNode mailNode = (MailNode)getSemanticElement();
		if ("name".equals(name)) {
			mailNode.setName(newValue);
		} else if ("async".equals(name)) {
			mailNode.setAsync(newValue);
		} else if ("to".equals(name)) {
			mailNode.setTo(newValue);
		} else if ("actors".equals(name)) {
			mailNode.setActors(newValue);
		} else if ("template".equals(name)) {
			mailNode.setTemplate(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		MailNode mailNode = (MailNode)getSemanticElement();
		if ("subject".equals(type)) {
			mailNode.setSubject((Subject)jpdlElement);
		} else if ("text".equals(type)) {
			mailNode.setText((Text)jpdlElement);
		} else if ("event".equals(type)) {
			mailNode.addEvent((Event)jpdlElement);
		} else if ("exception-handler".equals(type)) {
			mailNode.addExceptionHandler((ExceptionHandler)jpdlElement);
		} else if ("timer".equals(type)) {
			mailNode.addTimer((Timer)jpdlElement);
		} else if ("transition".equals(type)) {
			mailNode.addTransition((Transition)jpdlElement);
		} else if ("description".equals(getNodeType(type))) {
			mailNode.setDescription((Description)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		MailNode mailNode = (MailNode)getSemanticElement();
		if ("subject".equals(type)) {
			mailNode.setSubject(null);
		} else if ("text".equals(type)) {
			mailNode.setText(null);
		} else if ("event".equals(type)) {
			mailNode.removeEvent((Event)child.getSemanticElement());
		} else if ("exception-handler".equals(type)) {
			mailNode.removeExceptionHandler((ExceptionHandler)child.getSemanticElement());
		} else if ("timer".equals(type)) {
			mailNode.removeTimer((Timer)child.getSemanticElement());
		} else if ("transition".equals(type)) {
			mailNode.removeTransition((Transition)child.getSemanticElement());
		} else if ("description".equals(getNodeType(type))) {
			mailNode.setDescription(null);
		}
	}

}

