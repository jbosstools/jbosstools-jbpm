package org.jboss.tools.flow.jpdl4.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;


public class ExclusiveGateway extends Gateway {
	
	public static final String LANG = "lang";
	public static final String EXPR = "expr";
	public static final String HANDLER = "handler";
	
	private String lang;
	private String expr;
	private String handler;
	
	public ExclusiveGateway() {
		setMetaData("propertySource", new PropertySource());
	}

	protected boolean isPropagationExclusive() {
		return true;
	}
	
	public String getLang() {
		return lang;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String getExpr() {
		return expr;
	}
	
	public void setExpr(String expr) {
		this.expr = expr;
	}
	
	public String getHandler() {
		return handler;
	}
	
	public void setHandler(String handler) {
		this.handler = handler;
	}
	
	private class PropertySource implements IPropertySource {
		
		public IPropertyDescriptor[] getPropertyDescriptors() {
			return new IPropertyDescriptor[0];
		}

		public Object getPropertyValue(Object id) {
			if (LANG.equals(id)) {
				return getLang();
			} else if (EXPR.equals(id)) {
				return getExpr();
			} else if (HANDLER.equals(id)) {
				return getHandler();
			} else {
				return null;
			}
		}

		public boolean isPropertySet(Object id) {
			if (LANG.equals(id)) {
				return getName() != null;
			} else if (EXPR.equals(id)) {
				return getExpr() != null;
			} else if (HANDLER.equals(id)) {
				return getHandler() != null;
			} else {
				return false;
			}
		}

		public void resetPropertyValue(Object id) {
		}

		public void setPropertyValue(Object id, Object value) {
			if (LANG.equals(id)) {
				setLang((String)value);
			} else if (EXPR.equals(id)) {
				setExpr((String)value);
			} else if (HANDLER.equals(id)) {
				setHandler((String)value);
			}
		}

		public Object getEditableValue() {
			return null;
		}
		
	}
}
