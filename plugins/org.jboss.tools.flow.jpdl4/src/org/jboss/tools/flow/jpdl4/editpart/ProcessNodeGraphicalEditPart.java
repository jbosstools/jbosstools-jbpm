package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.jboss.tools.flow.common.editpart.NodeEditPart;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.policy.ProcessNodeGraphicalNodeEditPolicy;

public class ProcessNodeGraphicalEditPart extends NodeEditPart implements JpdlGraphicalEditPart {
	
    protected void createEditPolicies() {
    	super.createEditPolicies();
    	removeEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE);
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ProcessNodeGraphicalNodeEditPolicy());
   }
    
    public void modelChanged(ModelEvent event) {
    	super.modelChanged(event);
        if (event.getChangeType() == Wrapper.ADD_ELEMENT && "incomingConnection".equals(event.getChangeDiscriminator())) {
            Object object = getViewer().getEditPartRegistry().get(event.getNewValue());
            if (object != null && object instanceof SequenceFlowGraphicalEditPart) {
            	getViewer().select((EditPart)object);
            	((SequenceFlowGraphicalEditPart)object).performDirectEdit();
            }
        } else if ((event.getChangeType() == Wrapper.ADD_ELEMENT || 
        		event.getChangeType() == Wrapper.REMOVE_ELEMENT) &&
        		"outgoingConnection".equals(event.getChangeDiscriminator())) {
        	for (Object connection : getSourceConnections()) {
        		if (connection instanceof AbstractConnectionEditPart) {
        			((AbstractConnectionEditPart)connection).refresh();
        		}
        	}
        	
        }
    }

    // make performDirectEdit public
	public void performDirectEdit() {
		super.performDirectEdit();
	}

}
