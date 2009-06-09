package org.jbpm.gd.jpdl.util;

import org.jbpm.gd.jpdl.model.Assignable;
import org.jbpm.gd.jpdl.model.Task;

public class SwimlaneAssignmentMatcher implements AssignmentTypeMatcher {

	public boolean matches(Assignable assignable) {
		return (assignable instanceof Task && ((Task)assignable).getSwimlane() != null);
	}

}
