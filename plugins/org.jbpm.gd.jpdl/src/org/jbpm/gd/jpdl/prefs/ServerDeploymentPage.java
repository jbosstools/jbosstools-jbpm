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
package org.jbpm.gd.jpdl.prefs;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jbpm.gd.jpdl.Constants;
import org.jbpm.gd.jpdl.Plugin;

public class ServerDeploymentPage extends PreferencePage implements IWorkbenchPreferencePage, Constants {
	
	private Text nameText, portText, deployerText, usernameText, passwordText;
	private Button useCredentialsCheckbox;

	public ServerDeploymentPage() {
		super();
		setPreferenceStore(Plugin.getDefault().getPreferenceStore());
	}

	protected Control createContents(Composite parent) {
		Composite clientArea = createClientArea(parent);
		createServerNameField(clientArea);
		createServerPortField(clientArea);
		createServerDeployerField(clientArea);
		createServerCredentialsCheckBox(clientArea);
		createServerCredentialsGroup(clientArea);
		return null;
	}
	
	private void createServerCredentialsGroup(Composite parent) {
		Group serverCredentialsGroup = new Group(parent, SWT.BORDER);
		serverCredentialsGroup.setLayout(new GridLayout(2, false));
		createUserNameField(serverCredentialsGroup);
		createUserPasswordField(serverCredentialsGroup);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		serverCredentialsGroup.setLayoutData(data);
	}
	
	private void createUserNameField(Composite parent) {
		Label nameLabel = new Label(parent, SWT.NORMAL);
		nameLabel.setText("Username:");
		String username = getPreferenceStore().getString("user name");
		usernameText = new Text(parent, SWT.BORDER);
		usernameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		usernameText.setText(useCredentialsCheckbox.getSelection() ? username : "");
	}
	 
	private void createUserPasswordField(Composite parent) {
		Label passwordLabel = new Label(parent, SWT.NORMAL);
		passwordLabel.setText("Password:");
		String password = getPreferenceStore().getString("password");
		passwordText = new Text(parent, SWT.PASSWORD | SWT.BORDER);
		passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		passwordText.setText(useCredentialsCheckbox.getSelection() ? password : "");
	}
	
	private void createServerCredentialsCheckBox(Composite parent) {
		useCredentialsCheckbox = new Button(parent, SWT.CHECK);
		useCredentialsCheckbox.setText("Use credentials");
		useCredentialsCheckbox.setSelection(getPreferenceStore().getBoolean("use credentials"));
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		useCredentialsCheckbox.setLayoutData(data);
		useCredentialsCheckbox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				usernameText.setEnabled(useCredentialsCheckbox.getSelection());
				passwordText.setEnabled(useCredentialsCheckbox.getSelection());
			}			
		});
	}
	
	private void createServerNameField(Composite parent) {
		Label nameLabel = new Label(parent, SWT.NORMAL);
		nameLabel.setText("Server name:");
		nameText = new Text(parent, SWT.BORDER);
		String serverName = getPreferenceStore().getString("server name");
		nameText.setText(serverName == null || "".equals(serverName) ? "localhost" : serverName);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private void createServerPortField(Composite parent) {
		Label portLabel = new Label(parent, SWT.NORMAL);
		portLabel.setText("Server port:");
		portText = new Text(parent, SWT.BORDER);
		String serverPort = getPreferenceStore().getString("server port");
		portText.setText(serverPort == null || "".equals(serverPort)? "8080" : serverPort);
		portText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private void createServerDeployerField(Composite parent) {
		Label deployerLabel = new Label(parent, SWT.NORMAL);
		deployerLabel.setText("Server deployer:");
		deployerText = new Text(parent, SWT.BORDER);
		String serverDeployer = getPreferenceStore().getString("server deployer");
		deployerText.setText(serverDeployer == null || "".equals(serverDeployer)? "/jbpm-console/upload" : serverDeployer);
		deployerText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private Composite createClientArea(Composite parent) {
		Composite clientArea = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		clientArea.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		clientArea.setLayoutData(gridData);
		return clientArea;
	}

	public void init(IWorkbench workbench) {
	}
	
	public boolean performOk() {
		getPreferenceStore().setValue("server name", nameText.getText());
		getPreferenceStore().setValue("server port", portText.getText());
		getPreferenceStore().setValue("server deployer", deployerText.getText());
		getPreferenceStore().setValue("use credentials", useCredentialsCheckbox.getSelection());
		getPreferenceStore().setValue("user name", useCredentialsCheckbox.getSelection() ? usernameText.getText() : "");
		getPreferenceStore().setValue("password", useCredentialsCheckbox.getSelection() ? passwordText.getText() : "");
		return true;
	}
	
	public void performDefaults() {
		nameText.setText("localhost");
		portText.setText("8080");
		deployerText.setText("jbpm-console/upload");
		useCredentialsCheckbox.setSelection(false);
		usernameText.setText(null);
		passwordText.setText(null);
		getPreferenceStore().setValue("server name", "localhost");
		getPreferenceStore().setValue("server port", "8080");
		getPreferenceStore().setValue("server deployer", "/jbpm-console/upload");
		getPreferenceStore().setValue("use credentials", false);
		getPreferenceStore().setValue("user name", "");
		getPreferenceStore().setValue("password", "");
	}

}
