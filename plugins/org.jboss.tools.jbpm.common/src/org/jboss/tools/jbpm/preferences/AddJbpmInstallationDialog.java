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
package org.jboss.tools.jbpm.preferences;


import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AddJbpmInstallationDialog extends StatusDialog {
	
	private static final String ENTER_NAME = "Enter the name of the jBPM installation.";
	private static final String ENTER_LOCATION = "Enter the location of the jBPM installation.";
	private static final String UNEXISTING_LOCATION = "The location does not exist.";
	private static final String NAME_ALREADY_USED = "The name is already used.";
	private static final String INVALID_JBPM_INSTALLATION = "This is not a valid jBPM installation.";
	
	private Plugin plugin;
	
	String title;
	Text nameText, locationText;
	Button locationButton;
	
	IStatus currentStatus; ;
	
	String name, location;
	IStatus status;
	
	public AddJbpmInstallationDialog(Shell parentShell, Plugin plugin) {
		super(parentShell);
		this.plugin = plugin;
	}
	
	public void initialize(String t, String n, String l) {
		this.title = t;
		this.name = n;
		this.location = l;
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite)super.createDialogArea(parent);
		getShell().setText(title);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		area.setLayout(gridLayout);
		createNameLabel(area);
		createNameText(area);
		createFillLabel(area);
		createLocationLabel(area);
		createLocationText(area);
		createLocationButton(area);
		getShell().setText(title);
		return area;
	}
	
	protected Control createContents(Composite parent) {
		Control result = super.createContents(parent);
		updateCurrentStatus();
		return result;
	}
	
	private void createLocationText(Composite area) {
		locationText = new Text(area, SWT.BORDER);		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = convertWidthInCharsToPixels(40);
		locationText.setLayoutData(gridData);
		locationText.setText(location == null ? "" : location);
		locationText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleLocationChanged();			
			}			
		});
	}
	
	private void handleLocationChanged() {
		location = locationText.getText();
		updateCurrentStatus();
	}
	
	private void createLocationButton(Composite area) {
		locationButton = new Button(area, SWT.PUSH);
		locationButton.setText("Search...");
		locationButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				searchLocation();				
			}
		});
	}
	
	private void searchLocation() {
		DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.OPEN);
		String result = dialog.open();
		if (result != null) {
			locationText.setText(result);
		}		
	}
	
	private void createLocationLabel(Composite area) {
		Label label = new Label(area, SWT.NONE);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		label.setLayoutData(gridData);
		label.setText("Location :");		
	}
	
	private void createFillLabel(Composite area) {
		new Label(area, SWT.NONE);
	}
	
	private void createNameText(Composite area) {
		nameText = new Text(area, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.verticalIndent = 10;
		gridData.widthHint = convertWidthInCharsToPixels(40);
		nameText.setLayoutData(gridData);
		nameText.setText(name == null ? "" : name);
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				handleNameChanged();			
			}
		});
	}
	
	private void handleNameChanged() {
		name = nameText.getText();
		updateCurrentStatus();
	}	
	
	private IStatus getStatus(int severity, String str) {
		return new Status(severity, plugin.getBundle().getSymbolicName(), str, null);
	}
	
	private void updateCurrentStatus() {
		if (isNameEmpty() && isLocationEmpty()) {
			currentStatus = getStatus(Status.INFO, ENTER_NAME);
		} else if (isNameAlreadyUsed() && !"Edit Location".equals(title)) {
			currentStatus = getStatus(Status.ERROR, NAME_ALREADY_USED);
		} else if (isLocationEmpty()) {
			currentStatus = getStatus(Status.INFO, ENTER_LOCATION);
		} else if (!isLocationExisting()) {
			currentStatus = getStatus(Status.ERROR, UNEXISTING_LOCATION);
		} else if (!isValidJbpmInstallation()) {
			currentStatus = getStatus(Status.ERROR, INVALID_JBPM_INSTALLATION);
		} else if (isNameEmpty()) {
			currentStatus = getStatus(Status.INFO, ENTER_NAME);
		} else {
			currentStatus = Status.OK_STATUS;
		}
		updateStatus(currentStatus);
	}
	
	private boolean isNameAlreadyUsed() {
		return PreferencesManager.INSTANCE.getJbpmInstallation(nameText.getText()) != null;
	}
	
	private boolean isLocationExisting() {
		return new Path(location).toFile().exists();
	}
	
	private boolean isNameEmpty() {
		return name == null || "".equals(name);
	}
	
	private boolean isLocationEmpty() {
		return location == null || "".equals(location);
	}
		
	protected void updateButtonsEnableState(IStatus status) {
		Button ok = getButton(IDialogConstants.OK_ID);
		if (ok != null && !ok.isDisposed())
			ok.setEnabled(status.getSeverity() == IStatus.OK);
	}	
	
	private void createNameLabel(Composite area) {
		Label label = new Label(area, SWT.NONE);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		label.setLayoutData(gridData);
		label.setText("Name :");
	}
	
	public String getName() {
		return name;
	}
	
	public String getLocation() {
		return location;
	}

	private boolean isJbpm3() {
		return new Path(location).append("/src/resources/gpd/version.info.xml").toFile().exists();
	}
	
	private boolean isJbpm4() {
		return new Path(getLocation()).append("/jbpm.jar").toFile().exists();
	}
	
	protected String getVersion() {
		if (isJbpm3()) {
			return "jBPM3";
		} else if (isJbpm4()) {
			return "jBPM4";
		} else {
			return "unknown";
		}
	}
	protected boolean isValidJbpmInstallation() {
		return isJbpm3() || isJbpm4();
	}

}
