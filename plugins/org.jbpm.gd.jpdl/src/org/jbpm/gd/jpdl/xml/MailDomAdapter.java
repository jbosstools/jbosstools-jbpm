package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.MailAction;
import org.jbpm.gd.jpdl.model.Subject;
import org.jbpm.gd.jpdl.model.Text;

public class MailDomAdapter extends XmlAdapter {

	private static final String[] CHILD_ELEMENTS = {"subject", "text"};
	private static HashMap NODE_TYPES = null;
	
	protected String[] getChildElements() {
		return CHILD_ELEMENTS;
	}
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("subject", "subject");
			NODE_TYPES.put("text", "text");
		}
		return NODE_TYPES;
	}
	
	protected String getDefaultValue(String attributeName) {
		if ("async".equals(attributeName)) {
			return "false";
		} else {
			return super.getDefaultValue(attributeName);
		}
	}
	protected void initialize() {
		super.initialize();
		MailAction mail = (MailAction)getSemanticElement();
		if (mail != null) {
			setAttribute("async", mail.getAsync());
			setAttribute("template", mail.getTemplate());
			setAttribute("actors", mail.getActors());
			setAttribute("to", mail.getTo());
			setAttribute("name", mail.getName());
			addElement(mail.getSubject());
			addElement(mail.getText());
		}
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		MailAction mail = (MailAction)jpdlElement;
		mail.setAsync(getAttribute("async"));
		mail.setTemplate(getAttribute("template"));
		mail.setActors(getAttribute("actors"));
		mail.setTo(getAttribute("to"));
		mail.setName(getAttribute("name"));
		mail.addPropertyChangeListener(this);
	}

	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("subject".equals(evt.getPropertyName())) {
			setElement("subject", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("text".equals(evt.getPropertyName())) {
			setElement("text", (SemanticElement)evt.getOldValue(), (SemanticElement)evt.getNewValue());
		} else if ("async".equals(evt.getPropertyName())) {
			setAttribute("async", (String)evt.getNewValue());
		} else if ("template".equals(evt.getPropertyName())) {
			setAttribute("template", (String)evt.getNewValue());
		} else if ("actors".equals(evt.getPropertyName())) {
			setAttribute("actors", (String)evt.getNewValue());
		} else if ("to".equals(evt.getPropertyName())) {
			setAttribute("to", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		}
	}

	protected void doModelUpdate(String name, String newValue) {
		MailAction mail = (MailAction)getSemanticElement();
		if ("async".equals(name)) {
			mail.setAsync(newValue);
		} else if ("template".equals(name)) {
			mail.setTemplate(newValue);
		} else if ("actors".equals(name)) {
			mail.setActors(newValue);
		} else if ("to".equals(name)) {
			mail.setTo(newValue);
		} else if ("name".equals(name)) {
			mail.setName(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		MailAction mail = (MailAction)getSemanticElement();
		if ("subject".equals(type)) {
			mail.setSubject((Subject)jpdlElement);
		} else if ("text".equals(type)) {
			mail.setText((Text)jpdlElement);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		String type = child.getElementType();
		MailAction mail = (MailAction)getSemanticElement();
		if ("subject".equals(type)) {
			mail.setSubject(null);
		} else if ("text".equals(type)) {
			mail.setText(null);
		} else if ("script".equals(type)) {
			mail.setText(null);
		}
	}
	
}
