package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Text;

public class TextDomAdapter extends XmlAdapter {
	
	protected void initialize() {
		super.initialize();
		Text text = (Text)getSemanticElement();
		if (text != null) {
			setTextContent(text.getText());
		}
	}

	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Text text = (Text)jpdlElement;
		text.setText(getTextContent());
		text.addPropertyChangeListener(this);
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("text".equals(evt.getPropertyName())) {
			setTextContent((String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Text text = (Text)getSemanticElement();
		if ("#text".equals(name)) {
			text.setText(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		// a text cannot have any child nodes
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// a text cannot have any child nodes
	}
	
}
