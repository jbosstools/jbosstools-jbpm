package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.DirectEditRequest;
import org.jboss.tools.flow.common.editpart.ConnectionEditPart;
import org.jboss.tools.flow.common.figure.CrossHairDecoration;
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
    
    protected IFigure createFigure() {
    	PolylineConnection result = (PolylineConnection)super.createFigure();
    	Element element = ((Wrapper)getModel()).getElement();
    	if (element instanceof SequenceFlow) {
    		SequenceFlow sequenceFlow = (SequenceFlow)element;
    		if (sequenceFlow.isDefault()) {
    			result.setSourceDecoration(new CrossHairDecoration());
    		} else if (sequenceFlow.isConditional()) {
    			result.setSourceDecoration(new DiamondDecoration());
    		}
    	}
    	return result;
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
