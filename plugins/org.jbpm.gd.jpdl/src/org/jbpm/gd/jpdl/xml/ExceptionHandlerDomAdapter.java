package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.ActionElement;
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.Script;

public class ExceptionHandlerDomAdapter extends XmlAdapter {
	
	private static final String[] CHILD_ELEMENTS = {"action-element"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
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
		ExceptionHandler exceptionHandler = (ExceptionHandler)jpdlElement;
		exceptionHandler.setExceptionClass(getAttribute("exception-class"));
		exceptionHandler.addPropertyChangeListener(this);
	}
	
	protected void initialize() {
		super.initialize();
		ExceptionHandler exceptionHandler = (ExceptionHandler)getSemanticElement();
		if (exceptionHandler != null) {
			setAttribute("exception-class", exceptionHandler.getExceptionClass());
			addElements(exceptionHandler.getActionElements());
		}
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("actionElementAdd".equals(evt.getPropertyName())) {
			addElement((ActionElement)evt.getNewValue());
		} else if ("actionElementRemove".equals(evt.getPropertyName())) {
			removeElement((ActionElement)evt.getOldValue());
		} else if ("exceptionClass".equals(evt.getPropertyName())) {
			setAttribute("exception-class", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		ExceptionHandler exceptionHandler = (ExceptionHandler)getSemanticElement();
		if ("exception-class".equals(name)) {
			exceptionHandler.setExceptionClass(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		ExceptionHandler exceptionHandler = (ExceptionHandler)getSemanticElement();
		if ("action".equals(type)) {
			exceptionHandler.addAction((Action)jpdlElement);
		} else if ("script".equals(type)) {
			exceptionHandler.addScript((Script)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		ExceptionHandler exceptionHandler = (ExceptionHandler)getSemanticElement();
		if ("action".equals(type)) {
			exceptionHandler.removeAction((Action)child.getSemanticElement());
		} else if ("script".equals(type)) {
			exceptionHandler.removeScript((Script)child.getSemanticElement());
		}
	}
	
}
