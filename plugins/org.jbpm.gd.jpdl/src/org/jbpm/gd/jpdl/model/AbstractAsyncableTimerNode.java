package org.jbpm.gd.jpdl.model;

public class AbstractAsyncableTimerNode extends AbstractTimerNode implements AsyncableElement {

	private String async;
	
	public void setAsync(String newAsync) {
		String oldAsync = async;
		async = newAsync;
		firePropertyChange("async", oldAsync, newAsync);
	}
	
	public String getAsync() {
		return async;
	}
	
}
