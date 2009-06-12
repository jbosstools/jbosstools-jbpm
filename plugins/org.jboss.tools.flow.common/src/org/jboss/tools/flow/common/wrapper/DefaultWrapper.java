package org.jboss.tools.flow.common.wrapper;

import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.properties.WrapperPropertySource;

public class DefaultWrapper extends AbstractWrapper {
	
	private WrapperPropertySource propertySource;
	
	protected IPropertySource getPropertySource() {
    	if (propertySource == null) {
    		propertySource = new WrapperPropertySource(this);
    	}
    	return propertySource;
	}

}
