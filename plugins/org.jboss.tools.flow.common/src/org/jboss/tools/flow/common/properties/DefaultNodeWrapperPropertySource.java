package org.jboss.tools.flow.common.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jboss.tools.flow.common.wrapper.DefaultNodeWrapper;

public class DefaultNodeWrapperPropertySource extends WrapperPropertySource implements IPropertyId {
	
	private DefaultNodeWrapper wrapper = null;
	private IPropertyDescriptor[] propertyDescriptors;
	
	public DefaultNodeWrapperPropertySource(DefaultNodeWrapper wrapper) {
		super(wrapper);
		this.wrapper = wrapper;
	}
	
	public Object getEditableValue() {
		return super.getEditableValue();
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null) {
			propertyDescriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(NAME, "Name") {
					public String getCategory() {
						return "General";
					}
				}
			};
			propertyDescriptors = merge(propertyDescriptors, super.getPropertyDescriptors());
		}
		return propertyDescriptors;
	}
	
	public Object getPropertyValue(Object id) {
		if (NAME.equals(id)) {
			return wrapper.getName() != null ? wrapper.getName() : "";
		} else {
			return super.getPropertyValue(id);
		}
	}
	
	public boolean isPropertySet(Object id) {
		if (NAME.equals(id)) {
			return wrapper.getName() != null;
		} else  {
			return super.isPropertySet(id);
		}
	}
	
	public void resetPropertyValue(Object id) {
		super.resetPropertyValue(id);
	}
	
	public void setPropertyValue(Object id, Object value) {
		if (NAME.equals(id)) {
			if (value instanceof String) {
				wrapper.setName((String)value);
			}
		} else {
			super.setPropertyValue(id, value);
		}
	}
}
