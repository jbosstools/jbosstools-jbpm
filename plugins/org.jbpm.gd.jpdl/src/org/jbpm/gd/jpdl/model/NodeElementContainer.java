package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.SemanticElement;

public interface NodeElementContainer extends SemanticElement {

	void addNodeElement(NodeElement node);
	void removeNodeElement(NodeElement node);
	NodeElement[] getNodeElements();
	NodeElement getNodeElementByName(String nodeName);
	boolean canAdd(NodeElement node);

}
