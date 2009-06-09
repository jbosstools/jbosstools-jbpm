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

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Text;
import org.jbpm.gd.common.figure.NodeFigure;
import org.jbpm.gd.common.model.NamedElement;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.policy.DirectEditPolicy;
import org.jbpm.gd.common.util.CellEditorLocator;

public abstract class NodeGraphicalEditPart 
	extends AbstractNodeGraphicalEditPart  
	implements NodeEditPart {
	
	private DirectEditManager manager;
	
	public NodeGraphicalEditPart(NotationElement notationElement) {
		super(notationElement);
	}
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy("DirectEditPolicy", new DirectEditPolicy());
	}
	
	private String getSemanticElementLabel() {
		String result = ((NamedElement)getSemanticElement()).getName();
		if (result == null) {
			result = getSemanticElement().getNamePrefix();
		}
		return result;
	}
	
	public void refreshVisuals() {
		getNodeFigure().setName(getSemanticElementLabel());
		Rectangle constraint = null;
		if (((Node)getNotationElement()).getConstraint() != null) {
			constraint = new Rectangle(((Node)getNotationElement()).getConstraint());
		} else {
			constraint = new Rectangle(new Point(0, 0), new Dimension(-1, -1));			
		}
		((GraphicalEditPart)getParent()).setLayoutConstraint(this, getFigure(), constraint);
	}
	
	private NodeFigure getNodeFigure() {
		return (NodeFigure)getFigure();
	}

	private void performDirectEdit() {
		if (getNodeFigure().getNameLabel() == null) return;
		if (manager == null) {
			initializeManager();
		}
		manager.show();
	}
	
	private void initializeManager() {
		CellEditorLocator locator = new CellEditorLocator(getNodeFigure().getNameLabel());
		manager = new DirectEditManager(this, TextCellEditor.class, locator) {
			protected void initCellEditor() {
				Text text = (Text) getCellEditor().getControl();
				String name = ((NamedElement)getSemanticElement()).getName();
				getCellEditor().setValue(name);
				text.selectAll();
			}			
		};
	}

	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			performDirectEdit();
		} else {
			super.performRequest(request);
		}
	}

}
