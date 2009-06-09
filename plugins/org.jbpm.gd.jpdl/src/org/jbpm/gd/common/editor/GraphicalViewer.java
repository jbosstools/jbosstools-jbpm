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
package org.jbpm.gd.common.editor;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.jbpm.gd.jpdl.Constants;


public abstract class GraphicalViewer extends ScrollingGraphicalViewer {
	
	private Editor editor;
	
	public GraphicalViewer(Editor editor) {
		this.editor = editor;
		editor.getSelectionSynchronizer().addViewer(this);
		setKeyHandler(new GraphicalViewerKeyHandler(this));
		setRootEditPart(new ScalableFreeformRootEditPart());
		prepareGrid();
	}
	
	protected abstract void initEditPartFactory();

	private void prepareGrid() {
		getLayerManager().getLayer(LayerConstants.GRID_LAYER).setForegroundColor(Constants.veryLightBlue);
		editor.getActionRegistry().registerAction(new ToggleGridAction(this));
	}
	
	public void initControl(Composite parent) {
		super.createControl(parent);
		getControl().setBackground(ColorConstants.white);
		ContextMenuProvider provider = new ContextMenuProvider(this, editor.getActionRegistry());
		setContextMenu(provider);
		editor.getSite().registerContextMenu("org.jbpm.gd.common.graph.context", provider, this);
		editor.getEditDomain().addViewer(this);
		initEditPartFactory();
		setContents(editor.getRootContainer());
	}
	
	public Dimension getDimension() {
		Rectangle rectangle = getControl().getBounds();
		return new Dimension(rectangle.width, rectangle.height);
	}
	
	public FigureCanvas getFigureCanvas() {
		return super.getFigureCanvas();
	}
	
	public Editor getEditor() {
		return editor;
	}
	
}
