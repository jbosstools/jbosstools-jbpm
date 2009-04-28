package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.ModelListener;

public class JpdlTreeEditPart extends AbstractTreeEditPart implements ModelListener {

	private IPropertySource propertySource;
	
	public JpdlTreeEditPart(Object model) {
		super(model);
	}

    @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
    	if (adapter == IPropertySource.class)
            return getPropertySource();
        return super.getAdapter(adapter);
    }
    
    private IPropertySource getPropertySource() {
    	if (propertySource == null) {
    		initPropertySource();
    	}
    	return propertySource;
    }
    
    private void initPropertySource() {
    	EditPart editPart = this;
    	while (editPart != null && propertySource == null) {
    		if (editPart.getModel() != null && editPart.getModel() instanceof IAdaptable) {
    			propertySource = (IPropertySource)((IAdaptable)editPart.getModel()).getAdapter(IPropertySource.class);
    		}
    		editPart = editPart.getParent();
    	}
    }

	public void modelChanged(ModelEvent event) {
	}
    
}
