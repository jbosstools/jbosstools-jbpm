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
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.jbpm.gd.common.figure.AbstractNodeFigure;
import org.jbpm.gd.common.figure.NodeFigureFactory;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.policy.ComponentEditPolicy;
import org.jbpm.gd.common.policy.GraphicalNodeEditPolicy;

public abstract class AbstractNodeGraphicalEditPart 
	extends AbstractNotationElementGraphicalEditPart  
	implements NodeEditPart {
	
	public AbstractNodeGraphicalEditPart(NotationElement notationElement) {
		super(notationElement);
	}
	
	protected IFigure createFigure() {
		return NodeFigureFactory.INSTANCE.createFigure((Node)getNotationElement());
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		String eventType = evt.getPropertyName();
		if ("constraint".equals(eventType) || "name".equals(eventType)) {
			refreshVisuals();
		} else if ("leavingEdgeAdd".equals(eventType) 
				|| "leavingEdgeRemove".equals(eventType)
				|| "leavingEdgeRefresh".equals(eventType)) {
			refreshSourceConnections();
		} else if ("arrivingEdgeAdd".equals(eventType)
				|| "arrivingEdgeRemove".equals(eventType)
				|| "arrivingEdgeRefresh".equals(eventType)) {
			refreshTargetConnections();
		} 
	}
	
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart arg0) {
		return getNodeFigure().getLeavingConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart arg0) {
		return getNodeFigure().getArrivingConnectionAnchor();
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request arg0) {
		return getNodeFigure().getLeavingConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request arg0) {
		return getNodeFigure().getArrivingConnectionAnchor();
	}

	protected List getModelSourceConnections() {
		return ((Node)getNotationElement()).getLeavingEdges();
	}
	
	protected List getModelTargetConnections() {
		return ((Node)getNotationElement()).getArrivingEdges();
	}
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy("ComponentEditPolicy", createComponentEditPolicy());
		installEditPolicy("GraphicalNodeEditPolciy", createGraphicalNodeEditPolicy());
	}
	
	protected abstract ComponentEditPolicy createComponentEditPolicy();
	protected abstract GraphicalNodeEditPolicy createGraphicalNodeEditPolicy();
	
	private AbstractNodeFigure getNodeFigure() {
		return (AbstractNodeFigure)getFigure();
	}

}
