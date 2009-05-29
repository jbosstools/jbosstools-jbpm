package org.jboss.tools.flow.jpdl4.handler;

import org.jboss.tools.flow.jpdl4.model.SubprocessTask;

public class AddOutputParameterHandler extends AddChildHandler {
	
	@Override
	protected String getChildId() {
		return "org.jboss.tools.flow.jpdl4.outputParameter";
	}

	@Override
	protected String getChildType() {
		return SubprocessTask.OUTPUT_PARAMETERS;
	}

}
