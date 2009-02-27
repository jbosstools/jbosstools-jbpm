package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.DirectEditRequest;
import org.jboss.tools.flow.common.editpart.ConnectionEditPart;
import org.jboss.tools.flow.common.figure.DiamondDecoration;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.policy.ElementDirectEditPolicy;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.LabelWrapper;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.SequenceFlow;

public class SequenceFlowGraphicalEditPart extends ConnectionEditPart implements JpdlGraphicalEditPart {

    protected void createEditPolicies() {
    	super.createEditPolicies();
    	installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ElementDirectEditPolicy());
    }
    
    protected void refreshVisuals() {
    	super.refreshVisuals();
    	decorateFigure((PolylineConnection)getFigure(), ((Wrapper)getModel()).getElement());
    }
    
    protected IFigure createFigure() {
    	return decorateFigure((PolylineConnection)super.createFigure(), ((Wrapper)getModel()).getElement());
    }

	private IFigure decorateFigure(PolylineConnection figure, Element element) {
		if (element instanceof SequenceFlow) {
    		SequenceFlow sequenceFlow = (SequenceFlow)element;
//    		if (sequenceFlow.isDefault()) {
//    			figure.setSourceDecoration(new CrossHairDecoration());
//    		} else 
    		if (sequenceFlow.isConditional()) {
    			if (getElementConnection().getSource().getOutgoingConnections().size() > 1) {
    				figure.setSourceDecoration(new DiamondDecoration());
    			}
    		} else {
    			figure.setSourceDecoration(null);
    		}
    	}
		return figure;
	}
    
    public void performRequest(Request request) {
        if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            performDirectEdit();
         } else {
            super.performRequest(request);
        }
    }
    
    protected void performDirectEdit() {
    	ConnectionWrapper connectionWrapper = getElementConnection();
    	LabelWrapper labelWrapper = connectionWrapper.getLabel();
    	if (labelWrapper != null) {
    		EditPart editPart = (EditPart)getViewer().getEditPartRegistry().get(labelWrapper);
    		if (editPart != null) {
    			editPart.performRequest(new DirectEditRequest());
    		}
    	}
    }
    
    public void modelChanged(ModelEvent event) {
        refresh();
    }

}
