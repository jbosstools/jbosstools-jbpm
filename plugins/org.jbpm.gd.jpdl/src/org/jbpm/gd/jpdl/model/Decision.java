package org.jbpm.gd.jpdl.model;


public class Decision extends AbstractNode implements AsyncableElement {
	
	private Handler handler;
	private String expression;
	
	private String async;
	
	public void setAsync(String newAsync) {
		String oldAsync = async;
		async = newAsync;
		firePropertyChange("async", oldAsync, newAsync);
	}
	
	public String getAsync() {
		return async;
	}
	
	public void setHandler(Handler newHandler) {
		Handler oldHandler = handler;
		handler = newHandler;
		firePropertyChange("handler", oldHandler, newHandler);
	}
	
	public Handler getHandler() {
		return handler;
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
