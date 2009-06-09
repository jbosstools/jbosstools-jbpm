package org.jbpm.gd.common.notation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class Node extends AbstractNotationElement {
	
	private NodeContainer container;
	
	private Rectangle constraint = new Rectangle(new Point(0, 0), new Dimension(132, 36));
	private List leavingEdges = new ArrayList();
	private List arrivingEdges = new ArrayList();
	
	public void setConstraint(Rectangle newConstraint) {
		Rectangle oldConstraint = constraint;
		constraint = newConstraint;
		firePropertyChange("constraint", oldConstraint, newConstraint);
	}
	
	public Rectangle getConstraint() {
		return constraint;
	}
	
	public void addLeavingEdge(Edge edge) {
		leavingEdges.add(edge);
		edge.setSource(this);
		firePropertyChange("leavingEdgeAdd", null, edge);
	}
	
	public void removeLeavingEdge(Edge edge) {
		leavingEdges.remove(edge);
		edge.setSource(null);
		firePropertyChange("leavingEdgeRemove", null, edge);
	}
	
	public List getLeavingEdges() {
		return leavingEdges;
	}

	public void addArrivingEdge(Edge edge) {
		arrivingEdges.add(edge);
		edge.setTarget(this);
		firePropertyChange("arrivingEdgeAdd", null, edge);
	}
	
	public void removeArrivingEdge(Edge edge) {
		arrivingEdges.remove(edge);
		edge.setTarget(null);
		firePropertyChange("arrivingEdgeRemove", null, edge);
	}
	
	public List getArrivingEdges() {
		return arrivingEdges;
	}
	
	public void setContainer(NodeContainer notationElement) {
		this.container = notationElement;
	}
	
	public  NodeContainer getContainer() {
		return container;
	}

}
