package org.jboss.tools.flow.jpdl4.core;

import java.util.HashMap;

public class Transition {
	
	private HashMap<String, Object> metaData = new HashMap<String, Object>();

	public Transition(Node from, Node to) {
	}

	public Object getMetaData(String key) {
		return metaData.get(key);
	}

	public void setMetaData(String key, Object value) {
		metaData.put(key, value);
	}

}
