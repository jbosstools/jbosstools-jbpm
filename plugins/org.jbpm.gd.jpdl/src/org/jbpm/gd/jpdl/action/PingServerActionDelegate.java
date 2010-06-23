package org.jbpm.gd.jpdl.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.jbpm.gd.jpdl.editor.JpdlEditor;

public class PingServerActionDelegate implements IEditorActionDelegate {
	
	private JpdlEditor jpdlEditor;
	
	public void setActiveEditor(IAction action, IEditorPart editorPart) {
		if (editorPart != null && editorPart instanceof JpdlEditor) {
			jpdlEditor = (JpdlEditor)editorPart;
		}
	}

	public void run(IAction action) {
		if (jpdlEditor != null) {
			new PingServerAction(jpdlEditor).run();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
