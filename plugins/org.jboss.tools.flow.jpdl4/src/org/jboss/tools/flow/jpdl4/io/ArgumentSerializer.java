/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Argument;

class ArgumentSerializer extends AbstractElementSerializer {
	
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
		String value = (String)wrapper.getPropertyValue(Argument.VALUE);
		if (value != null && !("".equals(value))) {
			buffer.append(value);
		}
		super.appendBody(buffer, wrapper, level);
	}
	
	protected void appendTrailingNodes(StringBuffer buffer, Wrapper wrapper, int level) {
		// There are no trailing nodes in an argument serialization
	}
	
}