package org.jboss.tools.flow.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultNode extends DefaultElement implements Node {
	
	private long id;
	private String name;
	private Container container;
	
	private List<Connection> incomingConnections = new ArrayList<Connection>();
	private List<Connection> outgoingConnections = new ArrayList<Connection>();

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

	public Container getNodeContainer() {
		return container;
	}
	
	public void setNodeContainer(Container container) {
		this.container = container;
	}

	public void addIncomingConnection(String type, Connection connection) {
		incomingConnections.add(connection);
	}

	public void addOutgoingConnection(String type, Connection connection) {
		outgoingConnections.add(connection);
	}

	public Map<String, List<Connection>> getIncomingConnections() {
		HashMap<String, List<Connection>> result = new HashMap<String, List<Connection>>();
		result.put(null, getIncomingConnections(null));
		return result;
	}

	public List<Connection> getIncomingConnections(String type) {
		return new ArrayList<Connection>(incomingConnections);
	}

	public Map<String, List<Connection>> getOutgoingConnections() {
		HashMap<String, List<Connection>> result = new HashMap<String, List<Connection>>();
		result.put(null, getOutgoingConnections(null));
		return result;
	}

	public List<Connection> getOutgoingConnections(String type) {
		return new ArrayList<Connection>(outgoingConnections);
	}

	public void removeIncomingConnection(String type, Connection connection) {
		incomingConnections.remove(connection);
	}

	public void removeOutgoingConnection(String type, Connection connection) {
		outgoingConnections.remove(connection);
	}

}
