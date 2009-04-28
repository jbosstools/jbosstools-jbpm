package org.jboss.tools.flow.jpdl4.editpart;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;

public class SequenceFlowTreeRootEditPart extends JpdlTreeEditPart {
	
	public SequenceFlowTreeRootEditPart(ConnectionWrapper connectionWrapper) {
		super(connectionWrapper);
	}
	
	protected List<Object> getModelChildren() {
		List<Object> result = new ArrayList<Object>();
		result.add(new EventListenerListTreeEditPart(null));
		return result;
	}
	
}
