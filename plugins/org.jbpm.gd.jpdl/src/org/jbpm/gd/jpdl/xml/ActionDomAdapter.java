package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.GenericElement;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.Action;

public class ActionDomAdapter extends XmlAdapter {
	
	private static HashMap NODE_TYPES = null;
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("genericElement", "genericElement");
		}
		return NODE_TYPES;
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Action action = (Action)jpdlElement;
		action.setConfigInfo(getTextContent());
		action.setClassName(getAttribute("class"));
		action.setConfigType(getAttribute("config-type"));
		action.setName(getAttribute("name"));
		action.setRefName(getAttribute("ref-name"));
		action.setAcceptPropagatedEvents(getAttribute("accept-propagated-events"));
		action.setExpression(getAttribute("expression"));
		action.setAsync(getAttribute("async"));
		action.addPropertyChangeListener(this);
	}
	
	protected void initialize() {
		super.initialize();
		Action action = (Action)getSemanticElement();
		if (action != null) {
			GenericElement[] genericElements = action.getGenericElements();
			for (int i = 0; i < genericElements.length; i++) {
				addElement(genericElements[i]);
			}
			setTextContent(action.getConfigInfo());
			setAttribute("class", action.getClassName());
			setAttribute("config-type", action.getConfigType());
			setAttribute("name", action.getName());
			setAttribute("ref-name", action.getRefName());
			setAttribute("accept-propagated-events", action.getAcceptPropagatedEvents());
			setAttribute("expression", action.getExpression());
			setAttribute("async", action.getAsync());
		}
	}

	protected String getDefaultValue(String attributeName) {
		if ("accept-propagated-events".equals(attributeName)) {
			return "true";
		} else if ("async".equals(attributeName)) {
			return "false";
		} else if ("config-type".equals(attributeName)) {
			return "field";
		} else {
			return super.getDefaultValue(attributeName);
		}
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("configInfo".equals(evt.getPropertyName())) {
			setTextContent((String)evt.getNewValue());
		} else if ("className".equals(evt.getPropertyName())) {
			setAttribute("class", (String)evt.getNewValue());
		} else if ("configType".equals(evt.getPropertyName())) {
			setAttribute("config-type", (String)evt.getNewValue());
		} else if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		} else if ("refName".equals(evt.getPropertyName())) {
			setAttribute("ref-name", (String)evt.getNewValue());
		} else if ("acceptPropagatedEvents".equals(evt.getPropertyName())) {
			setAttribute("accept-propagated-events", (String)evt.getNewValue());
		} else if ("expression".equals(evt.getPropertyName())) {
			setAttribute("expression", (String)evt.getNewValue());
		} else if ("async".equals(evt.getPropertyName())) {
			setAttribute("async", (String)evt.getNewValue());
		} else if ("genericElementAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("genericElementRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		}
	}
	
	private boolean isDifferent(String left, String right) {
		if (left == null) return right == null;
		return left.equals(right);
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Action action = (Action)getSemanticElement();
		if ("#text".equals(name) && action.getGenericElements().length == 0) {
			if (isDifferent(action.getConfigInfo(), getTextContent())) {
				action.setConfigInfo(newValue);
			}
		} else if ("class".equals(name)) {
			action.setClassName(newValue);
		} else if ("config-type".equals(name)) {
			action.setConfigType(newValue);
		} else if ("name".equals(name)) {
			action.setName(newValue);
		} else if ("ref-name".equals(name)) {
			action.setRefName(newValue);
		} else if ("accept-propagated-events".equals(name)) {
			action.setAcceptPropagatedEvents(newValue);
		} else if ("expression".equals(name)) {
			action.setExpression(newValue);
		} else if ("async".equals(name)) {
			action.setAsync(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement;
		if ("genericElement".equals(type)) {
			jpdlElement = getSemanticElementFactory().createById("org.jbpm.gd.jpdl.genericElement");
		} else {
			jpdlElement = createSemanticElementFor(child);
		}
		child.initialize(jpdlElement);
		Action action = (Action)getSemanticElement();
		if ("genericElement".equals(type)) {
			action.addGenericElement((GenericElement)jpdlElement);
		}
	}
	
	protected SemanticElement createSemanticElementFor(XmlAdapter child) {
		if ("genericElement".equals(child.getElementType())) {
			return getSemanticElementFactory().createById("org.jbpm.gd.jpdl.genericElement");
		} else {
			return super.createSemanticElementFor(child);
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// an action cannot have any child nodes
	}
	
}
