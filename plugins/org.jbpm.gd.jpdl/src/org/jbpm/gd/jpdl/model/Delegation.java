package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.gd.common.model.AbstractSemanticElement;
import org.jbpm.gd.common.model.GenericElement;

public class Delegation extends AbstractSemanticElement {
	
	private String configInfo;
	private List genericElements = new ArrayList();
	private String className;
	private String configType = "field";
	
	public void setConfigInfo(String newConfigInfo) {
		String oldConfigInfo = configInfo;
		configInfo = newConfigInfo;
		firePropertyChange("configInfo", oldConfigInfo, newConfigInfo);
	}
	
	public String getConfigInfo() {
		return configInfo;
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
	
	public void setClassName(String newClassName) {
		String oldClassName = className;
		className = newClassName;
		firePropertyChange("className", oldClassName, newClassName);
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setConfigType(String newConfigType) {
		if (newConfigType == null) {
			newConfigType = "field";
		}
		String oldConfigType = configType;
		configType = newConfigType;
		firePropertyChange("configType", oldConfigType, newConfigType);
	}
	
	public String getConfigType() {
		return configType;
	}

}
