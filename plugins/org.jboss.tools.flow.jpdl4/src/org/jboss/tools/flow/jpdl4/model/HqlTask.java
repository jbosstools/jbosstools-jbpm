package org.jboss.tools.flow.jpdl4.model;

import java.util.ArrayList;
import java.util.List;


public class HqlTask extends Task {
	
	public static final String VAR = "org.jboss.tools.flow.jpdl4.model.hqlTask.var";
	public static final String UNIQUE = "org.jboss.tools.flow.jpdl4.model.hqlTask.unique";
	public static final String QUERY = "org.jboss.tools.flow.jpdl4.model.hqlTask.query";
	public static final String ARGS = "org.jboss.tools.flow.jpdl4.model.hqlTask.args";

	private String variableName;
	private String unique;
	private String query;
	private List<Field> fields = new ArrayList<Field>();
	
	protected boolean isPropagationExclusive() {
		return true;
	}
	
}
