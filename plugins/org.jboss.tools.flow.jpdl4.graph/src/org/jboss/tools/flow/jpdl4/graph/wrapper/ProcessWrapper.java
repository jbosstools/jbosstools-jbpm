package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.DefaultFlowWrapper;
import org.jboss.tools.flow.jpdl4.graph.strategy.ProcessAcceptsElementStrategy;
import org.jboss.tools.flow.jpdl4.model.Process;

public class ProcessWrapper extends DefaultFlowWrapper {

    public ProcessWrapper() {
    	Process process = new Process();
        setElement(process);
        ProcessAcceptsElementStrategy acceptsElementStrategy = new ProcessAcceptsElementStrategy();
        acceptsElementStrategy.setContainer(process);
        setAcceptsElementStrategy(acceptsElementStrategy);
    }

}
