package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.DefaultContainerWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;
import org.jboss.tools.flow.jpdl4.core.Process;
import org.jboss.tools.flow.jpdl4.core.StartState;
import org.jboss.tools.flow.jpdl4.core.SuperState;

public class SuperStateWrapper extends DefaultContainerWrapper {
	
    public SuperStateWrapper() {
        setElement(new SuperState());
    }
    
	public boolean acceptsElement(NodeWrapper element) {
    	if (element.getElement() instanceof StartState) {
    		return ((Process)getFlowWrapper().getElement()).getStartState() == null;
    	}
    	return super.acceptsElement(element); 
	}

}
