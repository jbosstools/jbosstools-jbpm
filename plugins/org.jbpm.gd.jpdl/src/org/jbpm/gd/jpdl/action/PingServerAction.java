package org.jbpm.gd.jpdl.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.jbpm.gd.jpdl.deployment.ProcessArchiveDeployer;
import org.jbpm.gd.jpdl.editor.JpdlEditor;

public class PingServerAction extends Action {
	
	private JpdlEditor jpdlEditor;
	
	public PingServerAction(JpdlEditor jpdlEditor) {
		this.jpdlEditor = jpdlEditor;
	}
	
	public void run() {
		boolean success = new ProcessArchiveDeployer(jpdlEditor).pingServer();
		if (success) {
			showSuccessDialog();
		}
	}
	
	private void showSuccessDialog() {
		MessageDialog dialog = new MessageDialog(
				jpdlEditor.getSite().getShell(), 
				"Ping Server Successful", 
				null,
				"The server could be reached successfully.",
				SWT.ICON_INFORMATION, 
				new String[] { "OK" }, 
				0);
		dialog.open();
	}

}
