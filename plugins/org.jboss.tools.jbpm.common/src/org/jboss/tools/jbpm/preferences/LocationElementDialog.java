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


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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

public class LocationElementDialog extends Dialog {
	
	String title;
	Text nameText, locationText;
	Button locationButton;
	
	String name, location;
	
	public LocationElementDialog(Shell parentShell) {
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
		createMessageLabel(area);
		createNameLabel(area);
		createNameText(area);
		createFillLabel(area);
		createLocationLabel(area);
		createLocationText(area);
		createLocationButton(area);
		createSeparator(area);
		getShell().setText(title);
		return area;
	}
	
	protected Control createContents(Composite parent) {
		Control result = super.createContents(parent);
		updateOkButton();
		return result;
	}
	
	private void createSeparator(Composite area) {
		Label separator = new Label(area, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		gridData.verticalIndent = 15;
		separator.setLayoutData(gridData);
	}
	
	private void createLocationText(Composite area) {
		locationText = new Text(area, SWT.BORDER);		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = convertWidthInCharsToPixels(40);
		locationText.setLayoutData(gridData);
		locationText.setText(location == null ? "" : location);
		locationText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				location = locationText.getText();
				updateOkButton();			
			}			
		});
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
		gridData.widthHint = convertWidthInCharsToPixels(40);
		nameText.setLayoutData(gridData);
		nameText.setText(name == null ? "" : name);
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				name = nameText.getText();
				updateOkButton();			
			}			
		});
	}
	
	private void updateOkButton() {
		getButton(IDialogConstants.OK_ID).setEnabled(isValid());
	}
	
	private boolean isValid() {
		if (name == null || location == null) {
			return false;
		}
		if ("".equals(name) || "".equals(location)) {
			return false;
		}
		return true;
	}
	
	private void createNameLabel(Composite area) {
		Label label = new Label(area, SWT.NONE);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		label.setLayoutData(gridData);
		label.setText("Name :");
	}

	private void createMessageLabel(Composite area) {
		Label label = new Label(area, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);
		gridData.horizontalSpan = 3;
		gridData.verticalIndent = 10;
		label.setLayoutData(gridData);
		label.setText("Enter a name and choose the location : ");
	}
	
	public String getName() {
		return name;
	}
	
	public String getLocation() {
		return location;
	}
	
	

}
