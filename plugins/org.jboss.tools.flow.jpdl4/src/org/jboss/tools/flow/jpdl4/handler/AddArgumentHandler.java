package org.jboss.tools.flow.jpdl4.handler;

import org.jboss.tools.flow.jpdl4.model.JavaTask;

public class AddArgumentHandler extends AddChildHandler {
	
	@Override
	protected String getChildId() {
		return "org.jboss.tools.flow.jpdl4.argument";
	}

	@Override
	protected String getChildType() {
		return JavaTask.ARGS;
	}

}
