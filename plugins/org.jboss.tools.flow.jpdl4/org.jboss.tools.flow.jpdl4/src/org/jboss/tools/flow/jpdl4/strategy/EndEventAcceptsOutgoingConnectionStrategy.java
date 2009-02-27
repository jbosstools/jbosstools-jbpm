package org.jboss.tools.flow.jpdl4.strategy;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.strategy.AcceptsOutgoingConnectionStrategy;

public class EndEventAcceptsOutgoingConnectionStrategy implements AcceptsOutgoingConnectionStrategy {

	public boolean acceptsOutgoingConnection(Connection connection, Node source) {
		return false;
	}

	public void setNode(Node node) {
	}

}
