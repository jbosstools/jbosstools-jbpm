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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.CreationFactory;
import org.jboss.tools.flow.common.policy.ConnectionBendpointEditPolicy;
import org.jboss.tools.flow.common.policy.ConnectionEditPolicy;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.LabelWrapper;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.ModelListener;
import org.jboss.tools.flow.common.wrapper.Wrapper;

/**
 * Implementation of a connection EditPart.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ConnectionEditPart extends AbstractConnectionEditPart implements ModelListener {
    
    public ConnectionWrapper getElementConnection() {
        return (ConnectionWrapper) getModel();
    }
    
    public List<Wrapper> getModelChildren() {
    	List<Wrapper> result = new ArrayList<Wrapper>();
    	LabelWrapper label = getElementConnection().getLabel();
    	if (label != null) result.add(label);
    	return result;
    }
    
    protected void createEditPolicies() {
    	ConnectionEditPolicy connectionEditPolicy = new ConnectionEditPolicy();
    	connectionEditPolicy.setElementConnectionFactory(getElementConnectionFactory());
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_ROLE, connectionEditPolicy);
        installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new ConnectionBendpointEditPolicy());
    }
    
    protected CreationFactory getElementConnectionFactory() {
    	return ElementRegistry.getCreationFactory(getElementConnection().getElement());
    }
    
    
	protected Class<?> getElementConnectionType() {
		return DefaultConnectionWrapper.class;
	}
	
    protected IFigure createFigure() {
        PolylineConnection result = new PolylineConnection();
        result.setForegroundColor(ColorConstants.gray);
        result.setConnectionRouter(new BendpointConnectionRouter());
        result.setTargetDecoration(new PolygonDecoration());
        return result;
    }
    
    public void setSelected(int value) {
        super.setSelected(value);
        if (value != EditPart.SELECTED_NONE) {
            ((PolylineConnection)getFigure()).setLineWidth(2);
        } else {
            ((PolylineConnection)getFigure()).setLineWidth(1);
        }
    }
    
    public void modelChanged(ModelEvent event) {
        if (event.getChangeType() == ConnectionWrapper.CHANGE_BENDPOINTS) {
            refreshBendpoints();
        }
    }

    public void activate() {
        super.activate();
        getElementConnection().addListener(this);
    }

    public void deactivate() {
        getElementConnection().removeListener(this);
        super.deactivate();
    }

    protected void refreshBendpoints() {
        List<Point> bendpoints = getElementConnection().getBendpoints();
        List<Point> constraint = new ArrayList<Point>();
        for (int i = 0; i < bendpoints.size(); i++) {
            constraint.add(new AbsoluteBendpoint((Point) bendpoints.get(i)));
        }
        getConnectionFigure().setRoutingConstraint(constraint);
    }

    protected void refreshVisuals() {
        refreshBendpoints();
    }
}