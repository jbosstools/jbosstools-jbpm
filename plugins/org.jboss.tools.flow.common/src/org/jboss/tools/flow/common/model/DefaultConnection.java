package org.jboss.tools.flow.common.model;


public class DefaultConnection extends DefaultElement implements Connection {
	
	private Node from, to;
	
	public DefaultConnection() {
		this(null, null);
	}

	public DefaultConnection(Node from, Node to) {
		setFrom(from);
		setTo(to);
	}

	public Node getFrom() {
		return from;
	}
	
	public void setFrom(Node node) {
		if (from != null) {
			from.removeOutgoingConnection(null, this);
		}
		from = node;
		if (from != null) {
			from.addOutgoingConnection(null, this);
		}
	}
	
	public Node getTo() {
		return to;
	}
	
	public void setTo(Node node) {
		if (to != null) {
			to.removeIncomingConnection(null, this);
		}
		to = node;
		if (to != null) {
			to.addIncomingConnection(null, this);
		}
	}

	public String getFromType() {
		return null;
	}

	public String getToType() {
		return null;
	}

}
