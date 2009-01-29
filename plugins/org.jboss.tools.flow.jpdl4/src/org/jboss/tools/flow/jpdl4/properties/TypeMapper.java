package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.views.properties.tabbed.AbstractTypeMapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class TypeMapper extends AbstractTypeMapper {

    @SuppressWarnings("unchecked")
	public Class mapType(Object object) {
        if (object instanceof EditPart) {
        	Object wrapper = ((EditPart)object).getModel();
        	if (wrapper instanceof Wrapper) { 
        		Object element = ((Wrapper)wrapper).getElement();
        		if (element != null) {
        			return element.getClass();
        		}
        	}
        }
        return super.mapType(object);
    }

}
