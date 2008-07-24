package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.jpdl4.core.State;

public class StateWrapper extends BaseNodeWrapper {

    public StateWrapper() {
        setElement(new State());
        getState().setName("State");
    }
    
    public State getState() {
        return (State) getElement();
    }
    
    
}
