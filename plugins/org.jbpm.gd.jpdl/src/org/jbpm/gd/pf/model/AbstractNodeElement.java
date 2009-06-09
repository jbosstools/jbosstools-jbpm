package org.jbpm.gd.pf.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNodeElement extends AbstractNamedElement implements
		NodeElement {
	
	private List transitions = new ArrayList();
	private String viewId;
	
	public void setViewId(String newViewId) {
		String oldViewId = this.viewId;
		this.viewId = newViewId;
		firePropertyChange("viewId", oldViewId, newViewId);
	}
	
	public String getViewId() {
		return viewId;
	}

	public void addTransition(Transition transition) {
		transitions.add(transition);
		firePropertyChange("transitionAdd", null, transition);
	}
	
	public void removeTransition(Transition transition) {
		transitions.remove(transition);
		firePropertyChange("transitionRemove", transition, null);
	}
	
	public Transition[] getTransitions() {
		return (Transition[])transitions.toArray(new Transition[transitions.size()]);
	}
	

	public void initializeName(PageFlowDefinition pageFlowDefinition) {
		int runner = 1;
		String prefix = getNamePrefix();
		while (true) {
			String candidate = prefix + runner;
			if (pageFlowDefinition.getNodeElementByName(candidate) == null) {
				setName(candidate);
				return;
			}
			runner ++;
		}
	}
}
