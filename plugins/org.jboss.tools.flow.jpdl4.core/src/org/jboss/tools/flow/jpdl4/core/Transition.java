package org.jboss.tools.flow.jpdl4.core;

import java.util.HashMap;

import org.jboss.tools.flow.common.core.AbstractConnection;
import org.jboss.tools.flow.common.core.Node;

public class Transition extends AbstractConnection {
	
	private HashMap<String, Object> metaData = new HashMap<String, Object>();

	public Transition(Node from, Node to) {
		super(from, to);
	}

	public Object getMetaData(String key) {
		return metaData.get(key);
	}

	public void setMetaData(String key, Object value) {
		metaData.put(key, value);
	}

	public String getFromType() {
		return null;
	}

	public String getToType() {
		return null;
	}

}
