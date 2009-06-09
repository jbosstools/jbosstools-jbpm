package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.AbstractNamedElement;

public class Variable extends AbstractNamedElement {
	
	private String variable;
	private String access;
	private String mappedName;
	
	public void setVariable(String newVariable) {
		String oldVariable = variable;
		variable = newVariable;
		firePropertyChange("variable", oldVariable, newVariable);
	}
	
	public String getVariable() {
		return variable;
	}
	
	public void setAccess(String newAccess) {
		String oldAccess = access;
		access = newAccess;
		firePropertyChange("access", oldAccess, newAccess);
	}
	
	public String getAccess() {
		return access;
	}
	
	public void setMappedName(String newMappedName) {
		String oldMappedName = mappedName;
		mappedName = newMappedName;
		firePropertyChange("mappedName", oldMappedName, newMappedName);
	}
	
	public String getMappedName() {
		return mappedName;
	}
	
	public boolean isReadable() {
		return access != null && access.indexOf("read") != -1;
	}

	public boolean isWritable() {
		return access != null && access.indexOf("write") != -1;
	}

	public boolean isRequired() {
		return access != null && access.indexOf("required") != -1;
	}
	
}
