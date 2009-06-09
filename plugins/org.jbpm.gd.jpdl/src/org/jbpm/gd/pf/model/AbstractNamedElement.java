package org.jbpm.gd.pf.model;

import org.jbpm.gd.common.model.AbstractSemanticElement;
import org.jbpm.gd.common.model.NamedElement;

public class AbstractNamedElement extends AbstractSemanticElement implements NamedElement {
	
	private String name;
	
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		firePropertyChange("name", oldName, newName);
	}
	
	public String getName() {
		return name;
	}

	public boolean isNameMandatory() {
		return true;
	}
	
}
