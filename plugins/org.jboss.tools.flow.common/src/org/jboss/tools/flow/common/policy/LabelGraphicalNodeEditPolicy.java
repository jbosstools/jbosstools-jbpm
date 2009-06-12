package org.jboss.tools.flow.common.policy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.jboss.tools.flow.common.command.LabelMoveCommand;
import org.jboss.tools.flow.common.wrapper.LabelWrapper;

public class LabelGraphicalNodeEditPolicy extends NonResizableEditPolicy {
	
	public Command getMoveCommand(ChangeBoundsRequest request) {
		if (!(getHost().getModel() instanceof LabelWrapper)) return null;
		LabelWrapper model = (LabelWrapper)getHost().getModel();
		Point delta = request.getMoveDelta();
		return new LabelMoveCommand(model, getParentFigure(), delta);
	}
	
	private IFigure getParentFigure() {
		return ((GraphicalEditPart)getHost().getParent()).getFigure();
	}

}
