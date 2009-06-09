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
package org.jbpm.gd.pf.wizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class NewPageFlowWizardPage extends WizardPage {
	
	private Text containerText;
	private Text fileNameText;
	private Button browseButton;
	
	private IWorkspaceRoot workspaceRoot;
	private String containerName;

	public NewPageFlowWizardPage() {
		super("Page Flow");
		setTitle("Create Page Flow");
		setDescription("Create a new page flow");	
		workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	}

	public void init(IStructuredSelection selection) {
		if (!selection.isEmpty()) {
			initContainerName(getSelectedElement(selection));
		}
	}
	
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = createClientArea(parent);		
//		createLabel(composite);	
		createContainerField(composite);
		createFileNameField(composite);
		setControl(composite);
		Dialog.applyDialogFont(composite);		
		setPageComplete(false);
	}

//	private void createLabel(Composite composite) {
//		Label label= new Label(composite, SWT.WRAP);
//		label.setText("Choose a source folder and enter a file name.");
//		GridData gd= new GridData();
//		gd.widthHint= convertWidthInCharsToPixels(80);
//		gd.horizontalSpan= 3;
//		label.setLayoutData(gd);
//	}

	private Composite createClientArea(Composite parent) {
		Composite composite= new Composite(parent, SWT.NONE);
		GridLayout layout= new GridLayout();
		layout.marginWidth= 0;
		layout.marginHeight= 0;
		layout.numColumns= 3;
		composite.setLayout(layout);
		return composite;
	}
	
	private void createContainerField(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Source folder : ");
		containerText = new Text(parent, SWT.BORDER);
		containerText.setText(containerName == null ? "" : containerName);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				verifyContentsValid();
			}
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		browseButton = new Button(parent, SWT.PUSH);
		browseButton.setText("Browse...");
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				chooseContainer();
			}			
		});
		gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(15);
		browseButton.setLayoutData(gd);
	}
	
	private void createFileNameField(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("File name : ");
		fileNameText = new Text(parent, SWT.BORDER);
		fileNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				verifyContentsValid();
			}
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		fileNameText.setLayoutData(gd);		
	}
	
	private void chooseContainer() {
		WorkbenchContentProvider provider= new WorkbenchContentProvider();
		ILabelProvider labelProvider= new WorkbenchLabelProvider(); 
		ElementTreeSelectionDialog dialog= new ElementTreeSelectionDialog(getShell(), labelProvider, provider);
		dialog.setTitle("Folder Selection");
		dialog.setMessage("Choose a folder");
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		dialog.open();
		initContainerName((IResource)dialog.getFirstResult());
		if (containerName != null) {
			containerText.setText(containerName);
		}
	}

	private IResource getSelectedElement(IStructuredSelection selection) {
		IResource resource = null;
		if (selection != null && !selection.isEmpty()) {
			Object selectedElement= selection.getFirstElement();
			if (selectedElement instanceof IAdaptable) {
				resource= (IResource)((IAdaptable) selectedElement).getAdapter(IResource.class);
			}
		}
		return resource;
	}
	
	private void initContainerName(IResource resource) {
		while (resource != null && !(resource instanceof IContainer)) {
			resource = resource.getParent();
		}
		containerName = resource == null ? null : ((IContainer)resource).getFullPath().toString();
	}
		
	private void verifyContentsValid() {
		if (!checkContainerPathValid()) {
			setErrorMessage("The folder does not exist.");
			setPageComplete(false);
		} else if (isFileNameEmpty()) {
			setErrorMessage("Enter a file name.");
			setPageComplete(false);
		} else if (fileExists()){
			setErrorMessage("A file with this name already exists.");
			setPageComplete(false);
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}
	}
	
	private boolean fileExists() {
		return getPageFlowFile().exists();
	}
	
	private boolean isFileNameEmpty() {
		String str = fileNameText.getText();
		return str == null || "".equals(str);
	}
	
	private boolean checkContainerPathValid() {
		Path path = new Path(containerText.getText());
		if (path.segmentCount() == 0) return false;
		if (path.segmentCount() == 1) return workspaceRoot.getProject(path.segment(0)).exists();
		return workspaceRoot.getFolder(path).exists();
	}
	
	public String getFileName() {
		String fileName = fileNameText.getText();
		if (fileName.length() <= 4 || (fileName.length() > 4 && !".xml".equals(fileName.substring(fileName.length() - 4)))) {
			fileName = fileName + ".xml";
		}
		return fileName;
	}
	
	public IContainer getProcessFolder() {
		IPath path = new Path(containerText.getText());
		if (path.segmentCount() == 0) return null;
		if (path.segmentCount() == 1) return workspaceRoot.getProject(path.segment(0));
		return workspaceRoot.getFolder(path);
	}
	
	public IFile getPageFlowFile() {
		return getProcessFolder().getFile(new Path(getFileName()));
	}
	
}
