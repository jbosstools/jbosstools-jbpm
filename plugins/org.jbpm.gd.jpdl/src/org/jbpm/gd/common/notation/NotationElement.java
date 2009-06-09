package org.jbpm.gd.common.notation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jbpm.gd.common.model.SemanticElement;

public interface NotationElement extends PropertyChangeListener {

	void setFactory(NotationElementFactory factory);
	NotationElementFactory getFactory();
	void setSemanticElement(SemanticElement semanticElement);
	SemanticElement getSemanticElement();
	void addPropertyChangeListener(PropertyChangeListener listener);
	void removePropertyChangeListener(PropertyChangeListener listener);
	void propertyChange(PropertyChangeEvent evt);
	void register();
	void unregister();
	AbstractNotationElement getRegisteredNotationElementFor(SemanticElement semanticElement);
}
