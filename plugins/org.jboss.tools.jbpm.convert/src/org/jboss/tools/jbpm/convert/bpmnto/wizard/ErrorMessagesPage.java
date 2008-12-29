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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Grid Qian
 * 
 *         the wizardpage for showing the error and warning messages from
 *         translating
 */
public class ErrorMessagesPage extends AbstractConvertWizardPage {

	private TableViewer listViewer;

	protected ErrorMessagesPage(String pageName, String title,
			String tableTitle, String description) {
		super(pageName, title, tableTitle, description);
	}

	public void createTableViewer(Composite composite) {
		listViewer = new TableViewer(composite, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 250;
		data.widthHint = 300;
		listViewer.getTable().setLayoutData(data);

		listViewer.setLabelProvider(new LabelProvider());
		listViewer.setContentProvider(new ArrayContentProvider());
	}

	public void initializePage() {
		this.setPageComplete(false);
	}

	public TableViewer getListViewer() {
		return listViewer;
	}

	public void setListViewer(TableViewer listViewer) {
		this.listViewer = listViewer;
	}
	
	public boolean isPageComplete() {
		return true;
	}

}
