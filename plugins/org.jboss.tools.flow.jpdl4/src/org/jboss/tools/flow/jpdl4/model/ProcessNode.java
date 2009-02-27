package org.jboss.tools.flow.jpdl4.model;

import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.DefaultNode;
import org.jboss.tools.flow.jpdl4.util.Jpdl4Helper;

public class ProcessNode extends DefaultNode {

	protected boolean isPropagationExclusive() {
		return false;
	}
	
	public void removeOutgoingConnection(String type, Connection connection) {
		Jpdl4Helper.mergeLeadingNodes(connection);
		super.removeOutgoingConnection(type, connection);
	}

}
