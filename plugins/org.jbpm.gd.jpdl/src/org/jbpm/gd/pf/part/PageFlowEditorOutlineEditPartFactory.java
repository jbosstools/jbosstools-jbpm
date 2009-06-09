package org.jbpm.gd.pf.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.jbpm.gd.pf.model.NodeElement;
import org.jbpm.gd.pf.model.PageFlowDefinition;
import org.jbpm.gd.pf.model.Transition;

public class PageFlowEditorOutlineEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		if (model == null) return null;
		if (model instanceof AbstractTreeEditPart) {
			return (EditPart)model;
		} else if (model instanceof PageFlowDefinition) {
			return new PageFlowDefinitionOutlineEditPart((PageFlowDefinition)model);
		} else if (model instanceof NodeElement) {
			return new NodeElementOutlineEditPart((NodeElement)model);
		} else if (model instanceof Transition) {
			return new TransitionOutlineEditPart((Transition)model);
		}
		return null;
	}

}
