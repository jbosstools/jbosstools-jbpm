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

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.jbpm.gd.common.notation.RootContainer;
import org.jbpm.gd.common.policy.XYLayoutEditPolicy;

public abstract class RootContainerGraphicalEditPart 
extends AbstractNotationElementGraphicalEditPart { 
	
	public RootContainerGraphicalEditPart(RootContainer rootContainer) {
		super(rootContainer);
	}
	
	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer();
		layer.setLayoutManager(new FreeformLayout());
		layer.setBorder(new LineBorder(1));
		return layer;
	}
	
	protected List getModelChildren() {
		return ((RootContainer)getNotationElement()).getNodes();
	}
	
	protected abstract XYLayoutEditPolicy createLayoutEditPolicy();
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy("LayoutEditPolicy", createLayoutEditPolicy());
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		String eventType = evt.getPropertyName();
		if ("nodeAdd".equals(eventType) || "nodeRemove".equals(eventType)) {
			refreshChildren();
		}
	}

	public Object getAdapter(Class adapter) {
		if (adapter == SnapToHelper.class) {
			return constructSnapToHelper();
		}
		return super.getAdapter(adapter);
	}

	private Object constructSnapToHelper() {
		Boolean val = (Boolean)getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
		if (val != null && val.booleanValue()) {
			return new SnapToGrid(this);
		} else {
			return null;
		}		
	}

}
