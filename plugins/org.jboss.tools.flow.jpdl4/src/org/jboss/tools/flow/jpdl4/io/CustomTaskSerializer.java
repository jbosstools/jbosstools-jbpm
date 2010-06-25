/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.jpdl4.model.CustomTask;

class CustomTaskSerializer extends ProcessNodeSerializer {
	protected List<String> getAttributesToSave() {
		List<String> result = super.getAttributesToSave();
		result.add("class");
		return result;
	}
	protected String getPropertyName(String attributeName) {
		if ("class".equals(attributeName)) {
			return CustomTask.CLASS;
		} 
		return super.getPropertyName(attributeName);
	}

}