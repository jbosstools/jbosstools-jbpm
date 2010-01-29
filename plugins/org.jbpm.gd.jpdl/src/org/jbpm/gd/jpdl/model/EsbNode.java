package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.GenericElement;



public class EsbNode extends Node implements EsbElement {
	
	private static final String CALLBACK_ACTION_NAME = "org.jboss.soa.esb.services.jbpm.actionhandlers.EsbActionHandler";
	
	private void createAction() {
		Action action = (Action)getFactory().createById("org.jbpm.gd.jpdl.action");
		setAction(action);
		action.setClassName(CALLBACK_ACTION_NAME);
	}
	
	public Action getAction() {
		Action action = super.getAction();
		if (action == null) {
			createAction();
		}
		return action;
	}
	
	public boolean isOneWay() {
		return false;
	}
		
	private GenericElement addGenericElement(String elementName) {
		GenericElement element = (GenericElement)getFactory().createById("org.jbpm.gd.jpdl.genericElement");
		element.setName(elementName);
		getAction().addGenericElement(element);
		return element;
	}
	
	private void setConfigurationElement(String elementName, String value) {
		GenericElement element = getGenericElement(elementName);
		if (value == null && element != null) {
			getAction().removeGenericElement(element);
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
		GenericElement[] genericElements = getAction().getGenericElements();
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
			getAction().removeGenericElement(element);
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
