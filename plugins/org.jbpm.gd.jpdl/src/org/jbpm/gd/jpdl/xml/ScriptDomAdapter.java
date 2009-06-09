package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Script;

public class ScriptDomAdapter extends XmlAdapter {
	
	protected void initialize() {
		super.initialize();
		Script script = (Script)getSemanticElement();
		if (script != null) {
			setTextContent(script.getScript());
			setAttribute("name", script.getName());
			setAttribute("accept-propagated-events", script.getAcceptPropagatedEvents());
		}
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Script script = (Script)jpdlElement;
		script.setScript(getTextContent());
		script.setName(getAttribute("name"));
		script.setAcceptPropagatedEvents(getAttribute("accept-propagated-events"));
		script.addPropertyChangeListener(this);
	}
	
	protected String getDefaultValue(String attributeName) {
		if ("accept-propagated-events".equals(attributeName)) {
			return "true";
		} else {
			return super.getDefaultValue(attributeName);
		}
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("script".equals(evt.getPropertyName())) {
			setTextContent((String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		} else if ("acceptPropagatedEvents".equals(evt.getPropertyName())) {
			setAttribute("accept-propagated-events", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Script script = (Script)getSemanticElement();
		if ("#text".equals(name)) {
			script.setScript(newValue);
		} else if ("name".equals(name)) {
			script.setName(newValue);
		} else if ("accept-propagated-events".equals(name)) {
			script.setAcceptPropagatedEvents(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		// a script cannot have any child nodes
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// a script cannot have any child nodes
	}
	
}
