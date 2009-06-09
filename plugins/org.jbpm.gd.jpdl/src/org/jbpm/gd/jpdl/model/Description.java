package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.AbstractSemanticElement;

public class Description extends AbstractSemanticElement {
	
	private String description;
	
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		firePropertyChange("description", oldDescription, newDescription);
	}
	
	public String getDescription() {
		return description;
	}

}
