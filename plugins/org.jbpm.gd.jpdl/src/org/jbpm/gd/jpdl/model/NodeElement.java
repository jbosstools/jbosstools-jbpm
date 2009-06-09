package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.NamedElement;

public interface NodeElement extends NamedElement, EventContainer, ExceptionHandlerContainer {

	void addTransition(Transition transition);
	void removeTransition(Transition transition);
	Transition[] getTransitions();
	
	boolean isPossibleChildOf(NodeElementContainer nodeElementContainer);
	
}
