package org.jboss.tools.flow.common.editpart;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.jboss.tools.flow.common.policy.ElementDirectEditManager;
import org.jboss.tools.flow.common.policy.ElementDirectEditPolicy;
import org.jboss.tools.flow.common.policy.LabelGraphicalNodeEditPolicy;
import org.jboss.tools.flow.common.wrapper.LabelWrapper;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.ModelListener;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class LabelEditPart extends AbstractGraphicalEditPart implements ModelListener {

    private DirectEditManager manager;
    
	@Override
	protected IFigure createFigure() {
		return new Label();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new LabelGraphicalNodeEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ElementDirectEditPolicy());
	}
	
	protected void refreshVisuals() {
		String text = getLabelWrapper().getText();
		Polyline polyline = (Polyline)((ConnectionEditPart)getParent()).getConnectionFigure();
		Point location = ((LabelWrapper)getModel()).getLocation();
		if (location == null) {
			location = calculateInitialLocation(polyline, text);
			((LabelWrapper)getModel()).setLocation(location);
		}
		Label label = (Label)getFigure();
		label.setText(text);
		LabelConstraint constraint = new LabelConstraint(text, location, polyline);
		((GraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), constraint);
	}
	
	private Point calculateInitialLocation(Polyline polyline, String text) {
		Dimension textDimension = FigureUtilities.getTextExtents(text, figure.getFont());
		return new Point(- (textDimension.width +5) , -(textDimension.height + 5));
	}
	
    public void performRequest(Request request) {
        if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            performDirectEdit();
        } else {
            super.performRequest(request);
        }
    }
    
    protected void performDirectEdit() {
    	Label label = (Label) getFigure();
    	if (label == null) {
    		return;
    	}
        if (manager == null) {
            manager = new ElementDirectEditManager(this, new CellEditorLocator(label));
        }
        manager.show();
    }
    
	public LabelWrapper getLabelWrapper() {
		return (LabelWrapper)getModel();
	}
	
	public DragTracker getDragTracker(Request request) {
		return new DragEditPartsTracker(this) {
			protected EditPart getTargetEditPart() {
				return getParent();
			}
		};
	}
	
    public void activate() {
        super.activate();
        getLabelWrapper().addListener(this);
        if (getParent().getModel() instanceof Wrapper) {
        	((Wrapper)getParent().getModel()).addListener(this);
        }
    }

    public void deactivate() {
        if (getParent().getModel() instanceof Wrapper) {
        	((Wrapper)getParent().getModel()).removeListener(this);
        }
        getLabelWrapper().removeListener(this);
        super.deactivate();
    }

	public void modelChanged(ModelEvent event) {
		if (event.getChangeType() == Wrapper.CHANGE_PROPERTY || event.getChangeType() == Wrapper.CHANGE_VISUAL) {
			refreshVisuals();
		}
	}
	
	private class LabelConstraint implements Locator {
		String text;
		Point relativeLocation;
		Polyline polyline;
		public LabelConstraint(String text, Point location, Polyline polyline) {
			this.text = text;
			this.relativeLocation = location;
			this.polyline = polyline;
		}
		public void relocate(IFigure figure) {
			Dimension minimum = FigureUtilities.getTextExtents(text, figure.getFont());
			figure.setSize(minimum);
			Point midPoint = polyline.getPoints().getMidpoint();
			Point newLocation = relativeLocation.getCopy();
			newLocation.translate(midPoint);
			figure.setLocation(newLocation);
		}
	}


}
