/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.jpdl4.model.Parameter;

class ParameterSerializer extends AbstractElementSerializer {
	protected List<String> getAttributesToSave() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("var");
		result.add("subvar");
		result.add("expr");
		result.add("lang");
		return result;
	}
	protected String getPropertyName(String attributeName) {
		if ("var".equals(attributeName)) {
			return Parameter.VAR;
		} else if ("subvar".equals(attributeName)) {
			return Parameter.SUBVAR;
		} else if ("expr".equals(attributeName)) {
			return Parameter.EXPR;
		} else if ("lang".equals(attributeName)) {
			return Parameter.LANG;
		}
		return super.getPropertyName(attributeName);
	}
}