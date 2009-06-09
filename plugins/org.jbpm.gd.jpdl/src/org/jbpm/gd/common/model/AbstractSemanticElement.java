package org.jbpm.gd.common.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.jface.resource.ImageDescriptor;


public abstract class AbstractSemanticElement implements SemanticElement {
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private String elementId;
	private String namePrefix;
	private String label;
	private ImageDescriptor iconDescriptor;
	private SemanticElementFactory factory;
	
	protected void firePropertyChange(String name, Object oldValue, Object newValue) {
		if ((oldValue == newValue) || (oldValue != null && oldValue.equals(newValue))) return;
		propertyChangeSupport.firePropertyChange(name, oldValue, newValue);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	void setElementId(String id) {
		this.elementId = id;
	}
	
	public String getElementId() {
		return elementId;
	}
	
	void setNamePrefix(String prefix) {
		namePrefix = prefix;
	}
	
	public String getNamePrefix() {
		return namePrefix == null ? elementId : namePrefix;
	}
	
	void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	void setIconDescriptor(ImageDescriptor iconDescriptor) {
		this.iconDescriptor = iconDescriptor;
	}
	
	public ImageDescriptor getIconDescriptor() {
		return iconDescriptor;
	}
	
	public void setFactory(SemanticElementFactory factory) {
		this.factory = factory;
	}
	
	public SemanticElementFactory getFactory() {
		return factory;
	}
	
	public void initialize() {
	}
	
}
