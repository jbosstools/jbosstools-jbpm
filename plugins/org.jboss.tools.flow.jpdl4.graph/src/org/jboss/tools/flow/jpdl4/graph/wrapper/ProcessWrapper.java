package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.editor.core.DefaultFlowWrapper;
import org.jboss.tools.flow.jpdl4.core.Process;
import org.jboss.tools.flow.jpdl4.graph.strategy.ProcessAcceptsElementStrategy;

public class ProcessWrapper extends DefaultFlowWrapper {

    public ProcessWrapper() {
        setElement(new Process());
        ProcessAcceptsElementStrategy acceptsElementStrategy = new ProcessAcceptsElementStrategy();
        acceptsElementStrategy.setContainer((Process)getElement());
    }

}
