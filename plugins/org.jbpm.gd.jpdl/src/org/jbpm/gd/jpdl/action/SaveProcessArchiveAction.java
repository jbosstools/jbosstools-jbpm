package org.jbpm.gd.jpdl.action;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.jbpm.gd.jpdl.deployment.ProcessArchiveBuilder;
import org.jbpm.gd.jpdl.editor.JpdlEditor;

public class SaveProcessArchiveAction extends Action {
	
	private JpdlEditor jpdlEditor;
	
	public SaveProcessArchiveAction(JpdlEditor jpdlEditor) {
		this.jpdlEditor = jpdlEditor;
	}
	
	public void run() {
		try {
			byte[] processArchive = new ProcessArchiveBuilder(jpdlEditor).build();
			if (processArchive == null) return;
			SaveAsDialog saveAsDialog = new SaveAsDialog(jpdlEditor.getSite().getShell());
			saveAsDialog.open();
			IPath path = saveAsDialog.getResult();
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (!file.exists()) {
				file.create(new ByteArrayInputStream(new byte[0]), IResource.NONE, null);
			} else {
				if (!askIfOverwriteAllowed()) {
					return;
				}
			}
			file.setContents(new ByteArrayInputStream(processArchive), IResource.NONE, null);
		} catch (CoreException e) {
			showSaveProcessArchiveException();
		}
	}
	
	private void showSaveProcessArchiveException() {
		MessageDialog dialog = new MessageDialog(
				jpdlEditor.getSite().getShell(), 
				"Save Process Archive Failed", 
				null,
				"The process archive could not be saved.",
				SWT.ICON_ERROR, 
				new String[] { "OK" }, 
				0);
		dialog.open();
	}
	
	private boolean askIfOverwriteAllowed() {
		MessageDialog dialog = new MessageDialog(
				jpdlEditor.getSite().getShell(), 
				"Overwrite Existing File?", 
				null,
				"The chosen file exists. Is it OK to overwrite the contents?",
				SWT.ICON_QUESTION, 
				new String[] { "Yes", "No" }, 
				0);
		return (dialog.open() == 0);
	}

}
