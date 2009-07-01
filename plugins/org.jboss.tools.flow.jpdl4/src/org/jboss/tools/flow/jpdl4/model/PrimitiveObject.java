package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.DefaultElement;

public class PrimitiveObject extends DefaultElement {
	
	public static final String VALUE = "org.jboss.tools.flow.jpdl4.model.primitiveObject.value";
	
	private String value = "<string value=\"aValue\"/>";
	
	public PrimitiveObject() {
		setMetaData("propertySource", new PropertySource());
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
				return value;
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (VALUE.equals(id)) {
				return value != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object val) {
			if (VALUE.equals(id)) {
				value = (String)val;
			}
		}
		
	}
}
