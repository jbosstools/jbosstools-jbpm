package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.SemanticElement;

public interface DescribableElement extends SemanticElement {
	
	public void setDescription(Description description);
	public Description getDescription();

}
