package org.jboss.tools.flow.jpdl4.handler;

import org.jboss.tools.flow.jpdl4.model.SubprocessTask;

public class AddInputParameterHandler extends AddChildHandler {
	
	@Override
	protected String getChildId() {
		return "org.jboss.tools.flow.jpdl4.inputParameter";
	}

	@Override
	protected String getChildType() {
		return SubprocessTask.INPUT_PARAMETERS;
	}

}
