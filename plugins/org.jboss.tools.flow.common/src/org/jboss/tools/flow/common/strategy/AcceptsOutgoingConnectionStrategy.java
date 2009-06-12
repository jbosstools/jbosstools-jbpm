package org.jboss.tools.flow.common.strategy;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Node;

public interface AcceptsOutgoingConnectionStrategy {
	
	void setNode(Node node);
	
	boolean acceptsOutgoingConnection(Connection connection, Node target);
	

}
