package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.jpdl4.model.ForeachParallelGateway;


public class ForEachParallelGatewaySerializer extends ProcessNodeSerializer {

	protected List<String> getAttributesToSave() {
		List<String> result = super.getAttributesToSave();
		result.add("var");
		result.add("in");
		return result;
	}
	
	protected String getPropertyName(String attributeName) {
		if ("var".equals(attributeName)) {
			return ForeachParallelGateway.VAR;
		} else if ("in".equals(attributeName)) {
			return ForeachParallelGateway.IN;
		}
		return super.getPropertyName(attributeName);
	}

}
