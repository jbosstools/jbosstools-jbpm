package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.AbstractConnectionWrapper;
import org.jboss.tools.flow.editor.core.DefaultNodeWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;
import org.jboss.tools.flow.jpdl4.core.StartState;

public class StartStateWrapper extends DefaultNodeWrapper {

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

}
