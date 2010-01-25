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
package org.jbpm.gd.jpdl.wizard;

import java.io.File;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

public class ConfigureRuntimePage extends WizardPage {
	
	private static final String ENTER_NAME_MSG = "Choose a name for the jBPM 3 Runtime, e.g. 'jBPM jPDL 3.2.2'";
	private static final String ENTER_LOCATION_MSG = "Enter or search a location for the jBPM 3 Runtime";
	private static final String CONTINUE_MSG = "Press next to continue the project creation";
	private static final String UNEXISTING_LOCATION_MSG = "This location does not exist";
	private static final String INVALID_LOCATION_MSG = "This location does not contain a valid jBPM 3 runtime, please retry";
	
	Text nameText, locationText;
	
	public ConfigureRuntimePage() {
		super("Configure JBoss jBPM Runtime");
		setTitle("Configure JBoss jBPM Runtime");
		setMessage(ENTER_NAME_MSG);
	}

	public void createControl(Composite parent) {
		Composite composite = createClientArea(parent);
		createLocateJbpmRuntimeGroup(composite);
		createSpace(composite);
		createDownloadJbpmRuntimeLink(composite);
		setControl(composite);
	}
	
	private void update() {
		updateMessage();
		updatePages();
	}
	
	private void updatePages() {
		setPageComplete(!isNameEmpty() && !isLocationEmpty());
		if (!isNameEmpty()) {
			NewProcessProjectDetailsWizardPage page = (NewProcessProjectDetailsWizardPage)getNextPage();
			page.combo.removeAll();
			page.combo.add(nameText.getText());
			page.combo.select(0);
		}
	}
	
	private void createSpace(Composite composite) {
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	private void createDownloadJbpmRuntimeLink(Composite composite) {
		Link link = new Link(composite, SWT.NONE);
		link.setText("<a>Download the JBoss jBPM package if you have none available yet.</a>");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalAlignment = SWT.CENTER;
		gridData.verticalIndent = 5;
		link.setLayoutData(gridData);
		link.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Program.launch("http://labs.jboss.com/jbossjbpm/downloads/");
			}			
		});
	}
	
	private void createLocateJbpmRuntimeGroup(Composite composite) {
		Group group = new Group(composite, SWT.NONE);
		group.setText("Locate a JBoss jBPM Runtime");
		group.setLayout(new GridLayout(3, false));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalIndent = 5;
		group.setLayoutData(gridData);
		createJbpmRuntimeNameField(group);
		createJbpmRuntimeLocationField(group);
	}
	
	private void createJbpmRuntimeNameField(Composite composite) {
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText("Name :");
		nameText = new Text(composite, SWT.BORDER);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				update();			
			}
		});
		new Label(composite, SWT.NONE);
	}
	
	private void createJbpmRuntimeLocationField(Composite composite) {
		Label locationLabel = new Label(composite, SWT.NONE);
		locationLabel.setText("Location :");
		locationText = new Text(composite, SWT.BORDER);
		locationText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		locationText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				update();
			}
		});
		Button locationButton = new Button(composite, SWT.PUSH);
		locationButton.setText("Browse...");
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
	
	private Composite createClientArea(Composite parent) {
		Composite composite= new Composite(parent, SWT.NONE);
		GridLayout layout= new GridLayout();
		layout.marginWidth= 0;
		layout.marginHeight= 0;
		layout.numColumns= 1;
		composite.setLayout(layout);
		return composite;
	}
	
	private void updateMessage() {
		setErrorMessage(null);
		if (isNameEmpty() && isLocationEmpty()) {
			setMessage(ENTER_NAME_MSG);
		} else if (isLocationEmpty()) {
			setMessage(ENTER_LOCATION_MSG);
		} else if (!isLocationExisting()) {
			setErrorMessage(UNEXISTING_LOCATION_MSG);
		} else if (!isValidJbpm3Installation()) {
			setErrorMessage(INVALID_LOCATION_MSG);
		} else if (isNameEmpty()) {
			setMessage(ENTER_NAME_MSG);
		} else {
			setMessage(CONTINUE_MSG);
		}
	}
	
	private boolean isNameEmpty() {
		String text = nameText.getText();
		return text == null || "".equals(text);
	}
	
	private boolean isLocationEmpty() {
		String text = locationText.getText();
		return text == null || "".equals(text);
	}
	
	private boolean isLocationExisting() {
		return new Path(locationText.getText()).toFile().exists();
	}
	
	private boolean isValidJbpm3Installation() {
		return getJbpmVersionInfoFile().exists();		
	}
	
	private File getJbpmVersionInfoFile() {
		return new Path(locationText.getText()).append("/src/resources/gpd/version.info.xml").toFile();
	}
	
	
}
