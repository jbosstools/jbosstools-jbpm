package org.jboss.tools.flow.jpdl4.editpart;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.jpdl4.model.Process;

import org.jboss.tools.flow.common.wrapper.FlowWrapper;

public class ProcessTreeRootEditPart extends JpdlTreeEditPart {
	
	public ProcessTreeRootEditPart(FlowWrapper flowWrapper) {
		super(flowWrapper);
	}
	
	private Process getProcess() {
		return (Process)((FlowWrapper)getModel()).getElement();
	}

	protected void createEditPolicies() {
	}

	protected List<Object> getModelChildren() {
		List<Object> result = new ArrayList<Object>();
		result.add(new EventListenerContainerListTreeEditPart(null));
//		Process process = getProcess();
//		if (process.get)
		return result;
//		List<Object> result = new ArrayList<Object>();
//		if (modelChildren == null) {
//			modelChildren = initModelChildren();
//		}
//		return modelChildren;
	}
	
}
