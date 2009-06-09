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


import java.io.File;
import java.net.MalformedURLException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
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
import org.jbpm.gd.jpdl.Plugin;

public class AddJbpmInstallationDialog extends StatusDialog {
	
	private static final String pluginId = Plugin.getDefault().getBundle().getSymbolicName();
	
	private static final IStatus enterNameStatus = new Status(
			Status.INFO, pluginId, 0, "Enter the name of the jBPM installation.", null);
	private static final IStatus enterLocationStatus = new Status(
			Status.INFO, pluginId, 0, "Enter the location of the jBPM installation.", null);
	private static final IStatus unExistingLocationStatus = new Status(
			Status.ERROR, pluginId, 0, "The location does not exist.", null);
	private static final IStatus nameAlreadyUsedStatus = new Status(
			Status.ERROR, pluginId, 0, "The name is already used.", null);
	private static final IStatus inValidJbpmInstallationStatus = new Status(
			Status.ERROR, pluginId, 0, "This is not a valid jBPM installation.", null);
	
	String title;
	Text nameText, locationText;
	Button locationButton;
	
	IStatus currentStatus = enterNameStatus;
	
	String name, location;
	IStatus status;
	
	public AddJbpmInstallationDialog(Shell parentShell) {
		super(parentShell);
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
		updateStatus(currentStatus);
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
		updateStatus(currentStatus);
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
		updateStatus(currentStatus);
	}	
	
	private void updateCurrentStatus() {
		if (isNameEmpty() && isLocationEmpty()) {
			currentStatus = enterNameStatus;
		} else if (isNameAlreadyUsed() && !"Edit Location".equals(title)) {
			currentStatus = nameAlreadyUsedStatus;
		} else if (isLocationEmpty()) {
			currentStatus = enterLocationStatus;
		} else if (!isLocationExisting()) {
			currentStatus = unExistingLocationStatus;
		} else if (!isValidJbpmInstallation()) {
			currentStatus = inValidJbpmInstallationStatus;
		} else if (isNameEmpty()) {
			currentStatus = enterNameStatus;
		} else {
			currentStatus = Status.OK_STATUS;
		}
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
	
	private boolean isValidJbpmInstallation() {
		return getJbpmVersionInfoFile().exists();		
	}
	
	private File getJbpmVersionInfoFile() {
		return new Path(location).append("/src/resources/gpd/version.info.xml").toFile();
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

	public String getVersion() {
		String result = "3.1";
		try {
			Document document = new SAXReader().read(getJbpmVersionInfoFile());
			result = document.getRootElement().attribute("name").getValue();
		} 
		catch (DocumentException e) {} 
		catch (MalformedURLException e) {}
		return result;
	}

}
