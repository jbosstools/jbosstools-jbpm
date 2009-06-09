package org.jbpm.gd.pf.xml;

import java.beans.PropertyChangeEvent;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.pf.model.Transition;

public class TransitionDomAdapter extends XmlAdapter {
	
	protected void initialize() {
		super.initialize();
		Transition transition = (Transition)getSemanticElement();
		if (transition != null) {
			setAttribute("name", transition.getName());
			setAttribute("to", transition.getTo());
		}
	}

	public void initialize(SemanticElement semanticElement) {
		super.initialize(semanticElement);
		Transition transition = (Transition)semanticElement;
		transition.setName(getAttribute("name"));
		transition.setTo(getAttribute("to"));
		transition.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("to".equals(evt.getPropertyName())) {
			setAttribute("to", (String)evt.getNewValue());
		} else  if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Transition transition = (Transition)getSemanticElement();
		if ("name".equals(name)) {
			transition.setName(newValue);
		} else if ("to".equals(name)) {
			transition.setTo(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
	}
	
	protected void doModelRemove(XmlAdapter child) {
	}


}
