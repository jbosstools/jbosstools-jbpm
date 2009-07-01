package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.DefaultElement;

public class Field extends DefaultElement {
	
	public static final String NAME = "org.jboss.tools.flow.jpdl4.model.field.name";
	public static final String VALUE = "org.jboss.tools.flow.jpdl4.model.field.value";
	
	private String value = "<string value=\"aValue\"/>";
	private String name;
	
	public Field() {
		setMetaData("propertySource", new PropertySource());
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
			} else if (NAME.equals(id)) {
				return getName();
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (VALUE.equals(id)) {
				return getValue() != null;
			} else if (NAME.equals(id)) {
				return getName() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (VALUE.equals(id)) {
				setValue((String)value);
			} else if (NAME.equals(id)) {
				setName((String)value);
			}
		}
		
	}
}
