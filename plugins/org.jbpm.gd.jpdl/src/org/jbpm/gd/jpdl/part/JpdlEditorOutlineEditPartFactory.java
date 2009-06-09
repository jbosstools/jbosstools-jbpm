package org.jbpm.gd.jpdl.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.jbpm.gd.jpdl.model.AbstractNode;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.ActionElement;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.Node;
import org.jbpm.gd.jpdl.model.NodeElement;
import org.jbpm.gd.jpdl.model.ProcessDefinition;
import org.jbpm.gd.jpdl.model.StartState;
import org.jbpm.gd.jpdl.model.SuperState;
import org.jbpm.gd.jpdl.model.Swimlane;
import org.jbpm.gd.jpdl.model.Task;
import org.jbpm.gd.jpdl.model.TaskNode;
import org.jbpm.gd.jpdl.model.Transition;

public class JpdlEditorOutlineEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		if (model == null) return null;
		if (model instanceof AbstractTreeEditPart) {
			return (EditPart)model;
		} else if (model instanceof ProcessDefinition) {
			return new ProcessDefinitionOutlineEditPart((ProcessDefinition)model);
		} else if (model instanceof Transition) {
			return new TransitionOutlineEditPart((Transition)model);
		} else if (model instanceof Task) {
			return new TaskOutlineEditPart((Task)model);
		} else if (model instanceof Swimlane) {
			return new SwimlaneOutlineEditPart((Swimlane)model);
		} else if (model instanceof TaskNode) {
			return new TaskNodeOutlineEditPart((TaskNode)model);
		} else if (model instanceof StartState) {
			return new StartStateOutlineEditPart((StartState)model);
		} else if (model instanceof Node) {
			return new NodeOutlineEditPart((Node)model);
		} else if (model instanceof SuperState) {
			return new SuperStateOutlineEditPart((SuperState)model);
		} else if (model instanceof Event) {
			return new EventOutlineEditPart((Event)model);
		} else if (model instanceof Action) {
			return new ActionOutlineEditPart((Action)model);
		} else if (model instanceof ActionElement) {
			return new ActionElementOutlineEditPart((ActionElement)model);
		} else if (model instanceof NodeElement) {
			return new NodeElementOutlineEditPart((AbstractNode)model);
		}
		return null;
	}

}
