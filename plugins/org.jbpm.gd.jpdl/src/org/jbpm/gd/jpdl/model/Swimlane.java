package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.AbstractNamedElement;

public class Swimlane extends AbstractNamedElement implements Assignable {
	
	private Assignment assignment;
	
	public void setAssignment(Assignment newAssignment) {
		Assignment oldAssignment = assignment;
		assignment = newAssignment;
		firePropertyChange("assignment", oldAssignment, newAssignment);
	}
	
	public Assignment getAssignment() {
		return assignment;
	}
	
	public void initializeName(ProcessDefinition processDefinition) {
		int runner = 1;
		String prefix = "swimlane";
		while (true) {
			String candidate = prefix + runner;
			if (processDefinition.getSwimlaneByName(candidate) == null) {
				setName(candidate);
				return;
			}
			runner ++;
		}
	}
	
}
