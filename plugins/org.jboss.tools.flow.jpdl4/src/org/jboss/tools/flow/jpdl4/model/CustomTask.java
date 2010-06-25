package org.jboss.tools.flow.jpdl4.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;


public class CustomTask extends Task {
	
	public static final String CLASS = "org.jboss.tools.flow.jpdl4.model.customTask.class";
	
	private String className;
	
	private List<Field> fields = new ArrayList<Field>();
	
	public CustomTask() {
		setMetaData("propertySource", new PropertySource());
	}
	
	protected boolean isPropagationExclusive() {
		return true;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public List<Field> getFields() {
		return fields;
	}
	private class PropertySource implements IPropertySource {
		
		public Object getEditableValue() {
			return null;
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			return new IPropertyDescriptor[0];
		}

		public Object getPropertyValue(Object id) {
			if (CLASS.equals(id)) {
				return getClassName();
			} else if (Field.FIELDS.equals(id)) {
				return fields;
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (CLASS.equals(id)) {
				return getClassName() != null;
			} else if (Field.FIELDS.equals(id)) {
				return true;
			} 
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (CLASS.equals(id)) {
				setClassName((String)value);
			} 
		}
		
	}
}
