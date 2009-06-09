package org.jbpm.gd.pf.notation;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.jbpm.gd.common.editor.CreationFactory;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.pf.model.NodeElement;
import org.jbpm.gd.pf.model.PageFlowDefinition;
import org.jbpm.gd.pf.model.Transition;

public class PageFlowNode extends Node {

	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("name")) {
			firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			List edges = getArrivingEdges();
			for (int i = 0; i < edges.size(); i++) {
				Edge edge = (Edge)edges.get(i);
				((SemanticElement)edge.getSemanticElement()).removePropertyChangeListener(edge);
				((Transition)edge.getSemanticElement()).setTo((String)evt.getNewValue());
				((SemanticElement)edge.getSemanticElement()).addPropertyChangeListener(edge);
			}		
		} else if (eventName.equals("transitionAdd")) {
			Transition transition = (Transition)evt.getNewValue();
			Edge edge = (Edge)getRegisteredNotationElementFor(transition);
			if (edge == null) {
				CreationFactory factory = new CreationFactory(transition, getFactory());
				edge = (Edge)factory.getNewObject();
			}
			addLeavingEdge(edge);
			transition.addPropertyChangeListener(edge);			
			Node targetNode = getDestinationNode(transition.getTo());
			if (targetNode != null) {
				targetNode.addArrivingEdge(edge);
			}
		} else if (eventName.equals("transitionRemove")) {
			Transition transition = (Transition)evt.getOldValue();
			Edge edge = (Edge)getRegisteredNotationElementFor(transition);
			if (edge != null) {
				Node targetNode = edge.getTarget();
				if (targetNode != null) {
					transition.removePropertyChangeListener(edge);
					targetNode.removeArrivingEdge(edge);
				}
				removeLeavingEdge(edge);
			}
		} else {
			super.propertyChange(evt);
		}
	}
	
	private Node getDestinationNode(String to) {
		NodeElement nodeElement = ((PageFlowDefinition)getContainer().getSemanticElement()).getNodeElementByName(to);
		return (Node)getRegisteredNotationElementFor(nodeElement);
	}
	
}
