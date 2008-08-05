package org.jboss.tools.flow.jpdl4.wrapper;

import org.jboss.tools.flow.common.wrapper.DefaultFlowWrapper;
import org.jboss.tools.flow.jpdl4.model.Process;
import org.jboss.tools.flow.jpdl4.strategy.ProcessAcceptsElementStrategy;

public class ProcessWrapper extends DefaultFlowWrapper {

    public ProcessWrapper() {
    	Process process = new Process();
        setElement(process);
        ProcessAcceptsElementStrategy acceptsElementStrategy = new ProcessAcceptsElementStrategy();
        acceptsElementStrategy.setContainer(process);
        setAcceptsElementStrategy(acceptsElementStrategy);
    }

}
