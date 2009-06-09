package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Description;

public class DescriptionDomAdapter extends XmlAdapter {
	
	protected void initialize() {
		super.initialize();
		Description description = (Description)getSemanticElement();
		if (description != null) {
			setTextContent(description.getDescription());
		}
	}

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Description description = (Description)jpdlElement;
		description.setDescription(getTextContent());
		description.addPropertyChangeListener(this);
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("description".equals(evt.getPropertyName())) {
			setTextContent((String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Description description = (Description)getSemanticElement();
		if ("#text".equals(name)) {
			description.setDescription(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		// a text cannot have any child nodes
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// a text cannot have any child nodes
	}
	
}
