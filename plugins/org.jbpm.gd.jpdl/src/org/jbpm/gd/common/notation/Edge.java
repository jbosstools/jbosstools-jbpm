package org.jbpm.gd.common.notation;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.jbpm.gd.common.model.SemanticElement;

public class Edge extends AbstractNotationElement {

	Node source;
	Node target;

	Label label;
	List bendPoints = new ArrayList();

	public Label getLabel() {
		if (label == null) {
			label = (Label) getFactory().create("org.jbpm.gd.jpdl.label");
			addPropertyChangeListener(label);
		}
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public void addBendPoint(BendPoint bendPoint) {
		bendPoints.add(bendPoint);
		firePropertyChange("bendPointAdd", null, bendPoint);
	}

	public void addBendPoint(int index, BendPoint bendPoint) {
		bendPoints.add(index, bendPoint);
		firePropertyChange("bendPointAdd", null, bendPoint);
	}

	public void setBendPoint(int index, BendPoint newBendPoint) {
		BendPoint oldBendPoint = (BendPoint) bendPoints.get(index);
		bendPoints.set(index, newBendPoint);
		firePropertyChange("bendPointSet", oldBendPoint, newBendPoint);
	}

	public void removeBendPoint(BendPoint bendPoint) {
		bendPoints.remove(bendPoint);
		firePropertyChange("bendPointRemove", bendPoint, null);
	}

	public void removeBendPoint(int index) {
		BendPoint bendPoint = (BendPoint) bendPoints.get(index);
		bendPoints.remove(index);
		firePropertyChange("bendPointRemove", bendPoint, null);
	}

	public List getBendPoints() {
		return bendPoints;
	}

	public void setSource(Node newSource) {
		source = newSource;
	}

	public Node getSource() {
		return source;
	}

	public void setTarget(Node newTarget) {
		target = newTarget;
	}

	public Node getTarget() {
		return target;
	}

	public void setSemanticElement(SemanticElement semanticElement) {
		super.setSemanticElement(semanticElement);
		getLabel().setSemanticElement(semanticElement);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("name".equals(evt.getPropertyName())) {
			firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		} else {
			super.propertyChange(evt);
		}
	}
	
}
