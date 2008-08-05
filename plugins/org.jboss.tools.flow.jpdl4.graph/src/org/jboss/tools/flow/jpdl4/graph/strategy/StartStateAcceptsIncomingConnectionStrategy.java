package org.jboss.tools.flow.jpdl4.graph.strategy;

import org.jboss.tools.flow.common.core.Connection;
import org.jboss.tools.flow.common.core.Node;
import org.jboss.tools.flow.editor.strategy.AcceptsIncomingConnectionStrategy;

public class StartStateAcceptsIncomingConnectionStrategy implements AcceptsIncomingConnectionStrategy {

	public boolean acceptsIncomingConnection(Connection connection, Node source) {
		return false;
	}

	public void setNode(Node node) {
	}

}
