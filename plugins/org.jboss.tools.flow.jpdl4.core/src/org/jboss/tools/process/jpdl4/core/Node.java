package org.jboss.tools.process.jpdl4.core;

import java.util.HashMap;

public class Node {
	
	private long id;
	private String name;
	private Process container;
	
	private HashMap<String, Object> metaData = new HashMap<String, Object>();

	public long getId() {
		return id;
	}

	public void setId(long l) {
		id = l;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMetaData(String key, Object value) {
		metaData.put(key, value);
	}

	public Object getMetaData(String key) {
		return metaData.get(key);
	}

	public Object getNodeContainer() {
		return container;
	}
	
	public void setNodeContainer(Process container) {
		this.container = container;
	}

}
