package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.Element;

public class JpdlTreeEditPart extends AbstractTreeEditPart {

	protected IPropertySource propertySource;

	public JpdlTreeEditPart(Element element) {
		super(element);
		if (element == null) return;
		Object object = element.getMetaData("propertySource");
		if (object != null && object instanceof IPropertySource) {
			propertySource = (IPropertySource)object;
		}
	}

    @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
    	if (adapter == IPropertySource.class)
            return propertySource;
        return super.getAdapter(adapter);
    }
    
}
