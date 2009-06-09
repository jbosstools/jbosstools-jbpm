package org.jbpm.gd.pf.notation;

import java.beans.PropertyChangeEvent;

import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.pf.model.NodeElement;
import org.jbpm.gd.pf.model.PageFlowDefinition;

public class PageFlowEdge extends Edge {
	
//	public Label getLabel() {
//		Label result = super.getLabel();
//		if (result == null) {
//			result = (Label)getFactory().create("org.jbpm.gd.jpdl.label");
//			addPropertyChangeListener(result);
//			setLabel(result);
//		}
//		return result;
//	}
//	
//	public void setSemanticElement(SemanticElement semanticElement) {
//		super.setSemanticElement(semanticElement);
//		getLabel().setSemanticElement(semanticElement);
//	}
//	
	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("to")) {
			if (getSource() == null) return;
			PageFlowDefinition pageFlowDefinition = (PageFlowDefinition)((Node)getSource()).getContainer().getSemanticElement();
			NodeElement newTarget = pageFlowDefinition.getNodeElementByName((String)evt.getNewValue());
			NodeElement oldTarget = pageFlowDefinition.getNodeElementByName((String)evt.getOldValue());
			if (oldTarget != null) {
				Node oldTargetNode = (Node)getRegisteredNotationElementFor(oldTarget);
				if (oldTargetNode != null) {
					oldTargetNode.removeArrivingEdge(this);
				}
			}
			if (newTarget != null) {
				Node targetNode = (Node)getRegisteredNotationElementFor(newTarget);
				if (targetNode != null) {
					targetNode.addArrivingEdge(this);
				}
			}
			getSource().propertyChange(new PropertyChangeEvent(this, "leavingEdgeRefresh", null, null));
		} else {
			super.propertyChange(evt);
		}
	}	

}
