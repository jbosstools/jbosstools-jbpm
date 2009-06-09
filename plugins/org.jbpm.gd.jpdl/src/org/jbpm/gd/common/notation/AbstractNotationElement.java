package org.jbpm.gd.common.notation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.jbpm.gd.common.model.SemanticElement;

public class AbstractNotationElement implements NotationElement {
	
	private NotationElementFactory factory;
	private SemanticElement semanticElement;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void setFactory(NotationElementFactory factory) {
		this.factory = factory;
	}
	
	public NotationElementFactory getFactory() {
		return factory;
	}
	
	public void setSemanticElement(SemanticElement semanticElement) {
		this.semanticElement = semanticElement;
	}
	
	public SemanticElement getSemanticElement() {
		return semanticElement;
	}

	protected void firePropertyChange(String name, Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(name, oldValue, newValue);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void propertyChange(PropertyChangeEvent evt) {
	}
	
	public void register() {
		getFactory().register(this);
	}
	
	public void unregister() {
		getFactory().unregister(this);
	}
	
	public AbstractNotationElement getRegisteredNotationElementFor(SemanticElement semanticElement) {
		return getFactory().getRegisteredNotationElementFor(semanticElement);
	}
}
