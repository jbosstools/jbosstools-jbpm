package org.jbpm.gd.pf.notation;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.jbpm.gd.common.model.NamedElement;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.RootContainer;
import org.jbpm.gd.pf.model.NodeElement;
import org.jbpm.gd.pf.model.Transition;

public class PageFlowRootContainer extends RootContainer {
	
	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("startPageAdd") || eventName.equals("nodeElementAdd")) {
			addNode(evt);
		} else if (eventName.equals("startPageRemove") || eventName.equals("nodeElementRemove")) {
			removeNode(evt);
		} else {
			super.propertyChange(evt);
		}
	}

	protected void addArrivingEdges(Node node) {
		ArrayList list = new ArrayList();
		for (int i = 0; i < danglingEdges.size(); i++) {
			String name= ((NamedElement)node.getSemanticElement()).getName();
			Edge edge = (Edge)danglingEdges.get(i);
			String to = ((Transition)edge.getSemanticElement()).getTo();
			if (name != null && name.equals(to)) {
				list.add(edge);
				node.addArrivingEdge(edge);
			}
		}
		danglingEdges.removeAll(list);
	}
	
	protected void addLeavingEdges(Node node) {
		NodeElement nodeElement = (NodeElement)node.getSemanticElement();
		Transition[] transitions = nodeElement.getTransitions();
		for (int i = 0; i < transitions.length; i++) {
			PropertyChangeEvent evt = new PropertyChangeEvent(nodeElement, "transitionAdd", null, transitions[i]);
			node.propertyChange(evt);
		}
	}
	
}
