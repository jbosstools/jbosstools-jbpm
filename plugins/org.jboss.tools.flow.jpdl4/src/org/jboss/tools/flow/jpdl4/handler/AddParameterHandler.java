package org.jboss.tools.flow.jpdl4.handler;

import org.jboss.tools.flow.jpdl4.model.HqlTask;

public class AddParameterHandler extends AddChildHandler {
	
	@Override
	protected String getChildId() {
		return "org.jboss.tools.flow.jpdl4.wireObject";
	}

	@Override
	protected String getChildType() {
		return HqlTask.PARAMETERS;
	}

}
