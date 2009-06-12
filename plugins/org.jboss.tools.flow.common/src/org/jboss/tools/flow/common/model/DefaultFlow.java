package org.jboss.tools.flow.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultFlow extends DefaultElement implements Flow {
	
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

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPackageName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public void setPackageName(String packageName) {
		// TODO Auto-generated method stub
		
	}

	public void setType(String type) {
		// TODO Auto-generated method stub
		
	}

	public void setVersion(String version) {
		// TODO Auto-generated method stub
		
	}

	public Node getNode(long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
