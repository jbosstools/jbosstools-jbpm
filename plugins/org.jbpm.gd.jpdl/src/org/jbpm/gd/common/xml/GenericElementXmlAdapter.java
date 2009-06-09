package org.jbpm.gd.common.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jbpm.gd.common.model.GenericElement;
import org.jbpm.gd.common.model.SemanticElement;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class GenericElementXmlAdapter extends XmlAdapter {
	
	private static HashMap NODE_TYPES = null;
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("genericElement", "genericElement");
		}
		return NODE_TYPES;
	}
	
	protected String getNodeType(String elementType) {
		return "genericElement";
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		if (jpdlElement == null || !(jpdlElement instanceof GenericElement)) return;
		GenericElement genericElement = (GenericElement)jpdlElement;
		genericElement.setName(getNode().getNodeName());
		genericElement.setValue(getTextContent());
		NamedNodeMap attributes = getNode().getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node node = attributes.item(i);
			genericElement.addGenericAttribute(node.getNodeName(), node.getNodeValue());
		}		
		genericElement.addPropertyChangeListener(this);
	}
	
	protected void initialize() {
		super.initialize();
		GenericElement genericElement = (GenericElement)getSemanticElement();
		if (genericElement != null ) {
			GenericElement[] genericElements = genericElement.getGenericElements();
			for (int i = 0; i < genericElements.length; i++) {
				addElement(genericElements[i]);
			}
			Map attributes = genericElement.getGenericAttibutes();
			Iterator iter = attributes.keySet().iterator();
			while (iter.hasNext()) {
				String name = (String)iter.next();
				String value = (String)attributes.get(name);
				setAttribute(name, value);
			}
			setTextContent(genericElement.getValue());
		}
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("value".equals(evt.getPropertyName())) {
			setTextContent((String)evt.getNewValue());
		} else if ("genericElementAdd".equals(evt.getPropertyName())) {
			addElement((GenericElement)evt.getNewValue());
		} else if ("genericElementRemove".equals(evt.getPropertyName())) {
			removeElement((GenericElement)evt.getOldValue());
		} else {			
			setAttribute(evt.getPropertyName(), (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		GenericElement genericElement = (GenericElement)getSemanticElement();
		if (genericElement != null && "#text".equals(name)) {
			genericElement.setValue(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		if (getElementType().equals(type)) {
			SemanticElement jpdlElement = getSemanticElementFactory().createById("org.jbpm.gd.jpdl.genericElement");
			child.initialize(jpdlElement);
			SemanticElement parent = getSemanticElement();
			if (parent instanceof GenericElement) {
				GenericElement genericElement= (GenericElement )getSemanticElement();
				genericElement.addGenericElement((GenericElement)jpdlElement);
			}
		}
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// a controller cannot have any child nodes
	}
	
	public String getElementType() {
		return "genericElement";
	}
}
