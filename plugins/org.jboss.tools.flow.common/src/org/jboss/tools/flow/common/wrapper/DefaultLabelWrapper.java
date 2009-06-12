package org.jboss.tools.flow.common.wrapper;

import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.properties.IPropertyId;
import org.jboss.tools.flow.common.properties.WrapperPropertySource;

public class DefaultLabelWrapper extends AbstractLabelWrapper implements ModelListener {
	
	private IPropertySource propertySource;
	
	public DefaultLabelWrapper(Wrapper owner) {
		if (owner != null) {
			setElement(owner.getElement());
		}
	}
	
	public void setText(String text) {
		getPropertySource().setPropertyValue(IPropertyId.LABEL, text);
	}
	
	public String getText() {
		return (String)getPropertySource().getPropertyValue(IPropertyId.LABEL);
	}
	
	public void modelChanged(ModelEvent event) {
		notifyListeners(event);
	}
    
    protected IPropertySource getPropertySource() {
    	if (propertySource == null) {
    		propertySource = new WrapperPropertySource(this);
    	}
    	return propertySource;
    }
    
    @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
    	if (adapter == IPropertySource.class) {
    		return this;
    	}
    	return super.getAdapter(adapter);
    }
    
}
