package org.jboss.tools.flow.jpdl4.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;


public class QueryTask extends Task {
	
	public static final String VAR = "org.jboss.tools.flow.jpdl4.model.queryTask.var";
	public static final String UNIQUE = "org.jboss.tools.flow.jpdl4.model.queryTask.unique";
	public static final String QUERY = "org.jboss.tools.flow.jpdl4.model.queryTask.query";
	public static final String PARAMETERS = "org.jboss.tools.flow.jpdl4.model.queryTask.parameters";

	private String variableName;
	private String unique;
	private String query;
	private List<PrimitiveObject> parameters = new ArrayList<PrimitiveObject>();
	
	public QueryTask() {
		setMetaData("propertySource", new PropertySource());
	}
	
	protected boolean isPropagationExclusive() {
		return true;
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
				return variableName;
			} else if (UNIQUE.equals(id)) {
				return unique;
			} else if (QUERY.equals(id)) {
				return query;
			} else if (PARAMETERS.equals(id)) {
				return parameters;
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (VAR.equals(id)) {
				return variableName != null;
			} else if (UNIQUE.equals(id)) {
				return unique != null;
			} else if (QUERY.equals(id)) {
				return query != null;
			} else if (PARAMETERS.equals(id)) {
				return true;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (VAR.equals(id)) {
				variableName = (String)value;
			} else if (UNIQUE.equals(id)) {
				unique = (String)value;
			} else if (QUERY.equals(id)) {
				query = (String)value;
			}
		}
		
	}
}
