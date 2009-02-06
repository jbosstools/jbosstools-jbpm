package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.jboss.tools.flow.common.editpart.ConnectionEditPart;
import org.jboss.tools.flow.common.policy.ElementDirectEditPolicy;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.LabelWrapper;
import org.jboss.tools.flow.common.wrapper.ModelEvent;

public class SequenceFlowEditPart extends ConnectionEditPart {

    protected void createEditPolicies() {
    	super.createEditPolicies();
    	installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ElementDirectEditPolicy());
    }
    
    public void performRequest(Request request) {
        if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            performDirectEdit(request);
         } else {
            super.performRequest(request);
        }
    }
    
    protected void performDirectEdit(Request request) {
    	ConnectionWrapper connectionWrapper = getElementConnection();
    	LabelWrapper labelWrapper = connectionWrapper.getLabel();
    	if (labelWrapper != null) {
    		EditPart editPart = (EditPart)getViewer().getEditPartRegistry().get(labelWrapper);
    		if (editPart != null) {
    			editPart.performRequest(request);
    		}
    	}
    }
    
    public void modelChanged(ModelEvent event) {
        refresh();
    }

}
