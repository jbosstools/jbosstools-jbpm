package org.jbpm.gd.common.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.jbpm.gd.common.notation.BendPoint;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Node;

public abstract class AbstractEdgeMoveCommand extends Command {
	
	private Node oldTarget;
	private Node oldSource;
	private Node target;
	private Node source;
	private Edge edge;
	
	private boolean bendPointsAdded = false;
	
	protected abstract void doMoveSource(Node oldSource, Node newSource);
	protected abstract void doMoveTarget(Node target);
	
	public void execute() {
		if (oldTarget == null) {
			oldTarget = edge.getTarget();
		}
		if (oldSource == null) {
			oldSource = edge.getSource();
		}
		if (source != null && source != edge.getSource()) {
			doMoveSource(oldSource, source);
		} 
		if (target != null && target != edge.getTarget()) {
			doMoveTarget(target);
		}
		if (edge.getSource() == edge.getTarget() && edge.getBendPoints().isEmpty()) {
			addBendPoints();
		}
	}
	
	public void undo() {
		if (bendPointsAdded) {
			removeBendPoints();
		}
		if (target != null) {
			doMoveTarget(oldTarget);
		}
		if (source != null) {
			doMoveSource(source, oldSource);
		}
	}
	
	private void removeBendPoints() {
		List list = new ArrayList(edge.getBendPoints());
		for (int i = 0; i < list.size(); i++) {
			edge.removeBendPoint((BendPoint)list.get(i));
		}
	}
	
	private void addBendPoints() {
		bendPointsAdded = true;
		Rectangle constraint = source.getConstraint();
		int horizontal = - (constraint.width / 2 + 25);
		int vertical = horizontal * constraint.height / constraint.width;
		BendPoint first = new BendPoint();
		first.setRelativeDimensions(new Dimension(horizontal, 0), new Dimension(horizontal, 0));
		BendPoint second = new BendPoint();
		second.setRelativeDimensions(new Dimension(horizontal, vertical), new Dimension(horizontal, vertical));
		edge.addBendPoint(first);
		edge.addBendPoint(second);
	}		
	public boolean canExecute() {
		if (source == null && target == null) {
			return false;
		} else {
			return true;
		}
	}		
	
	public void setSource(Node newSource) {
		source = newSource;
	}		
	
	public void setEdge(Edge newEdge) {
		edge = newEdge;
	}	
	
	protected Edge getEdge() {
		return edge;
	}
	
	public void setTarget(Node newTarget) {
		target = newTarget;
	}
	
}
