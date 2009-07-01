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
			buffer.append("\n");
			appendPadding(buffer, level + 1);
			buffer.append("<handler class=\"" + handler + "\" />");
		} 
		super.appendBody(buffer, wrapper, level);
	}
}