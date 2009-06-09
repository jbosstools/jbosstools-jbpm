package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.SemanticElement;

public interface Assignable extends SemanticElement {
	
	void setAssignment(Assignment assignment);
	Assignment getAssignment();

}
