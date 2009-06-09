package org.jbpm.gd.jpdl.model;


public class EndState extends AbstractNode {
	
	
	public void addTransition(Transition transition) {
		// No transitions can be added to a decision node
	}
	
	public void removeTransition(Transition transition) {
		// No transitions can be added to a decision node
	}
	
	public Transition[] getTransitions() {
		// No transitions can be added to a decision node
		return new Transition[0];
	}

}
