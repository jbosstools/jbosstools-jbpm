package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.SemanticElement;

public interface TimerContainer extends SemanticElement {

	void addTimer(Timer timer);
	void removeTimer(Timer timer);
	Timer[] getTimers();
	
}
