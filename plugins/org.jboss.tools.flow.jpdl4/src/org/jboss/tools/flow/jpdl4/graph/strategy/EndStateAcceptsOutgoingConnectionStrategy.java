package org.jboss.tools.flow.jpdl4.graph.strategy;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.editor.strategy.AcceptsOutgoingConnectionStrategy;

public class EndStateAcceptsOutgoingConnectionStrategy implements AcceptsOutgoingConnectionStrategy {

	public boolean acceptsOutgoingConnection(Connection connection, Node source) {
		return false;
	}

	public void setNode(Node node) {
	}

}
