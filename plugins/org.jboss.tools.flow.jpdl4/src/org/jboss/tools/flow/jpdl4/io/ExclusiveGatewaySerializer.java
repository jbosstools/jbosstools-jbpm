/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.ExclusiveGateway;

class ExclusiveGatewaySerializer extends ProcessNodeSerializer {
	
	protected List<String> getAttributesToSave() {
		List<String> result = super.getAttributesToSave();
		result.add("expr");
		result.add("lang");
		return result;
	}
	
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
		ExclusiveGateway exclusiveGateway = (ExclusiveGateway)wrapper.getElement();
		String handler = exclusiveGateway.getHandler();
		if (handler != null && !"".equals(handler)) {
			appendHandler(buffer, wrapper, handler, level);
		} 
		super.appendBody(buffer, wrapper, level);
	}
	
	private void appendHandler(StringBuffer buffer, Wrapper wrapper, String handler, int level) {
		appendUnknownNodes("beforeHandlerNodes", buffer, wrapper, level);
		buffer.append("<handler class=\"").append(handler).append("\" />");
	}
	
}