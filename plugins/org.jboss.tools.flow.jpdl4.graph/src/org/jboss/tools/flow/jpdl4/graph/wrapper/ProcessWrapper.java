package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.DefaultFlowWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;
import org.jboss.tools.flow.jpdl4.core.Process;
import org.jboss.tools.flow.jpdl4.core.StartState;

public class ProcessWrapper extends DefaultFlowWrapper {

    public ProcessWrapper() {
        setElement(new Process());
    }

    public Process getProcess() {
        return (Process) getElement();
    }
    
    public boolean acceptsElement(NodeWrapper element) {
    	if (element.getElement() instanceof StartState) {
    		return getProcess().getStartState() == null;
    	}
    	return super.acceptsElement(element); 
    }
    
}
