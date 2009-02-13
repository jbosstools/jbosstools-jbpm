package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.views.properties.tabbed.AbstractTypeMapper;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class TypeMapper extends AbstractTypeMapper {

    @SuppressWarnings("unchecked")
	public Class mapType(Object object) {
        if (object instanceof EditPart) {
        	Object model = ((EditPart)object).getModel();
        	if (model instanceof Wrapper) { 
        		Object element = ((Wrapper)model).getElement();
        		if (element != null) {
        			return element.getClass();
        		}
        	} else if (model instanceof Element) {
        		return model.getClass();
        	}
        }
        return super.mapType(object);
    }

}
