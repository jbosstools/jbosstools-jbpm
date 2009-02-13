package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.jboss.tools.flow.common.editpart.NodeEditPart;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.policy.ProcessNodeGraphicalNodeEditPolicy;

public class ProcessNodeGraphicalEditPart extends NodeEditPart implements JpdlGraphicalEditPart {
	
    protected void createEditPolicies() {
    	super.createEditPolicies();
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ProcessNodeGraphicalNodeEditPolicy());
   }
    
    public void modelChanged(ModelEvent event) {
    	super.modelChanged(event);
        if (event.getChange() == Wrapper.ADD_INCOMING_CONNECTION) {
            Object object = getViewer().getEditPartRegistry().get(event.getChangedObject());
            if (object != null && object instanceof SequenceFlowGraphicalEditPart) {
            	getViewer().select((EditPart)object);
            	((SequenceFlowGraphicalEditPart)object).performDirectEdit();
            }
        }
    }

    // make performDirectEdit public
	public void performDirectEdit() {
		super.performDirectEdit();
	}

}
