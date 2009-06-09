package org.jbpm.gd.jpdl.model;

public class Assignment extends Delegation {
	
	private String expression;
	private String actorId;
	private String pooledActors;
	
	public void setExpression(String newExpression) {
		String oldExpression = expression;
		expression = newExpression == null ? null : newExpression.trim();
		firePropertyChange("expression", oldExpression, expression);
	}
	
	public String getExpression() {
		return expression;
	}
	
	public void setActorId(String newActorId) {
		String oldActorId = actorId;
		actorId = newActorId;
		firePropertyChange("actorId", oldActorId, newActorId);
	}
	
	public String getActorId() {
		return actorId;
	}
	
	public void setPooledActors(String newPooledActors) {
		String oldPooledActors = pooledActors;
		pooledActors = newPooledActors;
		firePropertyChange("pooledActors", oldPooledActors, newPooledActors);
	}
	
	public String getPooledActors() {
		return pooledActors;
	}

}
