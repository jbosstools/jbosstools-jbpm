package org.jbpm.gd.jpdl.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.jbpm.gd.jpdl.deployment.ProcessArchiveBuilder;
import org.jbpm.gd.jpdl.deployment.ProcessArchiveDeployer;
import org.jbpm.gd.jpdl.editor.JpdlEditor;

public class DeployProcessAction extends Action {
	
	private JpdlEditor jpdlEditor;
	
	public DeployProcessAction(JpdlEditor jpdlEditor) {
		this.jpdlEditor = jpdlEditor;
	}
	
	public void run() {
		byte[] processArchive = new ProcessArchiveBuilder(jpdlEditor).build();
		if (processArchive == null) return;
		boolean success = new ProcessArchiveDeployer(jpdlEditor).deploy(processArchive);
		if (success) {
			showSuccessDialog();
		} else {
			showFailureDialog();
		}
	}
	
	private void showSuccessDialog() {
		MessageDialog dialog = new MessageDialog(
				jpdlEditor.getSite().getShell(), 
				"Deployment Successful", 
				null,
				"The process archive deployed successfully.",
				SWT.ICON_INFORMATION, 
				new String[] { "OK" }, 
				0);
		dialog.open();
	}

	private void showFailureDialog() {
		MessageDialog dialog = new MessageDialog(
				jpdlEditor.getSite().getShell(), 
				"Deployment Failed", 
				null,
				"The process archive could not be deployed.",
				SWT.ICON_ERROR, 
				new String[] { "OK" }, 
				0);
		dialog.open();
	}

}
