package org.jbpm.gd.jpdl.model;


public class StartState extends AbstractNode {
	
	private Task task;
	
	public void setTask(Task newTask) {
		Task oldTask = task;
		task = newTask;
		firePropertyChange("task", oldTask, newTask);
	}
	
	public Task getTask() {
		return task;
	}
	
	public boolean isPossibleChildOf(NodeElementContainer nodeElementContainer) {
		return nodeElementContainer instanceof ProcessDefinition && ((ProcessDefinition)nodeElementContainer).getStartState() == null;
	}
	
	public void initializeName(ProcessDefinition processDefinition) {
		setName(getNamePrefix());
	}

}
