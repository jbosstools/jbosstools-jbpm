package org.jbpm.gd.common.editor;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.ui.PlatformUI;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;

public class SelectionSynchronizer extends org.eclipse.gef.ui.parts.SelectionSynchronizer {
	
	protected EditPart convert(EditPartViewer viewer, EditPart part) {
		if (viewer instanceof GraphicalViewer && part instanceof OutlineEditPart) {
			part = getSelectablePartInGraphicalViewer(part);
			NotationElement notationElement = ((GraphicalViewer)viewer).getEditor().getNotationElementFactory().getRegisteredNotationElementFor(part.getModel());
			if (notationElement != null) {
				return (EditPart)viewer.getEditPartRegistry().get(notationElement);
			}
		}
		if (viewer instanceof TreeViewer && part instanceof NotationElementGraphicalEditPart) {
			Object semanticElement = ((AbstractNotationElement)part.getModel()).getSemanticElement();
			if (semanticElement != null) {
				EditPart result = (EditPart)viewer.getEditPartRegistry().get(semanticElement);
				while (result != null && result.getParent() != null && result.getParent().getModel() == semanticElement) {
					result = result.getParent();
				}
				return result; 
			}
		}
		return null;
	}
	
	private EditPart getSelectablePartInGraphicalViewer(EditPart part) {
		while (!isSelectablePartInGraphicalViewer(part)) {
			if (part.getParent() == null) break;
			part = part.getParent();
		}
		return part;
	}
	
	protected boolean isSelectablePartInGraphicalViewer(EditPart part) {
		return true;
	}
	
}
