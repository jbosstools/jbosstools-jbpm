package org.jboss.tools.flow.jpdl4.editor;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class JpdlSelectionSynchronizer extends SelectionSynchronizer {

	protected EditPart convert(EditPartViewer viewer, EditPart part) {
		if (viewer instanceof GraphicalViewer) {
			return convertToGraphicalViewerPart((GraphicalViewer)viewer, part);
		} else if (viewer instanceof TreeViewer) {
			return convertToTreeViewerPart((TreeViewer)viewer, part);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected EditPart convertToGraphicalViewerPart(GraphicalViewer viewer, EditPart part) {
		Iterator<EditPart> iterator = viewer.getEditPartRegistry().values().iterator();
		while (iterator.hasNext()) {
			EditPart editPart = iterator.next();
			if (editPart.getModel() instanceof Wrapper) {
				if (part.getModel() == ((Wrapper)editPart.getModel()).getElement()) {
					return editPart;
				}
			}
		}
		return null;
	}
	
	protected EditPart convertToTreeViewerPart(TreeViewer viewer, EditPart part) {
		return null;
	}
	
}
