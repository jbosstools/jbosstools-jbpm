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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.jbpm.util.AutoResizeTableLayout;
import org.jbpm.gd.jpdl.Constants;
import org.jbpm.gd.jpdl.Plugin;
import org.jbpm.gd.jpdl.util.AssignmentTypeHelper;

public class AssignmentTypesPage extends PreferencePage implements IWorkbenchPreferencePage, Constants {
	
	Table assignmentTypesTable;
	
	public AssignmentTypesPage() {
		super();
		setPreferenceStore(Plugin.getDefault().getPreferenceStore());
	}

	protected Control createContents(Composite parent) {
		Composite clientArea = createClientArea(parent);
		createAssignmentTypesTable(clientArea);
		return null;
	}
	
	private void createAssignmentTypesTable(Composite parent) {
		assignmentTypesTable = new Table(parent, SWT.CHECK | SWT.V_SCROLL | SWT.BORDER);
		assignmentTypesTable.setLinesVisible(true);
		assignmentTypesTable.setHeaderVisible(true);
		AutoResizeTableLayout layout = new AutoResizeTableLayout(assignmentTypesTable);
		TableColumn labelColumn = new TableColumn(assignmentTypesTable, SWT.LEFT);
		labelColumn.setText("Label");
		ColumnWeightData labelColumnData = new ColumnWeightData(30);
		layout.addColumnData(labelColumnData);
		TableColumn idColumn = new TableColumn(assignmentTypesTable, SWT.LEFT);
		idColumn.setText("Id");
		ColumnWeightData idColumnData = new ColumnWeightData(30);
		layout.addColumnData(idColumnData);
		TableColumn targetClassColumn = new TableColumn(assignmentTypesTable, SWT.LEFT);
		targetClassColumn.setText("Target Class");
		ColumnWeightData targetClassColumnData = new ColumnWeightData(30);
		layout.addColumnData(targetClassColumnData);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		assignmentTypesTable.setLayoutData(gridData);
		Map assignmentTypeMap = AssignmentTypeHelper.getLabelMap();
		Iterator mapIterator = assignmentTypeMap.keySet().iterator(); 
		while (mapIterator.hasNext()) {
			String label = (String)mapIterator.next(); 
			Iterator setIterator = ((Set)assignmentTypeMap.get(label)).iterator();
			while (setIterator.hasNext()) {
				IConfigurationElement element = (IConfigurationElement)setIterator.next();
				TableItem item = new TableItem(assignmentTypesTable, SWT.NORMAL);
				item.setData(element);
				item.setText(0, label);
				item.setText(1, element.getAttribute("id"));
				item.setText(2, element.getAttribute("input"));
				boolean disabled = getPreferenceStore().getBoolean(getPreferencesKey(element));
				item.setChecked(!disabled);
			}
		}
	}
	
	private String getPreferencesKey(IConfigurationElement element) {
		return "assignmentType(" + element.getAttribute("id") + ").disabled";		
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
		TableItem[] items = assignmentTypesTable.getItems();
		for (int i = 0; i < items.length; i++) {
			getPreferenceStore().setValue(
					getPreferencesKey((IConfigurationElement)items[i].getData()), 
					!items[i].getChecked());
		}
		return true;
	}
	
	public void performDefaults() {
		TableItem[] items = assignmentTypesTable.getItems();
		for (int i = 0; i < items.length; i++) {
			items[i].setChecked(true);
			getPreferenceStore().setValue(
					getPreferencesKey((IConfigurationElement)items[i].getData()), 
					!items[i].getChecked());
		}
	}

}
