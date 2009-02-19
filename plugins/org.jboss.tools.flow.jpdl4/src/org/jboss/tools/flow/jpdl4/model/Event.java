package org.jboss.tools.flow.jpdl4.model;

import org.jboss.tools.flow.common.model.Connection;


public class Event extends ProcessNode {

	public void addOutgoingConnection(String type, Connection connection) {
		super.addOutgoingConnection(type, connection);
		if (isPropagationExclusive() && !((SequenceFlow)connection).isDefault()) {
			((SequenceFlow)connection).setConditional(true);
		}
	}
	
}
