package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.jboss.tools.flow.jpdl4.model.Process;

public class JpdlTreeEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof Process && context == null) return new ProcessTreeRootEditPart((Process)model);
		if (model instanceof ProcessNodeListTreeEditPart) return (EditPart)model;
		return null;
	}
	
}
