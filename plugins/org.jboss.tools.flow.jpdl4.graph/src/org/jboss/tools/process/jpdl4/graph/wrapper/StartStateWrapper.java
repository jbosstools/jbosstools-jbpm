package org.jboss.tools.process.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.AbstractConnectionWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;
import org.jboss.tools.flow.jpdl4.core.StartState;

public class StartStateWrapper extends BaseNodeWrapper {

    private static final long serialVersionUID = 1L;

    public StartStateWrapper() {
        setElement(new StartState());
        setName("Start");
    }
    
    public StartState getStartState() {
        return (StartState) getElement();
    }
    
    public boolean acceptsIncomingConnection(AbstractConnectionWrapper connection, NodeWrapper source) {
        return false;
    }

    public boolean acceptsOutgoingConnection(AbstractConnectionWrapper connection, NodeWrapper target) {
        return super.acceptsOutgoingConnection(connection, (BaseNodeWrapper)target)
        	&& getOutgoingConnections().isEmpty();
    }
}
