package org.jbpm.gd.jpdl.properties;

import org.eclipse.jface.viewers.IFilter;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.jpdl.model.Node;

public class NodeActionFilter implements IFilter {

	public boolean select(Object toTest) {
		Object input = toTest;
        if (toTest instanceof NotationElementGraphicalEditPart) {
        	AbstractNotationElement notationElement = ((NotationElementGraphicalEditPart)toTest).getNotationElement();
        	input = notationElement.getSemanticElement();
        } else if (toTest instanceof OutlineEditPart) {
        	input = ((OutlineEditPart)toTest).getModel();
        }
        if (input instanceof Node) {
        	return ((Node)input).isActionElementConfigurable();
        }
		return false;
	}

}
