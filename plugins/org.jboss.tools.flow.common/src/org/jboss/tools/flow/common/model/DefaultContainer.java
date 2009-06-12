package org.jboss.tools.flow.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultContainer extends DefaultNode implements Container {
	
	private String name;
	private HashMap<String, Object> metaData = new HashMap<String, Object>();
	private ArrayList<Node> nodes = new ArrayList<Node>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getMetaData(String key) {
		return metaData.get(key);
	}

	public void setMetaData(String key, Object value) {
		metaData.put(key, value);
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void addNode(Node node) {
		nodes.add(node);
		node.setNodeContainer(this);
	}

	public void removeNode(Node node) {
		node.setNodeContainer(null);
		nodes.remove(node);
	}
	
	public Node getNode(long id) {
		for (Node node : nodes) {
			if (node.getId() == id) {
				return node;
			}
		} 
		return null;
	}
	
}
