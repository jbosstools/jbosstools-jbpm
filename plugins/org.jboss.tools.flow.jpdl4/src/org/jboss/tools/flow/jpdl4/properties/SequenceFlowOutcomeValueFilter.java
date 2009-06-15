package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.editpart.SequenceFlowGraphicalEditPart;
import org.jboss.tools.flow.jpdl4.model.SubprocessTask;

public class SequenceFlowOutcomeValueFilter implements IFilter {

	public boolean select(Object toTest) {
		if (!(toTest instanceof SequenceFlowGraphicalEditPart)) return false;
		SequenceFlowGraphicalEditPart editPart = (SequenceFlowGraphicalEditPart)toTest;
		EditPart source = editPart.getSource();
		Wrapper wrapper = (Wrapper)source.getModel();
		return wrapper.getElement() instanceof SubprocessTask;
	}

}
