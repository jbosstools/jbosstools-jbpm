package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public class ForeachParallelGateway extends ParallelGateway {

	public static final String VAR = "org.jboss.tools.flow.jpdl4.model.forEachParallelGateway.var";
	public static final String IN = "org.jboss.tools.flow.jpdl4.model.forEachParallelGateway.in";
	
	private String variableName;
	private String inCollection;
	
	public ForeachParallelGateway() {
		setMetaData("propertySource", new PropertySource());
	}
	
	
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public String getVariableName() {
		return variableName;
	}
	public void setInCollection(String inCollection) {
		this.inCollection = inCollection;
	}
	public String getInCollection() {
		return inCollection;
	}
	private class PropertySource implements IPropertySource {
		
		public Object getEditableValue() {
			return null;
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			return new IPropertyDescriptor[0];
		}

		public Object getPropertyValue(Object id) {
			if (VAR.equals(id)) {
				return getVariableName();
			} else if (IN.equals(id)) {
				return getInCollection();
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (VAR.equals(id)) {
				return getVariableName() != null;
			} else if (IN.equals(id)) {
				return getInCollection() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (VAR.equals(id)) {
				setVariableName((String)value);
			} else if (IN.equals(id)) {
				setInCollection((String)value);
			}
		}
		
	}
}
