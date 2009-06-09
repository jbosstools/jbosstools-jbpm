package org.jbpm.gd.common.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GenericElement extends AbstractSemanticElement {
	
	private String name;
	private String value;
	private List genericElements = new ArrayList();
	private HashMap genericAttributes = new HashMap();
	
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		firePropertyChange("name", oldName, newName);
	}
	
	public String getName() {
		return name;
	}
	
	public void setValue(String newValue) {
		String oldValue = value;
		value = newValue;
		firePropertyChange("value", oldValue, newValue);
	}
	
	public String getValue() {
		return value;
	}
	
	public void addGenericElement(GenericElement genericElement) {
		genericElements.add(genericElement);
		firePropertyChange("genericElementAdd", null, genericElement);
	}
	
	public void removeGenericElement(GenericElement genericElement) {
		genericElements.remove(genericElement);
		firePropertyChange("genericElementRemove", genericElement, null);
	}
	
	public GenericElement[] getGenericElements() {
		return (GenericElement[])genericElements.toArray(new GenericElement[genericElements.size()]);
	}
	
	public void addGenericAttribute(String name, String value) {
		genericAttributes.put(name, value);
		firePropertyChange(name, null, value);
	}
	
	public void removeGenericAttribute(String name) {
		genericAttributes.remove(name);
		firePropertyChange(name, null, null);
	}
	
	public Map getGenericAttibutes() {
		return (Map)genericAttributes.clone();
	}
	
	

}
