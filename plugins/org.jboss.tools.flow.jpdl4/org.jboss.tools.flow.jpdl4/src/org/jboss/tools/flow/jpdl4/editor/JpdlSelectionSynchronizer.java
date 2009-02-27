package org.jboss.tools.flow.jpdl4.editor;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class JpdlSelectionSynchronizer extends SelectionSynchronizer {

	@SuppressWarnings("unchecked")
	protected EditPart convert(EditPartViewer viewer, EditPart part) {
		if (!(viewer instanceof GraphicalViewer)) return null;
		if (!(part instanceof AbstractTreeEditPart)) return null;
		Iterator<EditPart> iterator = viewer.getEditPartRegistry().values().iterator();
		while (iterator.hasNext()) {
			EditPart editPart = iterator.next();
			Wrapper wrapper = (Wrapper)editPart.getModel();
			if (part.getModel() == wrapper.getElement()) {
				return editPart;
			}
		}
		return null;
	}
	
}
