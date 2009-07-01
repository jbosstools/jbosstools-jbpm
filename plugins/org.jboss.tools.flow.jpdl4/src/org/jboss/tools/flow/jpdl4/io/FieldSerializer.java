/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Field;

class FieldSerializer extends AbstractElementSerializer {
	
	protected List<String> getAttributesToSave() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("name");
		return result;
	}
	
	protected String getPropertyName(String attributeName) {
		if ("name".equals(attributeName)) {
			return Field.NAME;
		}
		return super.getPropertyName(attributeName);
	}
	
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
		String value = (String)wrapper.getPropertyValue(Field.VALUE);
		if (value != null && !("".equals(value))) {
			buffer.append(value);
		}
		super.appendBody(buffer, wrapper, level);
	}
	
	protected void appendTrailingNodes(StringBuffer buffer, Wrapper wrapper, int level) {
		// There are no trailing nodes in an argument serialization
	}
	
}