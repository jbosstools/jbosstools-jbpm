package org.jbpm.gd.pf.model;

import org.jbpm.gd.common.model.NamedElement;

public interface NodeElement extends NamedElement{
	
	void setViewId(String viewId);
	String getViewId();
	void addTransition(Transition transition);
	void removeTransition(Transition transition);
	Transition[] getTransitions();
	void initializeName(PageFlowDefinition pageFlowDefinition);

}
