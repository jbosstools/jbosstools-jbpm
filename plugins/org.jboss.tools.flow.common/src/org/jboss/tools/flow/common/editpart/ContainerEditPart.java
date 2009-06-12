package org.jboss.tools.flow.common.editpart;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.MouseWheelHelper;
import org.eclipse.gef.editparts.ViewportMouseWheelHelper;
import org.jboss.tools.flow.common.figure.ElementContainerFigure;
import org.jboss.tools.flow.common.policy.ElementContainerLayoutEditPolicy;
import org.jboss.tools.flow.common.wrapper.AbstractContainerWrapper;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;

public class ContainerEditPart extends ElementEditPart {

    protected AbstractContainerWrapper getElementContainerElementWrapper() {
        return (AbstractContainerWrapper) getModel();
    }

    protected IFigure createFigure() {
        return new ElementContainerFigure();
    }

    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, null);
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new ElementContainerLayoutEditPolicy());
    }
    
    public void modelChanged(ModelEvent event) {
        if (event.getChangeType() == ContainerWrapper.ADD_ELEMENT) {
        	refreshChildren();
        	Object changedObject = event.getChangedObject();
        	if (changedObject != null) {
        		EditPart editPart = (EditPart)getViewer().getEditPartRegistry().get(changedObject);
        		if (editPart instanceof ElementEditPart) {
        			((ElementEditPart)editPart).performDirectEdit();
        		}
        	}
        } else if (event.getChangeType() == ContainerWrapper.REMOVE_ELEMENT) {
            refreshChildren();
        } else {
            super.modelChanged(event);
        }
    }
    
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class key) {
        if (key == MouseWheelHelper.class) {
            return new ViewportMouseWheelHelper(this);
        }
        return super.getAdapter(key);
    }
    
    protected List<NodeWrapper> getModelChildren() {
        return getElementContainerElementWrapper().getNodeWrappers();
    }

    public IFigure getContentPane() {
        return ((ElementContainerFigure) getFigure()).getPane();
    }
}
