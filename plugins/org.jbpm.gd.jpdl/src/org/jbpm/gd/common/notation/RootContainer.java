package org.jbpm.gd.common.notation;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.jbpm.gd.common.editor.CreationFactory;
import org.jbpm.gd.common.model.SemanticElement;

public class RootContainer extends AbstractNotationElement implements NodeContainer {
	
	Dimension dimension;
	List nodes = new ArrayList();
	protected List danglingEdges = new ArrayList();
	
	public void setDimension(Dimension newDimension) {
		Dimension oldDimension = dimension;
		dimension = newDimension;
		firePropertyChange("dimension", oldDimension, newDimension);
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	public void addNode(Node node) {
		nodes.add(node);
		node.setContainer(this);
		addArrivingEdges(node);
		addLeavingEdges(node);
		firePropertyChange("nodeAdd", null, node);
	}
	
	public void removeNode(Node node) {
		removeArrivingEdges(node);
		removeLeavingEdges(node);
		nodes.remove(node);
		node.setContainer(null);
		firePropertyChange("nodeRemove", node, null);
	}
	
	public List getNodes() {
		return nodes;
	}

	protected void removeNode(PropertyChangeEvent evt) {
		SemanticElement jpdlElement = (SemanticElement)evt.getOldValue();
		AbstractNotationElement notationElement = getRegisteredNotationElementFor(jpdlElement);
		if (notationElement != null) {
			jpdlElement.removePropertyChangeListener(notationElement);
			removeNode((Node)notationElement);
//			notationElement.unregister();
		}
	}

	protected void addNode(PropertyChangeEvent evt) {
		SemanticElement semanticElement = (SemanticElement)evt.getNewValue();
		AbstractNotationElement notationElement = getRegisteredNotationElementFor(semanticElement);
		if (notationElement == null) {
			CreationFactory factory = new CreationFactory(semanticElement, getFactory());
			notationElement = (AbstractNotationElement)factory.getNewObject();
		}
		addNode((Node)notationElement);
		semanticElement.addPropertyChangeListener(notationElement);
	}
	
	protected void addLeavingEdges(Node node) {
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
	
	protected void removeLeavingEdges(Node node) {
		List list = node.getLeavingEdges();
		for (int i = 0; i < list.size(); i++) {
			Edge edge = (Edge)list.get(i);
			Node target = edge.getTarget();
			if (target != null) {
				target.removeArrivingEdge(edge);
			}
			node.removeLeavingEdge(edge);
			SemanticElement semanticElement = edge.getSemanticElement();
			if (semanticElement != null) {
				semanticElement.removePropertyChangeListener(edge);
			}
		}
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}

	
}
