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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class OutlineViewer extends ContentOutlinePage {
	
	private Control treeview;
	private Editor editor;
	
	public OutlineViewer(Editor editor) {
		super(new TreeViewer());
		editor.getSelectionSynchronizer().addViewer(getViewer());
		this.editor = editor;
	}
	
	protected abstract void initEditPartFactory();
	
	public void createControl(Composite parent) {
		createTreeview(parent);
	}
		
	private void createTreeview(Composite parent) {
		treeview = getViewer().createControl(parent);
		ContextMenuProvider provider = new ContextMenuProvider(getViewer(), editor.getActionRegistry());
		getViewer().setContextMenu(provider);
		getSite().registerContextMenu("org.jbpm.gd.common.outline.context", provider, getViewer());
		treeview.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				handleDoubleClick();				
			}
		});
		getSite().setSelectionProvider(getViewer());
		getViewer().setEditDomain(editor.getGraphicalViewer().getEditDomain());
		initEditPartFactory(); 
		setContents(editor.getRootContainer().getSemanticElement());
	}
	
	private void handleDoubleClick() {
		EditPart editPart = (EditPart)getViewer().getSelectedEditParts().get(0);
		Request openPropertiesRequest = new Request(RequestConstants.REQ_OPEN);
		editPart.performRequest(openPropertiesRequest);
	}

//	public CommandStack getCommandStack() {
//		return editor.getCommandStack();
//	}
//	
	public Editor getEditor() {
		return editor;
	}

	public Control getControl() {
		return treeview;
	}

	public void setFocus() {
		if (getControl() != null) {
			getControl().setFocus();
		}
	}

	public void setContents(Object contents) {
		getViewer().setContents(contents);
	}
	
	protected void setEditPartFactory(EditPartFactory factory) {
		getViewer().setEditPartFactory(factory);
	}

}
