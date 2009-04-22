package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.jface.viewers.StructuredSelection;
import org.jboss.tools.flow.common.editpart.RootEditPart;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.jpdl4.policy.ProcessLayoutEditPolicy;

public class ProcessGraphicalEditPart extends RootEditPart implements JpdlGraphicalEditPart {

    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new ProcessLayoutEditPolicy());
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
    }

    public void modelChanged(ModelEvent event) {
    	super.modelChanged(event);
        if (event.getChangeType() == ContainerWrapper.ADD_ELEMENT) {
        	Object changedObject = event.getChangedObject();
        	if (changedObject != null) {
        		EditPart editPart = (EditPart)getViewer().getEditPartRegistry().get(changedObject);
        		if (editPart instanceof ProcessNodeGraphicalEditPart) {
        			((ProcessNodeGraphicalEditPart)editPart).performDirectEdit();
        			// force selection to update the properties view
        			getViewer().setSelection(new StructuredSelection(editPart));
        		}
        	}
    	}
    }
    
}
