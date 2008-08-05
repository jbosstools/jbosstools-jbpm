package org.jboss.tools.flow.jpdl4.wrapper;

import org.jboss.tools.flow.common.strategy.AcceptsOutgoingConnectionStrategy;
import org.jboss.tools.flow.common.wrapper.DefaultNodeWrapper;
import org.jboss.tools.flow.jpdl4.model.EndState;
import org.jboss.tools.flow.jpdl4.strategy.EndStateAcceptsOutgoingConnectionStrategy;

public class EndStateWrapper extends DefaultNodeWrapper {

    public EndStateWrapper() {
    	EndState element = new EndState();
        setElement(element);
        setName("End");
        AcceptsOutgoingConnectionStrategy strategy = new EndStateAcceptsOutgoingConnectionStrategy();
        strategy.setNode(element);
        setAcceptsOutgoingConnectionStrategy(strategy);
    }
    
}
