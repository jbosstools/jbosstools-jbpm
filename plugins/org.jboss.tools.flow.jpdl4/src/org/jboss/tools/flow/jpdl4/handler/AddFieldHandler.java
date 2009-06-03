package org.jboss.tools.flow.jpdl4.handler;

import org.jboss.tools.flow.jpdl4.model.JavaTask;

public class AddFieldHandler extends AddChildHandler {
	
	@Override
	protected String getChildId() {
		return "org.jboss.tools.flow.jpdl4.field";
	}

	@Override
	protected String getChildType() {
		return JavaTask.FIELDS;
	}

}
