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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.jbpm.convert.b2j.messages.B2JMessages;
import org.jboss.tools.jbpm.convert.bpmnto.util.BPMNToUtil;
import org.jboss.tools.jbpm.convert.bpmnto.wizard.BpmnToWizard;
import org.jboss.tools.jbpm.convert.bpmnto.wizard.ProjectFilter;

/**
 * @author Grid Qian
 * 
 * the wizardpage used by user to choose the bpmn file
 */
public class BPMNResourcesChoicePage extends WizardPage {

	private TreeViewer viewer;
	private ISelection currentSelection;
	private IWizard wizard;

	public BPMNResourcesChoicePage(String pageName, String title,
			String description) {
		super(pageName);
		this.setTitle(title);
		this.setDescription(description);
	}

	public void createControl(Composite parent) {
		Composite composite = createDialogArea(parent);

		createListTitleArea(composite);
		createListViewer(composite);
		super.setControl(composite);

		initializePage();
	}

	private Label createListTitleArea(Composite composite) {
		Label label = new Label(composite, SWT.NONE);
		label.setText(B2JMessages.Bpmn_File_Choose_WizardPage_ViewerTitle);
		label.setFont(composite.getFont());
		return label;
	}

	private void createListViewer(Composite composite) {
		viewer = new TreeViewer(composite, SWT.BORDER | SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setLabelProvider(new WorkbenchLabelProvider());
		WorkbenchContentProvider cp = new WorkbenchContentProvider();
		viewer.setContentProvider(cp);
		viewer.setFilters(new ViewerFilter[] { new ProjectFilter() });
		viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				updateControls();
				currentSelection = viewer.getSelection();
				((BpmnToWizard) wizard)
						.setSelection((IStructuredSelection) currentSelection);

			}
		});
	}

	private void initializePage() {
		wizard = this.getWizard();
		viewer.setInput(ResourcesPlugin.getWorkspace());
		if (this.currentSelection != null) {
			viewer.setSelection(currentSelection, true);
		}
	}

	@Override
	public boolean isPageComplete() {
		if (viewer != null) {
			return BPMNToUtil.checkSelectedResources(viewer.getSelection());
		}
		return super.isPageComplete();
	}

	private void updateControls() {
		super.getWizard().getContainer().updateButtons();
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

	public void setSelection(ISelection selection) {
		this.currentSelection = selection;
	}

	public ISelection getSelection() {
		return currentSelection;
	}
}

class ProjectFilter extends ViewerFilter {
	@Override
	public boolean select(Viewer viewer, Object parent, Object element) {
		boolean res = false;
		if (element instanceof IFile) {
			IFile file = (IFile) element;
			if (file.getFileExtension().equalsIgnoreCase("bpmn")) {
				res = true;
			}
		}
		if (element instanceof IContainer) {
			res = true;
		}
		return res;
	}
}
