package org.jbpm.gd.pf.command;

import org.jbpm.gd.common.command.AbstractEdgeMoveCommand;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.pf.model.NodeElement;
import org.jbpm.gd.pf.model.Transition;

public class EdgeMoveCommand extends AbstractEdgeMoveCommand {
	
	protected void doMoveSource(Node oldSource, Node newSource) {
		oldSource.removeLeavingEdge(getEdge());
		((NodeElement)oldSource.getSemanticElement()).removeTransition((Transition)getEdge().getSemanticElement());
		((NodeElement)newSource.getSemanticElement()).addTransition((Transition)getEdge().getSemanticElement());		
	}
	
	protected void doMoveTarget(Node target) {
		((Transition)getEdge().getSemanticElement()).setTo(((NodeElement)target.getSemanticElement()).getName());
	}
	
}
