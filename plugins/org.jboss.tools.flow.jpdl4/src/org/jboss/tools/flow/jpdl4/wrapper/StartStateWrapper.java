package org.jboss.tools.flow.jpdl4.wrapper;

import org.jboss.tools.flow.common.strategy.AcceptsIncomingConnectionStrategy;
import org.jboss.tools.flow.common.wrapper.DefaultNodeWrapper;
import org.jboss.tools.flow.jpdl4.model.StartState;
import org.jboss.tools.flow.jpdl4.strategy.StartStateAcceptsIncomingConnectionStrategy;

public class StartStateWrapper extends DefaultNodeWrapper {

    public StartStateWrapper() {
    	StartState element = new StartState();
        setElement(element);
        setName("Start");
        AcceptsIncomingConnectionStrategy strategy = new StartStateAcceptsIncomingConnectionStrategy();
        strategy.setNode(element);
        
    }
    
}
