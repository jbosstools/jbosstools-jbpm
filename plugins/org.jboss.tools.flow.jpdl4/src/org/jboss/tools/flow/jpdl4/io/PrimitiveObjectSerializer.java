/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Argument;

class PrimitiveObjectSerializer extends AbstractElementSerializer {
	
	public void appendToBuffer(StringBuffer buffer, Wrapper wrapper, int level) {
		appendUnknownNodes("leadingNodes", buffer, wrapper, level);
		String value = (String)wrapper.getPropertyValue(Argument.VALUE);
		if (value != null && !("".equals(value))) {
			buffer.append(value);
		}
	}
		
}