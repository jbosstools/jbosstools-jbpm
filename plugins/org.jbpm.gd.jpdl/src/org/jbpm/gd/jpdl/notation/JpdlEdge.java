package org.jbpm.gd.jpdl.notation;

import java.beans.PropertyChangeEvent;

import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NodeContainer;
import org.jbpm.gd.common.notation.RootContainer;
import org.jbpm.gd.jpdl.model.NodeElement;
import org.jbpm.gd.jpdl.model.ProcessDefinition;

public class JpdlEdge extends Edge {

	// public Label getLabel() {
	// Label result = super.getLabel();
	// if (result == null) {
	// result = (Label)getFactory().create("org.jbpm.gd.jpdl.label");
	// addPropertyChangeListener(result);
	// setLabel(result);
	// }
	// return result;
	// }

	// public void setSemanticElement(SemanticElement semanticElement) {
	// super.setSemanticElement(semanticElement);
	// getLabel().setSemanticElement(semanticElement);
	// }
	//	
	
	private RootContainer getRootContainer() {
		Node source = getSource();
		if (source == null) return null;
		NodeContainer container = source.getContainer();
		while (container != null && container instanceof Node) {
			container = ((Node)container).getContainer();
		}
		return (RootContainer)container;
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("to")) {
			if (getSource() == null)
				return;
			RootContainer rootContainer = getRootContainer();
			if (rootContainer == null) return;
			ProcessDefinition processDefinition = (ProcessDefinition)rootContainer.getSemanticElement();
			if (processDefinition == null) return;
			NodeElement newTarget = processDefinition
					.getNodeElementByName((String) evt.getNewValue());
			NodeElement oldTarget = processDefinition
					.getNodeElementByName((String) evt.getOldValue());
			if (oldTarget != null) {
				Node oldTargetNode = (Node) getRegisteredNotationElementFor(oldTarget);
				if (oldTargetNode != null) {
					oldTargetNode.removeArrivingEdge(this);
				}
			}
			if (newTarget != null) {
				Node targetNode = (Node) getRegisteredNotationElementFor(newTarget);
				if (targetNode != null) {
					targetNode.addArrivingEdge(this);
				}
			}
			getSource().propertyChange(
					new PropertyChangeEvent(this, "leavingEdgeRefresh", null,
							null));
		} else {
			super.propertyChange(evt);
		}
	}

}
