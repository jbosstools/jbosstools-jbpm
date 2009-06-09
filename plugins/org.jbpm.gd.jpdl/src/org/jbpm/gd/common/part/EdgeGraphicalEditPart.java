/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jbpm.gd.common.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.jbpm.gd.common.figure.EdgeFigure;
import org.jbpm.gd.common.notation.BendPoint;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Label;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.policy.BendpointEditPolicy;
import org.jbpm.gd.common.policy.ConnectionEndpointsEditPolicy;
import org.jbpm.gd.common.policy.ConnectionEditPolicy;

public abstract class EdgeGraphicalEditPart 
	extends AbstractConnectionEditPart
	implements NotationElementGraphicalEditPart {
	
	public EdgeGraphicalEditPart(NotationElement notationElement) {
		setModel(notationElement);
	}
	
	private Edge getEdge() {
		return (Edge)getModel();
	}
	
	protected IFigure createFigure() {
		EdgeFigure result = new EdgeFigure();
		result.setRoutingConstraint(constructFigureBendpointList(result));
		return result;
	}
	
	private List constructFigureBendpointList(EdgeFigure f) {
		ArrayList result = new ArrayList();
		List modelBendpoints = getEdge().getBendPoints();
		for (int i = 0; i < modelBendpoints.size(); i++) {
			BendPoint bendpoint = (BendPoint)modelBendpoints.get(i);
			RelativeBendpoint figureBendpoint = new RelativeBendpoint(f);
			figureBendpoint.setRelativeDimensions(
					bendpoint.getFirstRelativeDimension(), 
					bendpoint.getSecondRelativeDimension());
			figureBendpoint.setWeight((i + 1) / (modelBendpoints.size() + 1));
			result.add(figureBendpoint);
		}
		return result;
	}
	
	private void refreshBendpoints() {
		EdgeFigure f = (EdgeFigure)getFigure();
		f.setRoutingConstraint(constructFigureBendpointList(f));
	}
	
	protected abstract ConnectionEditPolicy getConnectionEditPolicy();
	
	protected void createEditPolicies() {
		installEditPolicy("ConnectionEditPolicy", getConnectionEditPolicy());
		installEditPolicy("Connection Endpoint Policy", new ConnectionEndpointsEditPolicy());
		installEditPolicy("Connection Bendpoint Policy", new BendpointEditPolicy());
	}

	public void activate() {
		if (!isActive()) {
			getEdge().addPropertyChangeListener(this);
			super.activate();
		}
	}
	
	public void deactivate() {
		if (isActive()) {
			getEdge().removePropertyChangeListener(this);
			super.deactivate();
		}
	}
	
	protected List getModelChildren() {
		ArrayList result = new ArrayList();
		Label label = getEdge().getLabel();
		if (label != null) {
			result.add(label);
		}
		return result;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if ("bendPointAdd".equals(propertyName) 
				|| "bendPointSet".equals(propertyName) 
				|| "bendPointRemove".equals(propertyName)) {
			refreshBendpoints();
		} else if ("target".equals(propertyName)) {
			setTarget((EditPart)getViewer().getEditPartRegistry().get(evt.getNewValue()));
			refresh();
		}
	}
	

	public AbstractNotationElement getNotationElement() {
		return (AbstractNotationElement)getModel();
	}

	public boolean testAttribute(Object target, String name, String value) {
		return false;
	}
	
	
}
