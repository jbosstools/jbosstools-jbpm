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
import java.util.Observable;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.jface.viewers.TextCellEditor;
import org.jbpm.gd.common.figure.LabelFigure;
import org.jbpm.gd.common.notation.Label;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.policy.DirectEditPolicy;
import org.jbpm.gd.common.policy.LabelGraphicalNodeEditPolicy;
import org.jbpm.gd.common.util.CellEditorLocator;
import org.jbpm.gd.common.util.DirectEditManager;

public class LabelGraphicalEditPart 
	extends AbstractNotationElementGraphicalEditPart {
	
	private DirectEditManager manager;
	
	public LabelGraphicalEditPart(NotationElement notationElement) {
		super(notationElement);
	}
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy("DirectEditPolicy", new DirectEditPolicy());
		installEditPolicy("GraphicalNodeEditPolciy", new LabelGraphicalNodeEditPolicy());
	}
	
	protected IFigure createFigure() {
		LabelFigure result = new LabelFigure();
		result.setText(((Label)getNotationElement()).getText());
		return new LabelFigure();
	}

	protected void refreshVisuals() {
		String text = ((Label)getNotationElement()).getText();
		Polyline polyline = (Polyline)((GraphicalEditPart)getParent()).getFigure();
		Point offset = ((Label)getNotationElement()).getOffset();
		if (offset == null) {
			offset = calculateInitialOffset(polyline);
			((Label)getNotationElement()).setOffset(offset);
		}
		LabelFigure figure = (LabelFigure)getFigure();
		figure.setText(text);
		LabelConstraint constraint = 
			new LabelConstraint(text, offset, polyline);
		((GraphicalEditPart)getParent()).setLayoutConstraint(this,getFigure(),constraint);	  
	}
	
	private Point calculateInitialOffset(Polyline polyline) {
		Point result = new Point(5, -10);
		Point start = polyline.getStart();
		Point end = polyline.getEnd();
		Point mid = start.getNegated().getTranslated(end).getScaled(0.5);
		if (mid.x < -10) {
			result.y = 10;
		}
		return result;
	}
		  
	public DragTracker getDragTracker(Request request) {
		return new DragEditPartsTracker(this) {
			protected EditPart getTargetEditPart() {
				return getParent();
			}
		};
	}
		  
	private void performDirectEdit() {
		if (manager == null) {
			initializeManager();
		}
		manager.show();
	}
	
	private void initializeManager() {
		CellEditorLocator locator = new CellEditorLocator((LabelFigure)getFigure());
		manager = new DirectEditManager(this, TextCellEditor.class, locator);
	}

	public void performRequest(Request request) {
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
			performDirectEdit();
	}
	
	public void update(Observable arg0, Object arg1) {
		refreshVisuals();		
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("text")
				|| eventName.equals("offset")) {
			refreshVisuals();
		} 
	}
	
	private class LabelConstraint implements Locator {

		String text;
		Point offset;
		Polyline polyline;

		public LabelConstraint(String text, Point offset, Polyline polyline) {
			this.text = text;
			this.offset = offset;
			this.polyline = polyline;
		}

		public void relocate(IFigure figure) {
			Dimension minimum = FigureUtilities.getTextExtents(text, figure
					.getFont());
			figure.setSize(minimum);
			Point location = polyline.getPoints().getMidpoint();
			Point offsetCopy = offset.getCopy();
			offsetCopy.translate(location);
			figure.setLocation(offsetCopy);
		}
	}
	
}
