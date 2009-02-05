package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.gef.EditPolicy;
import org.jboss.tools.flow.common.editpart.NodeEditPart;
import org.jboss.tools.flow.jpdl4.policy.ProcessNodeGraphicalNodeEditPolicy;

public class ProcessNodeEditPart extends NodeEditPart {
	
    protected void createEditPolicies() {
    	super.createEditPolicies();
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ProcessNodeGraphicalNodeEditPolicy());
   }
    
    // make performDirectEdit public
	public void performDirectEdit() {
		super.performDirectEdit();
	}

}
