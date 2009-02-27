package org.jboss.tools.flow.jpdl4.strategy;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.strategy.AcceptsIncomingConnectionStrategy;

public class StartEventAcceptsIncomingConnectionStrategy implements AcceptsIncomingConnectionStrategy {

	public boolean acceptsIncomingConnection(Connection connection, Node source) {
		return false;
	}

	public void setNode(Node node) {
	}

}
