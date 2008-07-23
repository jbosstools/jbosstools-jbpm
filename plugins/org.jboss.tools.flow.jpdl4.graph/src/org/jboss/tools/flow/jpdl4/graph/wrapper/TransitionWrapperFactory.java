package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.AbstractConnectionWrapper;
import org.jboss.tools.flow.editor.core.ConnectionFactory;

public class TransitionWrapperFactory implements ConnectionFactory {
	
	public AbstractConnectionWrapper createElementConnection() {
		return new TransitionWrapper();
	}

}
