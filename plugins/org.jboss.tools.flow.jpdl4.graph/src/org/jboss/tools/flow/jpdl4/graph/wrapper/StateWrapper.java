package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.DefaultNodeWrapper;
import org.jboss.tools.flow.jpdl4.core.State;

public class StateWrapper extends DefaultNodeWrapper {

    public StateWrapper() {
        setElement(new State());
        getState().setName("State");
    }
    
    public State getState() {
        return (State) getElement();
    }
    
    
}
