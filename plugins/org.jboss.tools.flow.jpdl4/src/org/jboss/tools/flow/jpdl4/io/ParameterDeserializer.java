/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.jpdl4.model.Parameter;

class ParameterDeserializer extends AbstractElementDeserializer {
	protected List<String> getAttributesToRead() {
		List<String> result = super.getAttributesToRead();
		result.add(Parameter.VAR);
		result.add(Parameter.SUBVAR);
		result.add(Parameter.EXPR);
		result.add(Parameter.LANG);
		return result;
	}
	protected String getXmlName(String attributeName) {
		if (Parameter.VAR.equals(attributeName)) {
			return "var";
		} else if (Parameter.SUBVAR.equals(attributeName)) {
			return "subvar";
   		} else if (Parameter.EXPR.equals(attributeName)) {
			return "expr";
   		} else if (Parameter.LANG.equals(attributeName)) {
			return "lang";
		} else {
			return super.getXmlName(attributeName);
		}
	}
}