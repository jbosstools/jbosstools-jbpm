/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.ScriptTask;

class ScriptTaskSerializer extends ProcessNodeSerializer {
	protected List<String> getAttributesToSave() {
		List<String> result = super.getAttributesToSave();
		result.add("expr");
		result.add("lang");
		result.add("var");
		return result;
	}
	protected String getPropertyName(String attributeName) {
		if ("expr".equals(attributeName)) {
			return ScriptTask.EXPR;
		} else if ("lang".equals(attributeName)) {
			return ScriptTask.LANG;
		} else if ("var".equals(attributeName)) {
			return ScriptTask.VAR;
		}
		return super.getPropertyName(attributeName);
	}
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
		String text = (String)wrapper.getPropertyValue(ScriptTask.TEXT);
		if (text != null && !("".equals(text))) {
			buffer.append("\n");
			appendPadding(buffer, level + 1);
			buffer.append("<text>" + text + "</text>");
		} 
		super.appendBody(buffer, wrapper, level);
	}
}