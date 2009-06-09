package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Condition;

public class ConditionDomAdapter extends XmlAdapter {
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Condition condition = (Condition)jpdlElement;
		condition.setScript(getTextContent());
		condition.setExpression(getAttribute("expression"));
		condition.addPropertyChangeListener(this);
	}
	
	protected void initialize() {
		super.initialize();
		Condition condition = (Condition)getSemanticElement();
		if (condition != null) {
			setTextContent(condition.getScript());
			setAttribute("expression", condition.getExpression());
		}
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("script".equals(evt.getPropertyName())) {
			setTextContent((String)evt.getNewValue());
		} else if ("expression".equals(evt.getPropertyName())) {
			setAttribute("expression", (String)evt.getNewValue());
		}
	}	
	
	protected void doModelUpdate(String name, String newValue) {
		Condition condition = (Condition)getSemanticElement();
		if ("#text".equals(name)) {
			condition.setScript(newValue);
		} else if ("expression".equals(name)) {
			condition.setExpression(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		// a controller cannot have any child nodes
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// a controller cannot have any child nodes
	}
	
}
