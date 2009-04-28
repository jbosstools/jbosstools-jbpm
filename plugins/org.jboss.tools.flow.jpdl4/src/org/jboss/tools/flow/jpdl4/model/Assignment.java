package org.jboss.tools.flow.jpdl4.model;

public interface Assignment {

	public final String ASSIGNMENT_LABEL = "Assignment";
	
	public final String ASSIGNMENT_TYPE = "org.jboss.tools.flow.jpdl4.model.assignment.type";
	public final String ASSIGNMENT_EXPRESSION = "org.jboss.tools.flow.jpdl4.model.assignment.expression";
	public final String ASSIGNMENT_EXPRESSION_LANGUAGE = "org.jboss.tools.flow.jpdl4.model.assignment.expression.language";
	
	public final String ASSIGNEE = "assignee";
	public final String CANDIDATE_GROUPS = "candidate-groups";
	public final String SWIMLANE = "swimlane";
	public final String NONE = "none";
	
	public final String[] ASSIGNMENT_TYPES = { ASSIGNEE, CANDIDATE_GROUPS, SWIMLANE, NONE };
	
}
