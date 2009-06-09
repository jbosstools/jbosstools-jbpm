package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Assignment;
import org.jbpm.gd.jpdl.model.Swimlane;

public class SwimlaneDomAdapter extends XmlAdapter {
	
	private static HashMap NODE_TYPES = null;
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("assignment", "assignment");
		}
		return NODE_TYPES;
	}
	
	protected void initialize() {
		super.initialize();
		Swimlane swimlane  = (Swimlane)getSemanticElement();
		if (swimlane != null) {
			setAttribute("name", swimlane.getName());
			addElement(swimlane.getAssignment());
		}
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Swimlane swimlane = (Swimlane)jpdlElement;
		swimlane.setName(getAttribute("name"));
		swimlane.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		} else if ("assignment".equals(evt.getPropertyName())) {
			setElement("assignment", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Swimlane swimlane = (Swimlane)getSemanticElement();
		if ("name".equals(name)) {
			swimlane.setName(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		Swimlane swimlane = (Swimlane)getSemanticElement();
		if ("assignment".equals(type)) {
			swimlane.setAssignment((Assignment)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		Swimlane swimlane = (Swimlane)getSemanticElement();
		if ("assignment".equals(type)) {
			swimlane.setAssignment(null);
		}
	}
	
}
