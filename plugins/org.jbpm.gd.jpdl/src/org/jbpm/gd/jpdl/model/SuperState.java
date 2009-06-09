package org.jbpm.gd.jpdl.model;

import java.util.ArrayList;
import java.util.List;

public class SuperState extends AbstractAsyncableTimerNode implements NodeElementContainer {

	private List nodes = new ArrayList();	
	
	public void addNodeElement(NodeElement node) {
		nodes.add(node);
		firePropertyChange("nodeElementAdd", null, node);
	}
	
	public void removeNodeElement(NodeElement node) {
		nodes.remove(node);
		firePropertyChange("nodeElementRemove", node, null);
	}
	
	public NodeElement[] getNodeElements() {
		return (NodeElement[])nodes.toArray(new NodeElement[nodes.size()]);
	}

	public boolean canAdd(AbstractNode node) {
		return !nodes.contains(node) && node.isPossibleChildOf(this);
	}
	
	public NodeElement getNodeElementByName(String name) {
		NodeElement[] nodeElements = getNodeElements();
		for (int i = 0; i < nodeElements.length; i++) {
			if (nodeElements[i].getName().equals(name)) {
				return nodeElements[i];
			}
		}
		return null;
	}
	
	public boolean canAdd(NodeElement node) {
		return true;
	}
	
}
