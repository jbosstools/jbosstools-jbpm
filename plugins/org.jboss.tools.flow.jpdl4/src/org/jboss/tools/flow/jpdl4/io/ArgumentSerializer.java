/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Argument;

class ArgumentSerializer extends AbstractElementSerializer {
	protected List<String> getAttributesToSave() {
		ArrayList<String> result = new ArrayList<String>();
		return result;
	}
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
		String value = (String)wrapper.getPropertyValue(Argument.VALUE);
		if (value != null && !("".equals(value))) {
			buffer.append("\n");
			appendPadding(buffer, level + 1);
			buffer.append("<string value=\"" + value + "\"/>");
		}
		super.appendBody(buffer, wrapper, level);
	}
}