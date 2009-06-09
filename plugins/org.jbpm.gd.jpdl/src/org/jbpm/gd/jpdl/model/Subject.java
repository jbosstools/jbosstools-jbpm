package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.AbstractSemanticElement;

public class Subject extends AbstractSemanticElement {
	
	private String subject;
	
	public void setSubject(String newSubject) {
		String oldSubject = subject;
		subject = newSubject;
		firePropertyChange("subject", oldSubject, newSubject);
	}
	
	public String getSubject() {
		return subject;
	}

}
