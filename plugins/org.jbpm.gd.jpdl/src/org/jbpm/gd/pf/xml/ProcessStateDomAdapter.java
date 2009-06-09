package org.jbpm.gd.pf.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.pf.model.ProcessState;
import org.jbpm.gd.pf.model.Transition;

public class ProcessStateDomAdapter extends XmlAdapter {
	
	private static HashMap NODE_TYPES = null;	
	private static String[] CHILD_ELEMENTS = {"transition"};

	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	public Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("transition", "transition");
		}
		return NODE_TYPES;
	}
		
	protected void initialize() {
		super.initialize();
		ProcessState processState = (ProcessState)getSemanticElement();
		if (processState != null) {
			setAttribute("name", processState.getName());
			setAttribute("view-id", processState.getViewId());
			addElements(processState.getTransitions());
		}
	}

	public void initialize(SemanticElement semanticElement) {
		super.initialize(semanticElement);
		ProcessState processState = (ProcessState)semanticElement;
		processState.setName(getAttribute("name"));
		processState.setViewId(getAttribute("view-id"));
		processState.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("transitionAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("transitionRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		} else  if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		} else if ("viewId".equals(evt.getPropertyName())) {
			setAttribute("view-id", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		ProcessState processState = (ProcessState)getSemanticElement();
		if ("name".equals(name)) {
			processState.setName(newValue);
		} else if ("view-id".equals(name)) {
			processState.setViewId(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement semanticElement = createSemanticElementFor(child);
		if (semanticElement == null) return;
		child.initialize(semanticElement);
		ProcessState processState = (ProcessState)getSemanticElement();
		if ("transition".equals(type)) {
			processState.addTransition((Transition)semanticElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		ProcessState processState = (ProcessState)getSemanticElement();
		if ("transition".equals(type)) {
			processState.removeTransition((Transition)child.getSemanticElement());
		}
	}


}
