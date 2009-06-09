package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.CancelTimer;


public class CancelTimerDomAdapter extends XmlAdapter {

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		CancelTimer cancelTimer = (CancelTimer)jpdlElement;
		cancelTimer.setName(getAttribute("name"));
		cancelTimer.addPropertyChangeListener(this);
	}
	
	protected void initialize() {
		super.initialize();
		CancelTimer cancelTimer = (CancelTimer)getSemanticElement();
		if (cancelTimer != null) {
			setAttribute("name", cancelTimer.getName());
		}
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		CancelTimer event = (CancelTimer)getSemanticElement();
		if ("name".equals(name)) {
			event.setName(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		// an action cannot have any child nodes
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// an action cannot have any child nodes
	}
	
	
}
