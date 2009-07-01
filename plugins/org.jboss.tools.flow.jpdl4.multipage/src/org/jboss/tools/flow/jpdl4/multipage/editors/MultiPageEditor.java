/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.flow.jpdl4.multipage.editors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.io.JpdlDeserializer;
import org.jboss.tools.flow.jpdl4.multipage.message.MultiPageMessages;

/**
 * @author Grid Qian
 */
public class MultiPageEditor extends MultiPageEditorPart implements
		IResourceChangeListener{

	private InnerJpdlEditor jpdlEditor;

	private StructuredTextEditor xmlEditor;

	/**
	 * Creates a multi-page editor.
	 */
	public MultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	
	public void init(IEditorSite site, IEditorInput input)throws PartInitException{
		super.init(site, input);
	}

	void createJpdl4EditorPage() {
		try {
			jpdlEditor = new InnerJpdlEditor();
			int pageNum = addPage((IEditorPart) jpdlEditor, getEditorInput());
			super.setPageText(pageNum, MultiPageMessages.Jpdl_Page_Name);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating Jpdl editor", null, e.getStatus());
		}
	}

	void createXmlEditorPage() {
		try {
			xmlEditor = new StructuredTextEditor();
			int pageNum = addPage((IEditorPart) xmlEditor, getEditorInput());
			super.setPageText(pageNum, MultiPageMessages.Xml_Page_Name);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating xml editor", null, e.getStatus());
		}
	}

	protected void createPages() {
		createJpdl4EditorPage();
		createXmlEditorPage();	
		super.setPartName(jpdlEditor.getPartName());
	}
	
	public void pageChange(int newPageIndex) {
		if(this.isDirty()){
			if(this.getActiveEditor() instanceof StructuredTextEditor){
				doPageChangeFromJpdlToXml();
			} else {
				doPageChangeFromXmlToJpdl();
			}
		}
		super.pageChange(newPageIndex);
	}
	
	private void doPageChangeFromXmlToJpdl() {
		String xmlText = xmlEditor.getDocumentProvider().getDocument(xmlEditor.getEditorInput()).get();
		ByteArrayInputStream in = new ByteArrayInputStream(xmlText.getBytes());
		Wrapper model = JpdlDeserializer.deserialize(in);
		if(model != null && model.getElement() != null) {
			jpdlEditor.setModel(model);
			((GraphicalViewer) jpdlEditor.getAdapter(GraphicalViewer.class)).setContents(model);
		}		 
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void doPageChangeFromJpdlToXml() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			jpdlEditor.writeModel(out);
			xmlEditor.getDocumentProvider().getDocument(xmlEditor.getEditorInput()).set(out.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/*
	 * return the active editor of the multipage editor
	 */
	public IEditorPart getActiveInnerEditor(){
		return getActiveEditor();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if(this.getActiveEditor() instanceof StructuredTextEditor){
			doPageChangeFromXmlToJpdl();
		}
		jpdlEditor.doSave(monitor);
		xmlEditor.doSave(monitor);
	}

	@Override
	public void doSaveAs() {
		if(this.getActiveEditor() instanceof StructuredTextEditor){
			doPageChangeFromXmlToJpdl();
		}
		jpdlEditor.doSaveAs();
		xmlEditor.doSave(null);

	}

	@Override
	public boolean isSaveAsAllowed() {
		return jpdlEditor.isSaveAsAllowed();
	}

	public void resourceChanged(IResourceChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
