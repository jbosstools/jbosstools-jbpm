package org.jbpm.gd.common.policy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.swt.graphics.Color;
import org.jbpm.gd.jpdl.Constants;

/**
 */
public class ContainerHighlightEditPolicy extends
		org.eclipse.gef.editpolicies.GraphicalEditPolicy {

	private Color revertColor;

	public void eraseTargetFeedback(Request request) {
		if (revertColor != null) {
			setContainerBackground(revertColor);
			revertColor = null;
		}
	}

	private Color getContainerBackground() {
		return getContainerFigure().getBackgroundColor();
	}

	private IFigure getContainerFigure() {
		return ((GraphicalEditPart) getHost()).getFigure();
	}

	public EditPart getTargetEditPart(Request request) {
		return request.getType().equals(RequestConstants.REQ_SELECTION_HOVER) ? getHost()
				: null;
	}

	private void setContainerBackground(Color c) {
		getContainerFigure().setBackgroundColor(c);
	}

	protected void showHighlight() {
		if (revertColor == null) {
			revertColor = getContainerBackground();
			setContainerBackground(Constants.veryLightBlue);
		}
	}

	public void showTargetFeedback(Request request) {
		if (request.getType().equals(RequestConstants.REQ_MOVE)
				|| request.getType().equals(RequestConstants.REQ_ADD)
				|| request.getType().equals(RequestConstants.REQ_CLONE)
				|| request.getType().equals(
						RequestConstants.REQ_CONNECTION_START)
				|| request.getType()
						.equals(RequestConstants.REQ_CONNECTION_END)
				|| request.getType().equals(RequestConstants.REQ_CREATE))
			showHighlight();
	}

}
