package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.DefaultElement;

public class Parameter extends DefaultElement {
	
	public static final String VAR = "org.jboss.tools.flow.jpdl4.model.parameter.var";
	public static final String SUBVAR = "org.jboss.tools.flow.jpdl4.model.parameter.subvar";
	public static final String EXPR = "org.jboss.tools.flow.jpdl4.model.parameter.expr";
	public static final String LANG = "org.jboss.tools.flow.jpdl4.model.parameter.lang";
	
	private String outerVariable;
	private String innerVariable;
	private String expression;
	private String language;
	
	public Parameter() {
		setMetaData("propertySource", new PropertySource());
	}

	public String getOuterVariable() {
		return outerVariable;
	}

	public void setOuterVariable(String outerVariable) {
		this.outerVariable = outerVariable;
	}

	public String getInnerVariable() {
		return innerVariable;
	}

	public void setInnerVariable(String innerVariable) {
		this.innerVariable = innerVariable;
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

	private class PropertySource implements IPropertySource {
		
		public Object getEditableValue() {
			return null;
		}

		public IPropertyDescriptor[] getPropertyDescriptors() {
			return new IPropertyDescriptor[0];
		}

		public Object getPropertyValue(Object id) {
			if (VAR.equals(id)) {
				return getOuterVariable();
			} else if (SUBVAR.equals(id)) {
				return getInnerVariable();
			} else if (EXPR.equals(id)) {
				return getExpression();
			} else if (LANG.equals(id)) {
				return getLanguage();
			}
			return null;
		}

		public boolean isPropertySet(Object id) {
			if (VAR.equals(id)) {
				return getOuterVariable() != null;
			} else if (SUBVAR.equals(id)) {
				return getInnerVariable() != null;
			} else if (EXPR.equals(id)) {
				return getExpression() != null;
			} else if (LANG.equals(id)) {
				return getLanguage() != null;
			}
			return false;
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (VAR.equals(id)) {
				setOuterVariable((String)value);
			} else if (SUBVAR.equals(id)) {
				setInnerVariable((String)value);
			} else if (EXPR.equals(id)) {
				setExpression((String)value);
			} else if (LANG.equals(id)) {
				setLanguage((String)value);
			}
		}
		
	}
}
