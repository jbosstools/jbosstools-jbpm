package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.DefaultNodeWrapper;
import org.jboss.tools.flow.editor.strategy.AcceptsIncomingConnectionStrategy;
import org.jboss.tools.flow.jpdl4.core.StartState;
import org.jboss.tools.flow.jpdl4.graph.strategy.StartStateAcceptsIncomingConnectionStrategy;

public class StartStateWrapper extends DefaultNodeWrapper {

    public StartStateWrapper() {
    	StartState element = new StartState();
        setElement(element);
        setName("Start");
        AcceptsIncomingConnectionStrategy strategy = new StartStateAcceptsIncomingConnectionStrategy();
        strategy.setNode(element);
        
    }
    
}
