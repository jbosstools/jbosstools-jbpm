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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

public class GraphPage extends EditorPart {
	
	private Editor editor;
	private GraphicalViewer graphicalViewer;
	private OutlineViewer outlineViewer;

	public GraphPage(Editor editor) {
		this.editor = editor;
	}
	
	public void createPartControl(Composite parent) {
		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		addPalette(sashForm);
		addModelViewer(sashForm);
		addOutlineViewer();
		sashForm.setWeights(new int[] {15, 85});
	}
	
	private void addOutlineViewer() {
		outlineViewer = editor.createOutlineViewer();
	}
	
	private void addModelViewer(Composite composite) {
		graphicalViewer = editor.createGraphicalViewer();
		graphicalViewer.initControl(composite);
		getSite().setSelectionProvider(graphicalViewer);
	}
	
	private void addPalette(Composite composite) {	
		PaletteViewer paletteViewer = new PaletteViewer();
		paletteViewer.createControl(composite);
		editor.getEditDomain().setPaletteViewer(paletteViewer);
		editor.getEditDomain().setPaletteRoot(new PaletteRoot(editor));
	}
	
	public void setFocus() {	
	}
	
	public void doSave(IProgressMonitor monitor) {
		
		// TODO repair doSave method
		SWTGraphics g = null;
		GC gc = null;
		Image image = null;
	
		LayerManager lm = (LayerManager)graphicalViewer.getEditPartRegistry().get(LayerManager.ID);
		IFigure figure = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
		
		try {
		
		    Rectangle r = figure.getBounds();
		    editor.getRootContainer().setDimension(new Dimension(r.width, r.height));
			image = new Image(Display.getDefault(), r.width, r.height);
	        gc = new GC(image);
	        g = new SWTGraphics(gc);
	        g.translate(r.x * -1, r.y * -1);
	        figure.paint(g);
	        ImageLoader imageLoader = new ImageLoader();
	        imageLoader.data = new ImageData[] {image.getImageData()};
	        imageLoader.save(getImageSavePath(), SWT.IMAGE_JPEG);
	        refreshProcessFolder();
	        
	    } finally {
	        if (g != null) {
	            g.dispose();
	        }
	        if (gc != null) {
	            gc.dispose();
	        }
	        if (image != null) {
	            image.dispose();
	        }
	    }
	    
	}
	
	private void refreshProcessFolder() {
		try {
			IFile file = ((FileEditorInput)getEditorInput()).getFile();
			file.getParent().refreshLocal(1, null);			
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public String getImageSavePath() {
		IFile file = ((FileEditorInput)getEditorInput()).getFile();
		String name = editor.getContentProvider().getDiagramImageFileName(file.getName());
		IPath path = file.getRawLocation().removeLastSegments(1).append(name);
		return path.toOSString();
	}
	
	public void doSaveAs() {
	}

	public boolean isDirty() {
		return false;
	}
	
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}
	
	public GraphicalViewer getDesignerModelViewer() {
		return graphicalViewer;
	}
	
	public OutlineViewer getOutlineViewer() {
		return outlineViewer;
	}
	
	public Editor getEditor() {
		return editor;
	}
	
}
