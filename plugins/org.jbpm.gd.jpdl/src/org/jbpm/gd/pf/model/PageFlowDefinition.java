package org.jbpm.gd.pf.model;

import java.util.ArrayList;
import java.util.List;

public class PageFlowDefinition extends AbstractNamedElement {
	
	private StartPage startPage;
	private List nodeElements = new ArrayList();
		
	public void addStartPage(StartPage startPage) {
		if (this.startPage != null) return;
		this.startPage = startPage;
		nodeElements.add(0, startPage);
		firePropertyChange("startPageAdd", null, startPage);
	}
	
	public void removeStartPage(StartPage startPage) {
		if (this.startPage != startPage) return;
		this.startPage = null;
		nodeElements.remove(0);
		firePropertyChange("startPageRemove", startPage, null);
	}
	
	public StartPage getStartPage() {
		return startPage;
	}
	
	public void addNodeElement(NodeElement nodeElement) {
		nodeElements.add(nodeElement);
		firePropertyChange("nodeElementAdd", null, nodeElement);
	}
	
	public void removeNodeElement(NodeElement nodeElement) {
		nodeElements.remove(nodeElement);
		firePropertyChange("nodeElementRemove", nodeElement, null);
	}
	
	public NodeElement[] getNodeElements() {
		return (NodeElement[])nodeElements.toArray(new NodeElement[nodeElements.size()]);
	}
	
	public NodeElement getNodeElementByName(String name) {
		if (name == null) return null;
		NodeElement[] nodeElements = getNodeElements();
		for (int i = 0; i < nodeElements.length; i++) {
			if (name.equals(nodeElements[i].getName())) {
				return nodeElements[i];
			}
		}
		return null;
	}
}
