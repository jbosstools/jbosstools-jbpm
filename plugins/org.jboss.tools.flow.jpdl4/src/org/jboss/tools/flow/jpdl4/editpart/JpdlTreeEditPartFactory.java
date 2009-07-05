package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Argument;
import org.jboss.tools.flow.jpdl4.model.EventListener;
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;
import org.jboss.tools.flow.jpdl4.model.Field;
import org.jboss.tools.flow.jpdl4.model.HqlTask;
import org.jboss.tools.flow.jpdl4.model.JavaTask;
import org.jboss.tools.flow.jpdl4.model.Parameter;
import org.jboss.tools.flow.jpdl4.model.PrimitiveObject;
import org.jboss.tools.flow.jpdl4.model.SubprocessTask;
import org.jboss.tools.flow.jpdl4.model.Swimlane;
import org.jboss.tools.flow.jpdl4.model.Timer;

public class JpdlTreeEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof FlowWrapper && context == null) return new ProcessTreeRootEditPart((FlowWrapper)model);
		if (model instanceof NodeWrapper && context == null) {
			Element element = ((NodeWrapper)model).getElement();
			if (element instanceof SubprocessTask) return new SubprocessTaskTreeRootEditPart((Wrapper)model);
			if (element instanceof JavaTask) return new JavaTaskTreeRootEditPart((Wrapper)model);
			if (element instanceof HqlTask) return new HqlTaskTreeRootEditPart((Wrapper)model);
			return new TaskTreeRootEditPart((NodeWrapper)model);
		}
		if (model instanceof ConnectionWrapper && context == null) return new SequenceFlowTreeRootEditPart((ConnectionWrapper)model);
		if (model instanceof Wrapper) {
			Element  element = ((Wrapper)model).getElement();
			if (element instanceof Swimlane) return new SwimlaneTreeEditPart((Wrapper)model);
			if (element instanceof Parameter) return new ParameterTreeEditPart((Wrapper)model);
			if (element instanceof Argument) return new ArgumentTreeEditPart((Wrapper)model);
			if (element instanceof Field) return new FieldTreeEditPart((Wrapper)model);
			if (element instanceof EventListenerContainer) return new EventListenerListTreeEditPart((Wrapper)model);
			if (element instanceof EventListener) return new EventListenerTreeEditPart((Wrapper)model);
			if (element instanceof Timer) return new TimerTreeEditPart((Wrapper)model);
			if (element instanceof PrimitiveObject) return new PrimitiveObjectTreeEditPart((Wrapper)model);
			return new NoDetailsTreeRootEditPart();
		}
		if (model instanceof SwimlaneListTreeEditPart) return (EditPart)model;
		if (model instanceof InputParameterListTreeEditPart) return (EditPart)model;
		if (model instanceof OutputParameterListTreeEditPart) return (EditPart)model;
		if (model instanceof ArgumentListTreeEditPart) return (EditPart)model;
		if (model instanceof FieldListTreeEditPart) return (EditPart)model;
		if (model instanceof TimerListTreeEditPart) return (EditPart)model;
		if (model instanceof ListenerListTreeEditPart) return (EditPart)model;
		if (model instanceof ParameterListTreeEditPart) return (EditPart)model;
		return new NoDetailsTreeRootEditPart();
	}
	
}
