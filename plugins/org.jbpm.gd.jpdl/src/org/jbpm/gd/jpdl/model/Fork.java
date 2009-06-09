package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.List;

public class Fork extends AbstractAsyncableTimerNode {
	
	private List scripts = new ArrayList();
	
	public void addScript(Script script) {
		scripts.add(script);
		firePropertyChange("scriptAdd", null, script);
	}
	
	public void removeScript(Script script) {
		scripts.remove(script);
		firePropertyChange("scriptRemove", script, null);
	}
	
	public Script[] getScripts() {
		return (Script[])scripts.toArray(new Script[scripts.size()]);
	}
	
}
