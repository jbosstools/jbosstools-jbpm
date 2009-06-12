package org.jboss.tools.flow.common.model;

import java.util.HashMap;

public class DefaultElement implements Element {

	private HashMap<String, Object> metaData = new HashMap<String, Object>();
	
	public Object getMetaData(String key) {
		return metaData.get(key);
	}

	public void setMetaData(String key, Object value) {
		metaData.put(key, value);
	}
	
}
