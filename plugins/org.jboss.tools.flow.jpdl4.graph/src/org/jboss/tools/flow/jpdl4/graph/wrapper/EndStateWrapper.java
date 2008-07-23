package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.AbstractConnectionWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;
import org.jboss.tools.flow.jpdl4.core.EndState;

public class EndStateWrapper extends BaseNodeWrapper {

    public EndStateWrapper() {
        setElement(new EndState());
        setName("End");
    }
    
    public EndState getEndState() {
        return (EndState) getElement();
    }
    
    public boolean acceptsIncomingConnection(AbstractConnectionWrapper connection, NodeWrapper source) {
        return super.acceptsIncomingConnection(connection, (BaseNodeWrapper)source)
    	&& getIncomingConnections().isEmpty();
    }

    public boolean acceptsOutgoingConnection(AbstractConnectionWrapper connection, NodeWrapper target) {
        return false;
    }
}
