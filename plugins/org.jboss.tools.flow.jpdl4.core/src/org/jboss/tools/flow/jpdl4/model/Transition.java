package org.jboss.tools.flow.jpdl4.model;

import org.jboss.tools.flow.common.model.DefaultConnection;
import org.jboss.tools.flow.common.model.Node;

public class Transition extends DefaultConnection {

	public Transition(Node from, Node to) {
		super(from, to);
	}
	
}
