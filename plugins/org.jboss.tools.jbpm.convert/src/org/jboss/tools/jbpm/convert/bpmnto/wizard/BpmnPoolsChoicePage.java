/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.jbpm.convert.bpmnto.wizard;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.jbpm.convert.b2j.messages.B2JMessages;
import org.jboss.tools.jbpm.convert.bpmnto.wizard.BpmnToWizard;

/**
 * @author Grid Qian
 * 
 * the wizardpage used by user to choose the bpmn pool from a bpmn diagram
 */
public class BpmnPoolsChoicePage extends WizardPage {

	CheckboxTableViewer listViewer;
	String listTitle;
	private Map<String, String> idMap;

	private IWizard wizard;

	public BpmnPoolsChoicePage(String pageName, String title, String listTitle) {
		super(pageName);
		this.listTitle = listTitle;
		this.setDescription(title);
		this.setTitle(listTitle);
	}

	public void createControl(Composite parent) {
		Composite composite = createDialogArea(parent);

		createListTitleArea(composite);
		createListViewer(composite);
		addSelectionButtons(composite);
		setControl(composite);

		initializePage();
		initializeViewer();
	}

	private Label createListTitleArea(Composite composite) {
		Label label = new Label(composite, SWT.NONE);
		label.setText(listTitle);
		label.setFont(composite.getFont());
		return label;
	}

	private void createListViewer(Composite composite) {
		listViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 250;
		data.widthHint = 300;
		listViewer.getTable().setLayoutData(data);

		listViewer.setLabelProvider(new LabelProvider());
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.addCheckStateListener(new ICheckStateListener() {
			@SuppressWarnings("unchecked")
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (event.getChecked()) {
					(((BpmnToWizard) wizard)).getPoolIdList().add(
							((Entry<String, String>) event.getElement())
									.getKey());
				} else {
					(((BpmnToWizard) wizard)).getPoolIdList().remove(
							((Entry<String, String>) event.getElement())
									.getKey());
				}
				changeComplete();
			}
		});
	}

	private void addSelectionButtons(Composite composite) {
		Composite buttonComposite = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 4;
		buttonComposite.setLayout(layout);
		buttonComposite.setLayoutData(new GridData(SWT.END, SWT.TOP, true,
				false));

		Button selectButton = createButton(buttonComposite,
				B2JMessages.Label_Select_All, false);

		SelectionListener listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				listViewer.setAllChecked(true);
				(((BpmnToWizard) wizard)).getPoolIdList()
						.addAll(idMap.keySet());
				changeComplete();
			}
		};
		selectButton.addSelectionListener(listener);

		Button deselectButton = createButton(buttonComposite,
				B2JMessages.Label_Deselect_All, false);

		listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				listViewer.setAllChecked(false);
				(((BpmnToWizard) wizard)).getPoolIdList().clear();
				changeComplete();
			}
		};
		deselectButton.addSelectionListener(listener);
	}

	private Button createButton(Composite parent, String label,
			boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private void initializePage() {
		setPageComplete(false);
	}

	private void initializeViewer() {
		if (listViewer == null) {
			return;
		}
		listViewer.setInput(idMap.entrySet());
		wizard = this.getWizard();
		List<String> poolIdList = ((BpmnToWizard) wizard).getPoolIdList();
		if (poolIdList.size() == 0) {
			listViewer.setAllChecked(false);
		} else {
			for (String id : poolIdList) {
				Set<Entry<String, String>> set = idMap.entrySet();
				Entry<String, String> selectedEntry = null;
				for (Entry<String, String> entry : set) {
					if (entry.getKey().equals(id)) {
						selectedEntry = entry;
						break;
					}
				}
				listViewer.setChecked(selectedEntry, true);
			}
		}
	}

	private Composite createDialogArea(Composite parent) {
		// create a composite with standard margins and spacing
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 7;
		layout.marginWidth = 7;
		layout.verticalSpacing = 4;
		layout.horizontalSpacing = 4;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		return composite;
	}

	public void changeComplete() {
		if (listViewer.getCheckedElements().length != 0) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}

	public Map<String, String> getIdMap() {
		return idMap;
	}

	public void setIdMap(Map<String, String> idMap) {
		this.idMap = idMap;
		initializeViewer();
	}

}
