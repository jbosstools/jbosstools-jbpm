package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.GenericElement;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.ConfigType;
import org.jbpm.gd.jpdl.model.Handler;

public class HandlerDomAdapter extends XmlAdapter {
	
	private static HashMap NODE_TYPES = null;
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("genericElement", "genericElement");
		}
		return NODE_TYPES;
	}
	
	protected String getDefaultValue(String attributeName) {
		if ("config-type".equals(attributeName)) {
			return ConfigType.FIELD;
		} else {
			return super.getDefaultValue(attributeName);
		}
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Handler handler = (Handler)jpdlElement;
		handler.setConfigInfo(getTextContent());
		handler.setClassName(getAttribute("class"));
		handler.setConfigType(getAttribute("config-type"));
		handler.addPropertyChangeListener(this);
	}
	
	protected void initialize() {
		super.initialize();
		Handler handler = (Handler)getSemanticElement();
		if (handler != null) {
			GenericElement[] genericElements = handler.getGenericElements();
			for (int i = 0; i < genericElements.length; i++) {
				addElement(genericElements[i]);
			}
			setTextContent(handler.getConfigInfo());
			setAttribute("class", handler.getClassName());
			setAttribute("config-type", handler.getConfigType());
		}
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("configInfo".equals(evt.getPropertyName())) {
			setTextContent((String)evt.getNewValue());
		} else if ("className".equals(evt.getPropertyName())) {
			setAttribute("class", (String)evt.getNewValue());
		} else if ("configType".equals(evt.getPropertyName())) {
			setAttribute("config-type", (String)evt.getNewValue());
		} else if ("genericElementAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("genericElementRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Handler handler = (Handler)getSemanticElement();
		if ("#text".equals(name)) {
			handler.setConfigInfo(newValue);
		} else if ("class".equals(name)) {
			handler.setClassName(newValue);
		} else if ("config-type".equals(name)) {
			handler.setConfigType(newValue);
		}
	}
	
	protected SemanticElement createSemanticElementFor(XmlAdapter child) {
		if ("genericElement".equals(child.getElementType())) {
			return getSemanticElementFactory().createById("org.jbpm.gd.jpdl.genericElement");
		} else {
			return super.createSemanticElementFor(child);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		// a handler cannot have any child nodes
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// a handler cannot have any child nodes
	}
	
}
