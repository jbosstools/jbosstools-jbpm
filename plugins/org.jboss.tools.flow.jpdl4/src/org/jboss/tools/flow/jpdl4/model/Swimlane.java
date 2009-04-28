package org.jboss.tools.flow.jpdl4.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jboss.tools.flow.common.model.DefaultElement;
import org.jboss.tools.flow.common.properties.IPropertyId;


public class Swimlane extends DefaultElement {
	
	private String name;

	public Swimlane() {
		setMetaData("propertySource", new PropertySource());		
	}	
	
	public void setName(String newName) {
		name = newName;
	}
	
	public String getName() {
		return name;
	}
	
	private class PropertySource extends AssignmentPropertySource implements IPropertyId {
		
		private IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(NAME, "Name") {
					public String getCategory() {
						return "General";
					}
				}
		};

		public Object getEditableValue() {
			return super.getEditableValue();
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			IPropertyDescriptor[] parentDescriptors = super.getPropertyDescriptors();
			int size = propertyDescriptors.length + parentDescriptors.length;
			ArrayList<IPropertyDescriptor> list = new ArrayList<IPropertyDescriptor>(size);
			list.addAll(Arrays.asList(propertyDescriptors));
			list.addAll(Arrays.asList(parentDescriptors));
			return (IPropertyDescriptor[]) list.toArray();
		}

		public Object getPropertyValue(Object id) {
			if (NAME.equals(id)) {
				return getName() != null ? getName() : "";
			} else {
				return super.getPropertyValue(id);
			}
		}

		public boolean isPropertySet(Object id) {
			if (NAME.equals(id)) {
				return getName() != null;
			} else {
				return super.isPropertySet(id);
			}
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (NAME.equals(id)) {
				if (value instanceof String) {
					setName((String)value);
				}
			} else {
				super.setPropertyValue(id, value);
			}
		}
		
	}
	
}
