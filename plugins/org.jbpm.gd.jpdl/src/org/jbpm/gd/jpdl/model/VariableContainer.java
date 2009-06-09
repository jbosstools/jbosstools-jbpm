package org.jbpm.gd.jpdl.model;

public interface VariableContainer {

	void addVariable(Variable variable);
	void removeVariable(Variable variable);
	Variable[] getVariables();

}
