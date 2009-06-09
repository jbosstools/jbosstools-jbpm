package org.jbpm.gd.jpdl.editor;

import org.eclipse.gef.EditPart;
import org.jbpm.gd.common.editor.SelectionSynchronizer;
import org.jbpm.gd.jpdl.part.NodeElementOutlineEditPart;
import org.jbpm.gd.jpdl.part.ProcessDefinitionOutlineEditPart;
import org.jbpm.gd.jpdl.part.TransitionOutlineEditPart;

public class JpdlSelectionSynchronizer extends SelectionSynchronizer {
	
	protected boolean isSelectablePartInGraphicalViewer(EditPart part) {
		return part instanceof ProcessDefinitionOutlineEditPart ||
			part instanceof NodeElementOutlineEditPart ||
			part instanceof TransitionOutlineEditPart;
	}
	
}
