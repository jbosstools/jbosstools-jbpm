package org.jbpm.gd.common.notation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public abstract class AbstractNodeContainer extends Node implements NodeContainer {
	
	List nodes = new ArrayList();
	protected List danglingEdges = new ArrayList();

	public AbstractNodeContainer() {
		setConstraint(new Rectangle(new Point(0, 0), new Dimension(300, 150)));
	}

	public void addNode(Node node) {
		nodes.add(node);
		node.setContainer(this);
		addArrivingEdges(node);
		firePropertyChange("nodeAdd", null, node);
	}
	
	public void removeNode(Node node) {
		removeArrivingEdges(node);
		nodes.remove(node);
		node.setContainer(null);
		firePropertyChange("nodeRemove", node, null);
	}
	
	public List getNodes() {
		return nodes;
	}

	protected void addArrivingEdges(Node node) {
	}
	
	protected void removeArrivingEdges(Node node) {
		List list = node.getArrivingEdges();
		for (int i = 0; i < list.size(); i++) {
			Edge edge = (Edge)list.get(i);
			node.removeArrivingEdge(edge);
			danglingEdges.add(edge);
		}
	}
	
}
