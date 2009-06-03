package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.DefaultElement;

public class Argument extends DefaultElement {
	
	public static final String VALUE = "org.jboss.tools.flow.jpdl4.model.argument.value";
	
	private String value;
	
	public Argument() {
		setMetaData("propertySource", new PropertySource());
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private class PropertySource implements IPropertySource {
		
		public Object getEditableValue() {
			return null;
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			return new IPropertyDescriptor[0];
		}

		public Object getPropertyValue(Object id) {
			if (VALUE.equals(id)) {
				return getValue();
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (VALUE.equals(id)) {
				return getValue() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (VALUE.equals(id)) {
				setValue((String)value);
			}
		}
		
	}
}
