package org.jbpm.gd.pf.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.pf.model.PageFlowDefinition;
import org.jbpm.gd.pf.model.NodeElement;
import org.jbpm.gd.pf.model.StartPage;

public class PageFlowDefinitionDomAdapter extends XmlAdapter {
	
	private static HashMap NODE_TYPES = null;	
	private static String[] CHILD_ELEMENTS = {"start-page", "decision", "page", "process-state"};

	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	public Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("start-page", "start-page");
			NODE_TYPES.put("decision", "node-element");
			NODE_TYPES.put("page", "node-element");
			NODE_TYPES.put("process-state", "node-element");
		}
		return NODE_TYPES;
	}
		
	protected void initialize() {
		super.initialize();
		PageFlowDefinition pageFlowDefinition = (PageFlowDefinition)getSemanticElement();
		if (pageFlowDefinition != null) {
			setAttribute("name", pageFlowDefinition.getName());
			addElement(pageFlowDefinition.getStartPage());
			addElements(pageFlowDefinition.getNodeElements());
		}
	}

	public void initialize(SemanticElement semanticElement) {
		super.initialize(semanticElement);
		PageFlowDefinition pageFlowDefinition = (PageFlowDefinition)semanticElement;
		pageFlowDefinition.setName(getAttribute("name"));
		pageFlowDefinition.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("startPageAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("startPageRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		} else if ("nodeElementAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("nodeElementRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		} else  if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		PageFlowDefinition pageFlowDefinition = (PageFlowDefinition)getSemanticElement();
		if ("name".equals(name)) {
			pageFlowDefinition.setName(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement semanticElement = createSemanticElementFor(child);
		if (semanticElement == null) return;
		child.initialize(semanticElement);
		PageFlowDefinition pageFlowDefinition = (PageFlowDefinition)getSemanticElement();
		if ("start-page".equals(type)) {
			pageFlowDefinition.addStartPage((StartPage)semanticElement);
		} else if ("decision".equals(type)) {
			pageFlowDefinition.addNodeElement((NodeElement)semanticElement);
		} else if ("page".equals(type)) {
			pageFlowDefinition.addNodeElement((NodeElement)semanticElement);
		} else if ("process-state".equals(type)) {
			pageFlowDefinition.addNodeElement((NodeElement)semanticElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		PageFlowDefinition pageFlowDefinition = (PageFlowDefinition)getSemanticElement();
		if ("start-page".equals(type)) {
			pageFlowDefinition.removeStartPage((StartPage)child.getSemanticElement());
		} else if ("decision".equals(type)) {
			pageFlowDefinition.removeNodeElement((NodeElement)child.getSemanticElement());
		} else if ("page".equals(type)) {
			pageFlowDefinition.removeNodeElement((NodeElement)child.getSemanticElement());
		} else if ("process-state".equals(type)) {
			pageFlowDefinition.removeNodeElement((NodeElement)child.getSemanticElement());
		}
	}


}
