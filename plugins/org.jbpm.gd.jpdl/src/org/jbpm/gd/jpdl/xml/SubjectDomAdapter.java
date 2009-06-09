package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Subject;

public class SubjectDomAdapter extends XmlAdapter {
	
	protected void initialize() {
		super.initialize();
		Subject subject = (Subject)getSemanticElement();
		if (subject != null) {
			setTextContent(subject.getSubject());
		}
	}

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Subject subject = (Subject)jpdlElement;
		subject.setSubject(getTextContent());
		subject.addPropertyChangeListener(this);
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("subject".equals(evt.getPropertyName())) {
			setTextContent((String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Subject subject = (Subject)getSemanticElement();
		if ("#text".equals(name)) {
			subject.setSubject(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		// a subject cannot have any child nodes
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// a subject cannot have any child nodes
	}
	
}
