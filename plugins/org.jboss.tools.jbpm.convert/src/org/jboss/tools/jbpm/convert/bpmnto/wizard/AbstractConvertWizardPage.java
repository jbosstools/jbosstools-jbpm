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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Grid Qian
 * 
 *         this class is a abstract wizard page.
 */
abstract public class AbstractConvertWizardPage extends WizardPage {

	public String tableTitle;

	public AbstractConvertWizardPage(String pageName, String pageTitle,
			String tableTitle, String pageDescription) {
		super(pageName);
		this.setTitle(pageTitle);
		this.setDescription(pageDescription);
		this.tableTitle = tableTitle;
	}

	public void createControl(Composite parent) {
		Composite composite = createDialogArea(parent);
		createTableTitleArea(composite);
		createTableViewer(composite);
		addOtherAreas(composite);
		setControl(composite);

		initializePage();
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

	private Label createTableTitleArea(Composite composite) {
		Label label = new Label(composite, SWT.NONE);
		label.setText(tableTitle);
		label.setFont(composite.getFont());
		return label;
	}

	protected void addOtherAreas(Composite composite) {
		// TODO if need, sub-class may overwrite this method to add other ui
		// element
	}

	protected void initializePage() {
		// TODO if need, sub-class may overwrite this method to do some
		// initialize works
	}
	
	protected void updateControls() {
		super.getWizard().getContainer().updateButtons();
	}

	// create a table view
	abstract public void createTableViewer(Composite composite);

}
