package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;

public class JpdlTreeEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof FlowWrapper && context == null) return new ProcessTreeRootEditPart((FlowWrapper)model);
		if (model instanceof NodeWrapper && context == null) return new TaskTreeRootEditPart((NodeWrapper)model);
		if (model instanceof ConnectionWrapper && context == null) return new SequenceFlowTreeRootEditPart((ConnectionWrapper)model);
		if (model instanceof ProcessNodeListTreeEditPart) return (EditPart)model;
		if (model instanceof SwimlaneListTreeEditPart) return (EditPart)model;
		if (model instanceof EventListenerContainerListTreeEditPart) return (EditPart)model;
		if (model instanceof TimerListTreeEditPart) return (EditPart)model;
		return new NoDetailsTreeRootEditPart();
	}
	
}
