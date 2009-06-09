package org.jbpm.gd.pf.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.pf.model.StartPage;
import org.jbpm.gd.pf.model.Transition;

public class StartPageDomAdapter extends XmlAdapter {
	
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
		StartPage startPage = (StartPage)getSemanticElement();
		if (startPage != null) {
			setAttribute("name", startPage.getName());
			setAttribute("view-id", startPage.getViewId());
			addElements(startPage.getTransitions());
		}
	}

	public void initialize(SemanticElement semanticElement) {
		super.initialize(semanticElement);
		StartPage startPage = (StartPage)semanticElement;
		startPage.setName(getAttribute("name"));
		startPage.setViewId(getAttribute("view-id"));
		startPage.addPropertyChangeListener(this);
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
		StartPage startPage = (StartPage)getSemanticElement();
		if ("name".equals(name)) {
			startPage.setName(newValue);
		} else if ("view-id".equals(name)) {
			startPage.setViewId(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement semanticElement = createSemanticElementFor(child);
		if (semanticElement == null) return;
		child.initialize(semanticElement);
		StartPage startPage = (StartPage)getSemanticElement();
		if ("transition".equals(type)) {
			startPage.addTransition((Transition)semanticElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		StartPage startPage = (StartPage)getSemanticElement();
		if ("transition".equals(type)) {
			startPage.removeTransition((Transition)child.getSemanticElement());
		}
	}


}
