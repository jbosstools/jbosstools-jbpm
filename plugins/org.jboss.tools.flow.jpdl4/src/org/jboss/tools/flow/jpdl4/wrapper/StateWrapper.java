package org.jboss.tools.flow.jpdl4.wrapper;

import org.jboss.tools.flow.common.wrapper.DefaultNodeWrapper;
import org.jboss.tools.flow.jpdl4.model.State;

public class StateWrapper extends DefaultNodeWrapper {

    public StateWrapper() {
    	State element = new State();
        setElement(element);
        element.setName("State");
    }
    
}
