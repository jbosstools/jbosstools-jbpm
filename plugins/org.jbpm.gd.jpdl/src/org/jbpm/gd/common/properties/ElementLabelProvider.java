package org.jbpm.gd.common.properties;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.util.SharedImages;

public class ElementLabelProvider implements ILabelProvider {
	
	private SemanticElement getJpdlElement(Object element) {
		SemanticElement result = null;
		if (element instanceof IStructuredSelection) {
			element = ((IStructuredSelection)element).getFirstElement();
		}
        if (element instanceof NotationElementGraphicalEditPart) {
        	result = (SemanticElement)((NotationElementGraphicalEditPart)element).getNotationElement().getSemanticElement();
        } else if (element instanceof OutlineEditPart) {
        	result = (SemanticElement)((OutlineEditPart)element).getModel();
        }
        return result;
	}
		
	public Image getImage(Object element) {
		Image result = null;
		SemanticElement jpdlElement = getJpdlElement(element);
		if (jpdlElement != null) {
			result = SharedImages.INSTANCE.getImage(jpdlElement.getIconDescriptor());
		}
		return result;
	}

	public String getText(Object element) {
		String result = element.toString();
		SemanticElement jpdlElement = getJpdlElement(element);
		if (jpdlElement != null) {
			result = jpdlElement.getLabel();
			return result != null ? result : jpdlElement.getElementId();
		}
		return result;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

}
