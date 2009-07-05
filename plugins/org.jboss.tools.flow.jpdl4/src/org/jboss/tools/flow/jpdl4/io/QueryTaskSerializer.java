/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.QueryTask;

class QueryTaskSerializer extends ProcessNodeSerializer {
	
	protected List<String> getAttributesToSave() {
		List<String> result = super.getAttributesToSave();
		result.add("var");
		result.add("unique");
		return result;
	}
	
	protected String getPropertyName(String attributeName) {
		if ("var".equals(attributeName)) {
			return QueryTask.VAR;
		} else if ("unique".equals(attributeName)) {
			return QueryTask.UNIQUE;
		}
		return super.getPropertyName(attributeName);
	}
	
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
		String query = (String)wrapper.getPropertyValue(QueryTask.QUERY);
		if (query != null && !"".equals(query)) {
			appendQuery(buffer, wrapper, query, level+1);
		}
		List<Element> parameters =wrapper.getChildren(QueryTask.PARAMETERS);
		if (parameters != null) {
			appendParameters(buffer, wrapper, parameters, level+1);
		}
		super.appendBody(buffer, wrapper, level);
	}
	
	private void appendQuery(StringBuffer buffer, Wrapper wrapper, String query, int level) {
		appendUnknownNodes("beforeQueryNodes", buffer, wrapper, level);
		buffer.append("<query>").append(query).append("</query>");
	}
	
	private void appendParameters(StringBuffer buffer, Wrapper wrapper, List<Element> parameters, int level) {
		appendUnknownNodes("beforeParametersNodes", buffer, wrapper, level);
		for (Element parameter : parameters) {
			if (parameter instanceof Wrapper) {
				JpdlSerializer.serialize((Wrapper)parameter, buffer, level+1);
			}
		}
	}
}