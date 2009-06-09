package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.List;

public class ProcessState extends AbstractAsyncableTimerNode implements VariableContainer {

	private SubProcess subProcess;
	private List variables = new ArrayList();
	
	public void setSubProcess(SubProcess newSubProcess) {
		SubProcess oldSubProcess = subProcess;
		subProcess = newSubProcess;
		firePropertyChange("subProcess", oldSubProcess, newSubProcess);
	}
	
	public SubProcess getSubProcess() {
		if (subProcess == null) {
			subProcess = (SubProcess)getFactory().createById("org.jbpm.gd.jpdl.subprocess");
		}
		return subProcess;
	}
	
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
