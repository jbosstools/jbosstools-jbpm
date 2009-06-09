package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.List;

public class AbstractTimerNode extends AbstractNode implements TimerContainer {

	public void addTimer(Timer timer) {
		timers.add(timer);
		firePropertyChange("timerAdd", null, timer);
	}
	
	public void removeTimer(Timer timer) {
		timers.remove(timer);
		firePropertyChange("timerRemove", timer, null);
	}
	
	public Timer[] getTimers() {
		return (Timer[])timers.toArray(new Timer[timers.size()]);
	}
	
	private List timers = new ArrayList();
}
