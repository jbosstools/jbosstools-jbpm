package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.AbstractNamedElement;

public class SubProcess extends AbstractNamedElement {
	
	private String version;
	
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		firePropertyChange("version", oldVersion, newVersion);
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getName() {
		if (super.getName() == null) {
			setName("");
		}
		return super.getName();
	}

}
