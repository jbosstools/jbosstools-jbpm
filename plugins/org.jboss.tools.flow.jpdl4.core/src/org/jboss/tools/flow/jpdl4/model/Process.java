package org.jboss.tools.flow.jpdl4.model;

import java.util.Iterator;

import org.jboss.tools.flow.common.model.DefaultFlow;
import org.jboss.tools.flow.common.model.Node;

public class Process extends DefaultFlow {
	
	public StartState getStartState() {
		for (Iterator<Node> iterator = getNodes().iterator(); iterator.hasNext(); ) {
			Node node = iterator.next();
			if (node instanceof StartState) {
				return (StartState)node;
			}
		}
		return null;
	}

}
