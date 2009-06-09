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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.ExposeHelper;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.MouseWheelHelper;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.editparts.ViewportAutoexposeHelper;
import org.eclipse.gef.editparts.ViewportExposeHelper;
import org.eclipse.gef.editparts.ViewportMouseWheelHelper;
import org.jbpm.gd.common.figure.NodeContainerFigure;
import org.jbpm.gd.common.figure.NodeFigureFactory;
import org.jbpm.gd.common.notation.AbstractNodeContainer;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.policy.ContainerHighlightEditPolicy;
import org.jbpm.gd.common.policy.XYLayoutEditPolicy;

public abstract class NodeContainerGraphicalEditPart 
	extends AbstractNodeGraphicalEditPart  
	implements NodeEditPart {
	
	public NodeContainerGraphicalEditPart(NotationElement notationElement) {
		super(notationElement);
	}
	
	protected IFigure createFigure() {
		return NodeFigureFactory.INSTANCE.createFigure((AbstractNodeContainer)getNotationElement());
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		String eventType = evt.getPropertyName();
		if ("constraint".equals(eventType) || "name".equals(eventType)) {
			refreshVisuals();
		} else if ("leavingEdgeAdd".equals(eventType) 
				|| "leavingEdgeRemove".equals(eventType)) {
			refreshSourceConnections();
		} else if ("arrivingEdgeAdd".equals(eventType)
				|| "arrivingEdgeRemove".equals(eventType)) {
			refreshTargetConnections();
		} else if ("nodeAdd".equals(eventType)
				|| "nodeRemove".equals(eventType)){
			refreshChildren();
		}
	}
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy("LayoutEditPolicy", createXYLayoutEditPolicy());
		installEditPolicy("Selection Feedback", new ContainerHighlightEditPolicy());
	}
	
	public void refreshVisuals() {
		Rectangle constraint = null;
		if (((Node)getNotationElement()).getConstraint() != null) {
			constraint = new Rectangle(((Node)getNotationElement()).getConstraint());
		} else {
			constraint = new Rectangle(new Point(0, 0), new Dimension(-1, -1));			
		}
		((GraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), constraint);
	}
	
	protected List getModelChildren() {
		return ((AbstractNodeContainer)getNotationElement()).getNodes();
	}
	
	public Object getAdapter(Class key) {
		if (key == AutoexposeHelper.class)
			return new ViewportAutoexposeHelper(this);
		if (key == ExposeHelper.class)
			return new ViewportExposeHelper(this);
		if (key == MouseWheelHelper.class)
			return new ViewportMouseWheelHelper(this);
		return super.getAdapter(key);
	}

	public IFigure getContentPane() {
		return ((NodeContainerFigure)getFigure()).getContentPane();
	}
	
	protected abstract XYLayoutEditPolicy createXYLayoutEditPolicy();

}
