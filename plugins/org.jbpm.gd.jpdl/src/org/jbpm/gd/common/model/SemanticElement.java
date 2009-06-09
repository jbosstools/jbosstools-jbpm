package org.jbpm.gd.common.model;

import java.beans.PropertyChangeListener;

import org.eclipse.jface.resource.ImageDescriptor;

public interface SemanticElement {

	void addPropertyChangeListener(PropertyChangeListener listener);
	void removePropertyChangeListener(PropertyChangeListener listener);
	String getElementId();
	String getNamePrefix();
	String getLabel();
	ImageDescriptor getIconDescriptor();
	SemanticElementFactory getFactory();
	void setFactory(SemanticElementFactory factory);
	void initialize();
	
}
