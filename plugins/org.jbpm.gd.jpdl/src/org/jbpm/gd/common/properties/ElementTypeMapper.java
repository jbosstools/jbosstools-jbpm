package org.jbpm.gd.common.properties;

import org.eclipse.ui.views.properties.tabbed.ITypeMapper;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;

public class ElementTypeMapper
	implements ITypeMapper {

	public Class mapType(Object object) {
		Class type = object.getClass();
		if (object instanceof NotationElementGraphicalEditPart) {
			Object notationElement = ((NotationElementGraphicalEditPart) object).getModel();		
			type = ((AbstractNotationElement)notationElement).getSemanticElement().getClass();
		} else if (object instanceof OutlineEditPart) {
			type = ((OutlineEditPart)object).getModel().getClass();
		}
		return type;
	}
}