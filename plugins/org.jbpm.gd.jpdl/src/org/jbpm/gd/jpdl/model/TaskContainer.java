package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.SemanticElement;

public interface TaskContainer extends SemanticElement {

	void addTask(Task task);
	void removeTask(Task task);
	Task[] getTasks();

}
