package org.jboss.tools.flow.jpdl4.editpart;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.wrapper.NodeWrapper;

public class TaskTreeRootEditPart extends JpdlTreeEditPart {
	
	public TaskTreeRootEditPart(NodeWrapper nodeWrapper) {
		super(nodeWrapper);
	}
	
	protected List<Object> getModelChildren() {
		List<Object> result = new ArrayList<Object>();
		result.add(new EventListenerContainerListTreeEditPart(null));
		return result;
	}
	
}
