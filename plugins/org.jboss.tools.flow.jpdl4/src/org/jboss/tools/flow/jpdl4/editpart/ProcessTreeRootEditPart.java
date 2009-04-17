package org.jboss.tools.flow.jpdl4.editpart;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.jpdl4.model.Process;

public class ProcessTreeRootEditPart extends JpdlTreeEditPart {
	
	List<Object> modelChildren;
	
	public ProcessTreeRootEditPart(Process process) {
		super(process);
	}

	protected void createEditPolicies() {
	}

	private List<Object> initModelChildren() {
		List<Object> result = new ArrayList<Object>();
		result.add(new SwimlaneListTreeEditPart((Process)getModel()));
		result.add(new EventListenerContainerListTreeEditPart((Process)getModel()));
		result.add(new TimerListTreeEditPart((Process)getModel()));
		result.add(new ProcessNodeListTreeEditPart((Process)getModel()));
		return result;
	}
	
	protected List<Object> getModelChildren() {
		if (modelChildren == null) {
			modelChildren = initModelChildren();
		}
		return modelChildren;
	}
	
}
