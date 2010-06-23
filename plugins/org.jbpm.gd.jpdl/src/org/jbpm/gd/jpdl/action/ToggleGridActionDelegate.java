package org.jbpm.gd.jpdl.action;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.jbpm.gd.jpdl.editor.JpdlEditor;

public class ToggleGridActionDelegate implements IEditorActionDelegate {
	
	private GraphicalViewer graphicalViewer;
	
	public void setActiveEditor(IAction action, IEditorPart editorPart) {
		if (editorPart != null && editorPart instanceof JpdlEditor) {
			graphicalViewer = ((JpdlEditor)editorPart).getGraphicalViewer();
		}
	}

	public void run(IAction action) {
		if (graphicalViewer != null) {
			ToggleGridAction toggleGridAction = new ToggleGridAction(graphicalViewer);
			toggleGridAction.run();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
