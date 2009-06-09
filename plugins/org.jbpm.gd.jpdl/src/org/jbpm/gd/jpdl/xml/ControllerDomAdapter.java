package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.gd.common.model.GenericElement;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.ConfigType;
import org.jbpm.gd.jpdl.model.Controller;
import org.jbpm.gd.jpdl.model.Variable;

public class ControllerDomAdapter extends XmlAdapter {
	
	private static HashMap NODE_TYPES = null;
	
	protected Map getNodeTypes() {
		if (NODE_TYPES == null) {
			NODE_TYPES = new HashMap();
			NODE_TYPES.put("variable", "variable");
			NODE_TYPES.put("genericElement", "genericElement");
		}
		return NODE_TYPES;
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		Controller controller = (Controller)jpdlElement;
		controller.setConfigInfo(getTextContent());
		controller.setClassName(getAttribute("class"));
		controller.setConfigType(getAttribute("config-type"));
		controller.addPropertyChangeListener(this);
	}
	
	protected void initialize() {
		super.initialize();
		Controller controller = (Controller)getSemanticElement();
		if (controller != null ) {
			GenericElement[] genericElements = controller.getGenericElements();
			for (int i = 0; i < genericElements.length; i++) {
				addElement(genericElements[i]);
			}
			setTextContent(controller.getConfigInfo());
			setAttribute("class", controller.getClassName());
			setAttribute("config-type", controller.getConfigType());
			addElements(controller.getVariables());
		}
	}
	
	protected String getDefaultValue(String attributeName) {
		if ("config-type".equals(attributeName)) {
			return ConfigType.FIELD;
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
		} else if ("variableAdd".equals(evt.getPropertyName())) {
			addElement((Variable)evt.getNewValue());
		} else if ("variableRemove".equals(evt.getPropertyName())) {
			removeElement((Variable)evt.getOldValue());
		} else if ("genericElementAdd".equals(evt.getPropertyName())) {
			addElement((SemanticElement)evt.getNewValue());
		} else if ("genericElementRemove".equals(evt.getPropertyName())) {
			removeElement((SemanticElement)evt.getOldValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		Controller controller = (Controller)getSemanticElement();
		if ("#text".equals(name) && newValue != null) {
			controller.setConfigInfo(newValue);
		} else if ("class".equals(name)) {
			controller.setClassName(newValue);
		} else if ("config-type".equals(name)) {
			controller.setConfigType(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		String type = child.getElementType();
		SemanticElement jpdlElement = createSemanticElementFor(child);
		child.initialize(jpdlElement);
		Controller controller = (Controller)getSemanticElement();
		if ("variable".equals(type)) {
			controller.addVariable((Variable)jpdlElement);
		} else if ("genericElement".equals(type)) {
			controller.addGenericElement((GenericElement)jpdlElement);
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
	}
	
	public void addElement(SemanticElement element) {
		super.addElement(element);
	}
	
}
