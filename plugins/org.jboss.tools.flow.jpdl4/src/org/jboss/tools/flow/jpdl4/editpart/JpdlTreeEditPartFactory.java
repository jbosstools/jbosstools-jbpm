package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.EventListener;
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;
import org.jboss.tools.flow.jpdl4.model.Swimlane;
import org.jboss.tools.flow.jpdl4.model.Timer;

public class JpdlTreeEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof FlowWrapper && context == null) return new ProcessTreeRootEditPart((FlowWrapper)model);
		if (model instanceof NodeWrapper && context == null) return new TaskTreeRootEditPart((NodeWrapper)model);
		if (model instanceof ConnectionWrapper && context == null) return new SequenceFlowTreeRootEditPart((ConnectionWrapper)model);
		if (model instanceof Wrapper) {
			Element  element = ((Wrapper)model).getElement();
			if (element instanceof Swimlane) return new SwimlaneTreeEditPart((Wrapper)model);
			if (element instanceof EventListenerContainer) return new EventListenerListTreeEditPart((Wrapper)model);
			if (element instanceof EventListener) return new EventListenerTreeEditPart((Wrapper)model);
			if (element instanceof Timer) return new TimerTreeEditPart((Wrapper)model);
			return new NoDetailsTreeRootEditPart();
		}
		if (model instanceof SwimlaneListTreeEditPart) return (EditPart)model;
		if (model instanceof TimerListTreeEditPart) return (EditPart)model;
		return new NoDetailsTreeRootEditPart();
	}
	
}
