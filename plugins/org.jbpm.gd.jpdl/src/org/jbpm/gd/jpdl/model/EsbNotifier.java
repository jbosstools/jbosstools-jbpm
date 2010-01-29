package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.GenericElement;



public class EsbNotifier extends Action implements EsbElement {
	
	private static final String CLASS_NAME = "org.jboss.soa.esb.services.jbpm.actionhandlers.EsbNotifier";
	
	public void setClassName(String newClassName) {
		// changing the class name is not authorized
	}
	
	public String getClassName() {
		return CLASS_NAME;
	}
	
	public boolean isOneWay() {
		return true;
	}
	
	private GenericElement addGenericElement(String elementName) {
		GenericElement element = (GenericElement)getFactory().createById("org.jbpm.gd.jpdl.genericElement");
		element.setName(elementName);
		addGenericElement(element);
		return element;
	}
	
	private void setConfigurationElement(String elementName, String value) {
		GenericElement element = getGenericElement(elementName);
		if (value == null && element != null) {
			removeGenericElement(element);
		} else {
			if (element == null) {
				element = addGenericElement(elementName);
			}
			element.setValue(value);
		}
	}
	
	private String getConfigurationElementValue(String elementName) {
		String result = null;
		GenericElement element = getGenericElement(elementName);
		if (element != null) {
			result = element.getValue();
		}
		return result;
	}
	
	private GenericElement getGenericElement(String name) {
		if (name == null) return null;
		GenericElement[] genericElements = getGenericElements();
		for (int i = 0; i < genericElements.length; i++) {
			if (name.equals(genericElements[i].getName())) {
				return genericElements[i];
			}
		}
		return null;	
	}
	
	public void setCategoryName(String name) {
		setConfigurationElement("esbCategoryName", name);
	}
	
	public String getCategoryName() {
		return getConfigurationElementValue("esbCategoryName");
	}
	
	public void setServiceName(String name) {
		setConfigurationElement("esbServiceName", name);
	}
	
	public String getServiceName() {
		return getConfigurationElementValue("esbServiceName");
	}
	
	public void setReplyToOriginator(String value) {
		setConfigurationElement("replyToOriginator", value);
	}
	
	public String getReplyToOriginator() {
		return getConfigurationElementValue("replyToOriginator");
	}
	
	private void addMapping(GenericElement mapping, String configurationElementName) {
		if (mapping == null) return;
		GenericElement element = getGenericElement(configurationElementName);
		if (element == null) {
			element = addGenericElement(configurationElementName);
		}
		element.addGenericElement(mapping);
	}
	
	private void removeMapping(GenericElement mapping, String configurationElementName) {
		if (mapping == null) return;
		GenericElement element = getGenericElement(configurationElementName);
		element.removeGenericElement(mapping);
		if (element.getGenericElements().length == 0) {
			removeGenericElement(element);
		}
	}
	
	public void addJbpmToEsbMapping(GenericElement mapping) {
		addMapping(mapping, "bpmToEsbVars");
	}
	
	public void removeJbpmToEsbMapping(GenericElement mapping) {
		removeMapping(mapping, "bpmToEsbVars");
	}
	
	public GenericElement[] getJbpmToEsbMappings() {
		GenericElement genericElements = getGenericElement("bpmToEsbVars");
		return genericElements != null ? genericElements.getGenericElements() : new GenericElement[0];
	}
	
	public void addEsbToJbpmMapping(GenericElement mapping) {
		addMapping(mapping, "esbToBpmVars");
	}
	
	public void removeEsbToJbpmMapping(GenericElement mapping) {
		removeMapping(mapping, "esbToBpmVars");
	}
	
	public GenericElement[] getEsbToJbpmMappings() {
		GenericElement genericElements = getGenericElement("esbToBpmVars");
		return genericElements != null ? genericElements.getGenericElements() : new GenericElement[0];
	}
	
	public boolean isActionElementConfigurable() {
		return false;
	}

}
