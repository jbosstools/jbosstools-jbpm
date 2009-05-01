package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jboss.tools.flow.common.model.DefaultElement;

public class EventListener extends DefaultElement {
	
	public static final String CLASS_NAME = "org.jboss.tools.flow.jpdl4.model.eventListener.className";
	
	private String className;
	
	public EventListener() {
		setMetaData("propertySource", new PropertySource());
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	private class PropertySource implements IPropertySource {
		
		private IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(CLASS_NAME, "Class") {
					public String getCategory() {
						return "General";
					}
				}
		};

		public Object getEditableValue() {
			return null;
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			return propertyDescriptors;
		}

		public Object getPropertyValue(Object id) {
			if (CLASS_NAME.equals(id)) {
				return getClassName() != null ? getClassName() : "";
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (CLASS_NAME.equals(id)) {
				return getClassName() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (CLASS_NAME.equals(id)) {
				if (value == null || value instanceof String) {
					setClassName((String)value);
				}
			}
		}
		
	}
}
