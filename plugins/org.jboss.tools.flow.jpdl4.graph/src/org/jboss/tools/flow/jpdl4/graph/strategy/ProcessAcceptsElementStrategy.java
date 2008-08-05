package org.jboss.tools.flow.jpdl4.graph.strategy;

import org.jboss.tools.flow.common.core.Container;
import org.jboss.tools.flow.editor.strategy.AcceptsElementStrategy;
import org.jboss.tools.flow.jpdl4.core.Process;
import org.jboss.tools.flow.jpdl4.core.StartState;

public class ProcessAcceptsElementStrategy implements AcceptsElementStrategy {
	
	private Process process;

	public boolean acceptsElement(Object element) {
		if (process == null) {
			return false;
		} else if (element instanceof StartState) {
			return process.getStartState() == null;
		} else {
			return true;
		}
	}
	
	public void setContainer(Container container) {
		if (container instanceof Process) {
			this.process = (Process)container;
		}
	}

}
