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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.jbpm.convert.bpmnto.util.BPMNToUtil;

/**
 * @author Grid Qian
 * 
 *         the wizardpage used by user to choose the bpmn file
 */
public class BPMNResourcesChoicePage extends AbstractConvertWizardPage {

	private TreeViewer viewer;
	private ISelection currentSelection;
	private IWizard wizard;

	public BPMNResourcesChoicePage(String pageName, String title,
			String tableTitle, String description) {
		super(pageName, title, tableTitle, description);
	}

	public void createTableViewer(Composite composite) {
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

	public void initializePage() {
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

	public void setSelection(ISelection selection) {
		this.currentSelection = selection;
	}

	public ISelection getSelection() {
		return currentSelection;
	}
}

class ProjectFilter extends ViewerFilter {
	private static String BPMN_FILE_EXT = "bpmn";

	@Override
	public boolean select(Viewer viewer, Object parent, Object element) {
		boolean res = false;
		if (element instanceof IFile) {
			IFile file = (IFile) element;
			if (file.getFileExtension().equalsIgnoreCase(BPMN_FILE_EXT)) {
				res = file.getProject().isAccessible();
			}
		}
		if (element instanceof IContainer) {
			IContainer container = (IContainer) element;
			if (container.getProject().isAccessible()) {
				res = hasBPMNResources(container);
			}
		}
		return res;
	}

	private boolean hasBPMNResources(IContainer container) {
		boolean res = false;
		try {
			IResource[] resources = container.members();
			for (int i = 0; i < resources.length; i++) {
				if (resources[i] instanceof IFile
						&& ((IFile) resources[i]).getFileExtension()
								.equalsIgnoreCase(BPMN_FILE_EXT)) {
					res = true;
					break;
				}
				if (resources[i] instanceof IContainer) {
					if (hasBPMNResources((IContainer) resources[i])) {
						res = true;
						break;
					}
				}
			}
		} catch (CoreException ce) {
			// Ignore
		}
		return res;
	}
}
