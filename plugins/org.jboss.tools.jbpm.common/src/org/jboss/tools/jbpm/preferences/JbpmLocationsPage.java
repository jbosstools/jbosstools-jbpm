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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.jbpm.Activator;
import org.jboss.tools.jbpm.Constants;
import org.jboss.tools.jbpm.util.AutoResizeTableLayout;

public class JbpmLocationsPage extends PreferencePage implements IWorkbenchPreferencePage {
	
	private TableViewer tableViewer;
	private Button addButton, editButton, removeButton;
	
	public JbpmLocationsPage() {
		super();
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	protected AddJbpmInstallationDialog createAddJbpmInstallationDialog(Shell shell) {
		 return new AddJbpmInstallationDialog(shell, Activator.getDefault());
	}

	protected Control createContents(Composite parent) {
		Composite clientArea = createClientArea(parent);
		createLabels(clientArea);
		createJbpmHomeListTable(clientArea);		
		createButtons(clientArea);
		return null;
	}
	
	private void createButtons(Composite parent) {
		GridData gridData = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
		gridData.widthHint = 80;
		addButton = new Button(parent, SWT.PUSH);
		addButton.setText("Add...");
		addButton.setLayoutData(gridData);
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addNewLocation();
			}
		});
		editButton = new Button(parent, SWT.PUSH);
		editButton.setText("Edit...");
		editButton.setLayoutData(gridData);
		editButton.setEnabled(false);
		editButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editLocation();				
			}
		});
		removeButton = new Button(parent, SWT.PUSH);
		removeButton.setText("Remove");
		removeButton.setLayoutData(gridData);
		removeButton.setEnabled(false);
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeLocation();				
			}
		});
	}
	
	private void removeLocation() {
		TableItem item = tableViewer.getTable().getSelection()[0];
		String name = item.getText(0);
		PreferencesManager inputManager = 
			(PreferencesManager)tableViewer.getInput();
		JbpmInstallation installation = inputManager.getJbpmInstallation(name);
		inputManager.getJbpmInstallationMap().remove(name);
		tableViewer.remove(installation);		
		updateButtons();
		updateStatus();
	}
	
	private void editLocation() {
		TableItem item = tableViewer.getTable().getSelection()[0];
		String name = item.getText(0);
		PreferencesManager inputManager = 
			(PreferencesManager)tableViewer.getInput();
		JbpmInstallation jbpmInstallation = inputManager.getJbpmInstallation(name);
		AddJbpmInstallationDialog dialog = createAddJbpmInstallationDialog(getShell());
		dialog.initialize("Edit Location", jbpmInstallation.name, jbpmInstallation.location);
		if (dialog.open() == IDialogConstants.OK_ID) {
			jbpmInstallation.name = dialog.getName();
			jbpmInstallation.version = dialog.getVersion();
			jbpmInstallation.location = dialog.getLocation();
			tableViewer.update(jbpmInstallation, null);
			if (name != dialog.getName()) {
				inputManager.getJbpmInstallationMap().remove(name);
				inputManager.getJbpmInstallationMap().put(dialog.getName(), jbpmInstallation);
			}
		}		
		updateButtons();
	}
	
	private void addNewLocation() {
		AddJbpmInstallationDialog dialog = createAddJbpmInstallationDialog(getShell());
		dialog.initialize("Add Location", "", "");
		if (dialog.open() == IDialogConstants.OK_ID) {
			PreferencesManager inputManager = 
				(PreferencesManager)tableViewer.getInput();
			JbpmInstallation jbpmInstallation = getJbpmInstallation(dialog);
			inputManager.getJbpmInstallationMap().put(jbpmInstallation.name, jbpmInstallation);
			tableViewer.add(jbpmInstallation);
		}
		updateButtons();
	}

	private JbpmInstallation getJbpmInstallation(AddJbpmInstallationDialog dialog) {
		JbpmInstallation jbpmInstallation = new JbpmInstallation();
		jbpmInstallation.name = dialog.getName();
		jbpmInstallation.location = dialog.getLocation();
		jbpmInstallation.version = dialog.getVersion();
		return jbpmInstallation;
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
	
	private void createJbpmHomeListTable(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.CHECK);
		tableViewer.setContentProvider(new JbpmInstallationTableContentProvider());
		tableViewer.setLabelProvider(new JbpmInstallationLabelProvider());
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalSpan = 3;
		gridData.heightHint = 200;
		tableViewer.getControl().setLayoutData(gridData);
		initializeTable(tableViewer);
		createTableColumns(tableViewer);
		initializeInput(tableViewer);
	}
	
	private void initializeInput(TableViewer viewer) {
		viewer.setInput(PreferencesManager.INSTANCE);
		checkItemToCheck(viewer);
	}

	private void checkItemToCheck(TableViewer viewer) {
		String name = Activator.getDefault().getPreferenceStore().getString(Constants.JBPM_NAME);
		if (name != null) {
			TableItem tableItem = getItemToCheck(viewer, name);
			if (tableItem != null) {
				tableItem.setChecked(true);
			}
		}
	}
	
	private TableItem getItemToCheck(TableViewer viewer, String name) {
		TableItem[] items = viewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			if (name.equals(items[i].getText(0))) return items[i];
		}
		return null;
	}
	
	private void createTableColumns(TableViewer viewer) {
		Table table = viewer.getTable();
		TableColumn nameColumn = new TableColumn(table, SWT.LEFT);
		nameColumn.setText("Name");
		ColumnWeightData nameColumnData = new ColumnWeightData(30);
		AutoResizeTableLayout layout = (AutoResizeTableLayout)table.getLayout();
		layout.addColumnData(nameColumnData);
		TableColumn locationColumn = new TableColumn(table, SWT.LEFT);
		locationColumn.setText("Location");
		ColumnWeightData locationColumnData = new ColumnWeightData(70);
		layout.addColumnData(locationColumnData);
	}
	
    private void initializeTable(TableViewer viewer) {
        Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setLayout(new AutoResizeTableLayout(table));
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateSelection(e);
			}
		});
    }
	
	private void updateSelection(SelectionEvent e) {
		if (e.detail == SWT.CHECK) {
			updateChecks(e);
		} else {
			updateButtons();
		}
		updateStatus();
	}
	
	private void updateChecks(SelectionEvent e) {
		Table table = tableViewer.getTable();
		TableItem[] items = table.getItems();
		for (int i = 0; i < items.length; i++) {
			if (e.item != items[i]) {
				items[i].setChecked(false);
			}
		}
	}
	
	private void updateStatus() {
		TableItem item = getCheckedItem();
		if (item != null) {
			setErrorMessage(null);
			setValid(true);
		} else {
			setErrorMessage("Select a default jBPM installation.");
			setValid(false);
		}
	}
	
	private TableItem getCheckedItem() {
		TableItem[] items = tableViewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getChecked()) {
				return items[i];
			}
		}
		return null;
	}
	
	private void updateButtons() {
		Table table = tableViewer.getTable();
		TableItem[] selection = table.getSelection();
		editButton.setEnabled(selection.length == 1);
		removeButton.setEnabled(selection.length == 1 && table.getItemCount() > 1);
	}
    
	private void createLabels(Composite parent) {
		GridData infoLabelGridData = new GridData(GridData.FILL_HORIZONTAL);
		infoLabelGridData.horizontalSpan = 2;
		Label infoLabel = new Label(parent, SWT.NONE);
		infoLabel.setText(
				"Add, remove or edit JBoss jBPM installation locations.\n" +
				"The checked location will be used by the jBPM creation wizards.");
		infoLabel.setLayoutData(infoLabelGridData);
		GridData tableLabelGridData = new GridData(SWT.BEGINNING, SWT.END, false, false);
		tableLabelGridData.horizontalSpan = 2;
		Label tableLabel = new Label(parent, SWT.NONE);
		tableLabel.setText("jBPM Installation Locations:");
		tableLabel.setLayoutData(tableLabelGridData);
	}
	
	public boolean performOk() {
		String name = "";
		PreferencesManager inputManager = 
			(PreferencesManager)tableViewer.getInput();
		inputManager.saveInstallations();
		TableItem item = getCheckedItem(tableViewer);
		if (item != null) {
			name = item.getText(0);
		}
		Activator.getDefault().getPluginPreferences().setValue(Constants.JBPM_NAME, name);		
		return true;
	}
	
	private TableItem getCheckedItem(TableViewer viewer) {
		TableItem[] items = viewer.getTable().getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getChecked()) return items[i];
		}
		return null;
	}
	
	public void performDefaults() {
		Activator.getDefault().getPluginPreferences().setToDefault(Constants.JBPM_NAME);
		PreferencesManager inputManager = 
			(PreferencesManager)tableViewer.getInput();
		inputManager.getJbpmInstallationMap().clear();
		tableViewer.setInput(inputManager);
		checkItemToCheck(tableViewer);
		setMessage("");
		updateButtons();
		setValid(true);
	}
	
}
