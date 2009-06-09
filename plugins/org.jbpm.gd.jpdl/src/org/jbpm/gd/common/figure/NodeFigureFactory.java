package org.jbpm.gd.common.figure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.notation.NodeContainer;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.notation.NotationMapping;

public class NodeFigureFactory {
	
	public static NodeFigureFactory INSTANCE = new NodeFigureFactory();
	
	private NodeFigureFactory() {
	}
	
	public IFigure createFigure(NotationElement notationElement) {
		if (notationElement instanceof NodeContainer) {
			return new NodeContainerFigure();
		}
		SemanticElement semanticElement = notationElement.getSemanticElement();
		String label = null;
		ImageDescriptor imageDescriptor = null;
		if (semanticElement instanceof SemanticElement) {
			imageDescriptor = ((SemanticElement)semanticElement).getIconDescriptor();
			label = ((SemanticElement)semanticElement).getLabel();
		}
		if (imageDescriptor == null) return null;
		if (label != null) {
			return new NodeFigure(label, NotationMapping.hideName(semanticElement.getElementId()), imageDescriptor);
		} else {
			return null;
		}
	}

}
