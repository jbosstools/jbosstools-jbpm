package org.jboss.tools.process.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.AbstractConnectionWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;
import org.jboss.tools.flow.jpdl4.core.State;

public class StateWrapper extends BaseNodeWrapper {

    public StateWrapper() {
        setElement(new State());
        getState().setName("State");
    }
    
    public State getState() {
        return (State) getElement();
    }
    
    public boolean acceptsIncomingConnection(AbstractConnectionWrapper connection, NodeWrapper source) {
        return super.acceptsIncomingConnection(connection, (BaseNodeWrapper)source)
        	&& getIncomingConnections().isEmpty();
    }

    public boolean acceptsOutgoingConnection(AbstractConnectionWrapper connection, NodeWrapper target) {
        return super.acceptsOutgoingConnection(connection, (BaseNodeWrapper)target)
        	&& getOutgoingConnections().isEmpty();
    }
    
}
