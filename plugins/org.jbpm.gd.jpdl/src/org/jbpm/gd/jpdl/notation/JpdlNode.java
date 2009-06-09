package org.jbpm.gd.jpdl.notation;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.StringTokenizer;

import org.jbpm.gd.common.editor.CreationFactory;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.jpdl.model.NodeElementContainer;
import org.jbpm.gd.jpdl.model.Transition;

public class JpdlNode extends Node {

	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("name")) {
			firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
			updateArrivingEdges((String)evt.getNewValue());
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
//				edge.unregister();
			}
		} else {
			super.propertyChange(evt);
		}
	}

	private void updateArrivingEdges(String to) {
		List edges = getArrivingEdges();
		for (int i = 0; i < edges.size(); i++) {
			Edge edge = (Edge)edges.get(i);
			((SemanticElement)edge.getSemanticElement()).removePropertyChangeListener(edge);
			((Transition)edge.getSemanticElement()).setTo(to);
			((SemanticElement)edge.getSemanticElement()).addPropertyChangeListener(edge);
		}
	}
	
	private Node getDestinationNode(String to) {
		if (to == null) return null;
		NotationElement runner = getContainer();
		SemanticElement jpdlElement = null;
		StringTokenizer tokenizer = new StringTokenizer(to, "/");
		while (tokenizer.hasMoreTokens()) {
			String nextToken = tokenizer.nextToken();
			if ("..".equals(nextToken)) {
				if (runner != null && runner instanceof Node) {
					runner = ((Node)runner).getContainer();
				} else {
					runner = null;
					break;
				}
			} else {
				if (jpdlElement == null) {
					jpdlElement = (SemanticElement)runner.getSemanticElement();
				}
				if (jpdlElement instanceof NodeElementContainer) {
					jpdlElement = ((NodeElementContainer)jpdlElement).getNodeElementByName(nextToken);
					runner = (Node)getRegisteredNotationElementFor(jpdlElement);
				} else {
					runner = null;
					break;
				}				
			}
		}
		return runner == getContainer() ? null : (Node)runner;
	}
	
}
