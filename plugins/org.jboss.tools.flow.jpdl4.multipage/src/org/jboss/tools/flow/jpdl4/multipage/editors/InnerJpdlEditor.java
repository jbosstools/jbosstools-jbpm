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

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.tools.flow.jpdl4.editor.JpdlEditor;

/**
 * the jpdl editor is only for the multipage editor. It extends jpdl editor and
 * do some small changes for adapting to the multipage editor.
 * 
 * @author Grid Qian
 */
public class InnerJpdlEditor extends JpdlEditor {

	public String JpdlEditorID = "org.jboss.tools.flow.jpdl4.editor";
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IEditorPart activeEditor = getSite().getPage().getActiveEditor();
		if (activeEditor instanceof MultiPageEditor) {
			MultiPageEditor mulEditor = (MultiPageEditor) activeEditor;

			if (mulEditor.getActiveInnerEditor() == this)
				updateActions(super.getSelectionActions());
		}
		super.selectionChanged(part, selection); 
	}
	
    public void writeModel(OutputStream os) throws IOException {
        super.writeModel(os);
    }
	public void setModel(Object model) {
		super.setModel(model);
	}
	
	public String getContributorId() {
		return JpdlEditorID;
	}
}
