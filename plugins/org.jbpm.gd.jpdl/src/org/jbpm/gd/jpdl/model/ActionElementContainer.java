package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.SemanticElement;

public interface ActionElementContainer extends SemanticElement {

	void addActionElement(ActionElement actionElement);
	void removeActionElement(ActionElement actionElement);
	ActionElement[] getActionElements();
	
}
