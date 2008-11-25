package org.jboss.tools.flow.jpdl4.model;

import org.jboss.tools.flow.common.model.DefaultConnection;
import org.jboss.tools.flow.common.model.Node;

public class SequenceFlow extends DefaultConnection {

	public SequenceFlow() {
		this(null, null);
	}
	
	public SequenceFlow(Node from, Node to) {
		super(from, to);
	}
		
}
