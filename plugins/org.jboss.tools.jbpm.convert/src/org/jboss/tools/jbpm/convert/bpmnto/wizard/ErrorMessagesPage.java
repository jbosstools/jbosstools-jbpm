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

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.tools.jbpm.convert.b2j.messages.B2JMessages;

/**
 * @author Grid Qian
 * 
 * the wizardpage for showing the error and warning messages from translating
 */
public class ErrorMessagesPage extends WizardPage {

	private TableViewer listViewer;

	protected ErrorMessagesPage(String pageName, String title, String description) {
		super(pageName);
		this.setDescription(description);
		this.setTitle(title);
	}

	public void createControl(Composite parent) {
		Composite composite = createDialogArea(parent);

		createListTitleArea(composite);
		createListViewer(composite);
		setControl(composite);

	}
	
	private Label createListTitleArea(Composite composite) {
		Label label = new Label(composite, SWT.NONE);
		label
				.setText(B2JMessages.Bpmn_Translate_Message_WizardpageViewer_Title);
		label.setFont(composite.getFont());
		return label;
	}

	private void createListViewer(Composite composite) {
		listViewer = new TableViewer(composite, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 250;
		data.widthHint = 300;
		listViewer.getTable().setLayoutData(data);

		listViewer.setLabelProvider(new LabelProvider());
		listViewer.setContentProvider(new ArrayContentProvider());
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

	public boolean isPageComplete() {
		return true;
	}
	

	public TableViewer getListViewer() {
		return listViewer;
	}

	public void setListViewer(TableViewer listViewer) {
		this.listViewer = listViewer;
	}

}
