package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.AbstractSemanticElement;

public class Condition extends AbstractSemanticElement {
	
	private String script;
	private String expression;
	
	public void setScript(String newScript) {
		String oldScript = script;
		script = newScript;
		firePropertyChange("script", oldScript, newScript);
	}
	
	public String getScript() {
		return script;
	}
	
	public void setExpression(String newExpression) {
		String oldExpression = expression;
		expression = newExpression == null ? null : newExpression.trim();
		firePropertyChange("expression", oldExpression, expression);
	}
	
	public String getExpression() {
		return expression;
	}

}
