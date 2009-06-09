package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.List;

public class TaskNode extends AbstractAsyncableTimerNode implements TaskContainer {

	private List tasks = new ArrayList();
	private String createTasks;
	private String endTasks;
	private String signal;
	
	public void addTask(Task task) {
		tasks.add(task);
		firePropertyChange("taskAdd", null, task);
	}
	
	public void removeTask(Task task) {
		tasks.remove(task);
		firePropertyChange("taskRemove", task, null);
	}
	
	public Task[] getTasks() {
		return (Task[])tasks.toArray(new Task[tasks.size()]);
	}

	public void setCreateTasks(String newCreateTasks) {
		String oldCreateTasks = createTasks;
		createTasks = newCreateTasks;
		firePropertyChange("createTasks", oldCreateTasks, newCreateTasks);
	}
	
	public String getCreateTasks() {
		return createTasks;
	}
	
	public void setEndTasks(String newEndTasks) {
		String oldEndTasks = endTasks;
		endTasks = newEndTasks;
		firePropertyChange("endTasks", oldEndTasks, newEndTasks);
	}
	
	public String getEndTasks() {
		return endTasks;
	}
	
	public void setSignal(String newSignal) {
		String oldSignal = signal;
		signal = newSignal;
		firePropertyChange("signal", oldSignal, newSignal);
	}
	
	public String getSignal() {
		return signal;
	}

}
