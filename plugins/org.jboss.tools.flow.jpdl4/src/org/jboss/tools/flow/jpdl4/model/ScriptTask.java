package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;


public class ScriptTask extends Task {
	
	public static final String EXPR = "org.jboss.tools.flow.jpdl4.model.scriptTask.expr";
	public static final String LANG = "org.jboss.tools.flow.jpdl4.model.scriptTask.lang";
	public static final String VAR = "org.jboss.tools.flow.jpdl4.model.scriptTask.var";
	public static final String TEXT = "org.jboss.tools.flow.jpdl4.model.scriptTask.text";
	
	private String expression;
	private String language;
	private String variableName;
	private String text;
	
	public ScriptTask() {
		setMetaData("propertySource", new PropertySource());		
	}
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
			if (EXPR.equals(id)) {
				return getExpression();
			} else if (LANG.equals(id)) {
				return getLanguage();
			} else if (VAR.equals(id)) {
				return getVariableName();
			} else if (TEXT.equals(id)) {
				return getText();
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (EXPR.equals(id)) {
				return getExpression() != null;
			} else if (LANG.equals(id)) {
				return getLanguage() != null;
			} else if (VAR.equals(id)) {
				return getVariableName() != null;
			} else if (TEXT.equals(id)) {
				return getText() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (EXPR.equals(id)) {
				setExpression((String)value);
			} else if (LANG.equals(id)) {
				setLanguage((String)value);
			} else if (VAR.equals(id)) {
				setVariableName((String)value);
			} else if (TEXT.equals(id)) {
				setText((String)value);
			}
		}
		
	}
}
