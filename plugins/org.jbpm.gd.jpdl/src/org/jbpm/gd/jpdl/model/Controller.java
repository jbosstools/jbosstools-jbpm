package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.List;

public class Controller extends Delegation implements VariableContainer {
	
	private List variables = new ArrayList();
	
	public void addVariable(Variable variable) {
		variables.add(variable);
		firePropertyChange("variableAdd", null, variable);
	}
	
	public void removeVariable(Variable variable) {
		variables.remove(variable);
		firePropertyChange("variableRemove", variable, null);
	}
	
	public Variable[] getVariables() {
		return (Variable[])variables.toArray(new Variable[variables.size()]);
	}
	

}
