package org.jboss.tools.flow.jpdl4.core;

import java.util.Iterator;

import org.jboss.tools.flow.common.core.AbstractFlow;
import org.jboss.tools.flow.common.core.Node;

public class Process extends AbstractFlow {
		
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
