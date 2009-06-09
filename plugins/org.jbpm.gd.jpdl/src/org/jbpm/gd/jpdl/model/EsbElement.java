package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.GenericElement;
import org.jbpm.gd.common.model.SemanticElement;

public interface EsbElement extends SemanticElement {
	
	boolean isOneWay();

	String getServiceName();
	String getCategoryName();
	String getReplyToOriginator();

	void setServiceName(String name);
	void setCategoryName(String name);
	void setReplyToOriginator(String value);

	void addJbpmToEsbMapping(GenericElement element);
	void removeJbpmToEsbMapping(GenericElement element);
	GenericElement[] getJbpmToEsbMappings();

	void addEsbToJbpmMapping(GenericElement element);
	void removeEsbToJbpmMapping(GenericElement element);
	GenericElement[] getEsbToJbpmMappings();

}
