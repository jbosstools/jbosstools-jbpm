package org.jboss.tools.flow.jpdl4.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;


public class JavaTask extends Task {
	
	public static final String CLASS = "org.jboss.tools.flow.jpdl4.model.javaTask.class";
	public static final String METHOD = "org.jboss.tools.flow.jpdl4.model.javaTask.method";
	public static final String VAR = "org.jboss.tools.flow.jpdl4.model.javaTask.var";
	public static final String FIELDS = "org.jboss.tools.flow.jpdl4.model.javaTask.fields";
	public static final String ARGS = "org.jboss.tools.flow.jpdl4.model.javaTask.args";
	
	private String className;
	private String methodName;
	private String variableName;
	private List<Argument> arguments = new ArrayList<Argument>();
	private List<Field> fields = new ArrayList<Field>();
	
	public JavaTask() {
		setMetaData("propertySource", new PropertySource());
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
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
			if (CLASS.equals(id)) {
				return getClassName();
			} else if (METHOD.equals(id)) {
				return getMethodName();
			} else if (VAR.equals(id)) {
				return getVariableName();
			} else if (FIELDS.equals(id)) {
				return fields;
			} else if (ARGS.equals(id)) {
				return arguments;
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (CLASS.equals(id)) {
				return getClassName() != null;
			} else if (METHOD.equals(id)) {
				return getMethodName() != null;
			} else if (VAR.equals(id)) {
				return getVariableName() != null;
			} else if (FIELDS.equals(id)) {
				return true;
			} else if (ARGS.equals(id)) {
				return true;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (CLASS.equals(id)) {
				setClassName((String)value);
			} else if (METHOD.equals(id)) {
				setMethodName((String)value);
			} else if (VAR.equals(id)) {
				setVariableName((String)value);
			}
		}
		
	}
}
