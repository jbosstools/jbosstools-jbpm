package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Variable;


public class VariableDomAdapter extends XmlAdapter {
	
	protected void initialize() {
		super.initialize();
		Variable variable = (Variable)getSemanticElement();
		if (variable != null) {
			setTextContent(variable.getVariable());
			setAttribute("access", variable.getAccess());
			setAttribute("mapped-name", variable.getMappedName());
			setAttribute("name", variable.getName());
		}
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Variable variable = (Variable)jpdlElement;
		variable.setVariable(getTextContent());
		variable.setAccess(getAttribute("access"));
		variable.setMappedName(getAttribute("mapped-name"));
		variable.setName(getAttribute("name"));
		variable.addPropertyChangeListener(this);
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("variable".equals(evt.getPropertyName())) {
			setTextContent((String)evt.getNewValue());
		} else if ("access".equals(evt.getPropertyName())) {
			setAttribute("access", (String)evt.getNewValue());
		} else if ("mappedName".equals(evt.getPropertyName())) {
			setAttribute("mapped-name", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}	
	
	protected void doModelUpdate(String name, String newValue) {
		Variable variable = (Variable)getSemanticElement();
		if ("#text".equals(name)) {
			variable.setVariable(newValue);
		} else if ("access".equals(name)) {
			variable.setAccess(newValue);
		} else if ("mappedName".equals(name)) {
			variable.setMappedName(newValue);
		} else if ("name".equals(name)) {
			variable.setName(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		// a variable cannot have any child nodes
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// a variable cannot have any child nodes
	}
	
}
