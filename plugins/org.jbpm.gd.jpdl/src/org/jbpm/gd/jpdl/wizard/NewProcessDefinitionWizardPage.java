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
package org.jbpm.gd.jpdl.wizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
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

public class NewProcessDefinitionWizardPage extends WizardPage {
	
	private Text containerText;
	private Text processNameText;
	private Button browseButton;

	private IWorkspaceRoot workspaceRoot;
	private String containerName;	

	public NewProcessDefinitionWizardPage() {
		super("Process Definition");
		setTitle("Create Process Definition");
		setDescription("Create a new process definition");	
		workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	}

	public void init(IStructuredSelection selection) {
		IContainer container = null;
		if (selection != null && !selection.isEmpty()) {
			Object object = selection.getFirstElement();
			if (IFile.class.isInstance(object) && !IContainer.class.isInstance(object)) {
				container = ((IFile)object).getParent();
			} else if (IContainer.class.isInstance(object)) {
				container = (IContainer)object;
			}
		} else {
			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			IProject[] projects = workspaceRoot.getProjects();
			if (projects != null && projects.length != 0) {
				container = projects[0];
			}			
		}
		initContainerName(container);
	}
	
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite = createClientArea(parent);		
		createProcessNameField(composite);
		createContainerField(composite);
		setControl(composite);
		Dialog.applyDialogFont(composite);		
		checkPage();
	}

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
		label.setText("Source Folder: ");
		containerText = new Text(parent, SWT.BORDER);
		containerText.setText(containerName);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkPage();
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
		gd.heightHint = 18;
		browseButton.setLayoutData(gd);
	}
	
	private void createProcessNameField(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Process Name: ");
		processNameText = new Text(parent, SWT.BORDER);
		processNameText.setText(findInitialProcessName());
		processNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkPage();
			}
		});
		processNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));	
		new Label(parent, SWT.NONE);
	}
	
	private String findInitialProcessName() {
		String baseName = "process";
		String result = baseName;
		if (processExists(result)) {
			int runner = 1;
			do {
				result = baseName + "-" + runner;
			} while (processExists(result));
		}
		return result;
	}
	
	private boolean processExists(String name) {
		IResource resource = workspaceRoot.findMember(new Path(containerName).append(name + ".jpdl.xml"));
		return resource != null && resource.exists();
	}
	
	private void chooseContainer() {
		WorkbenchContentProvider provider= new WorkbenchContentProvider();
		ILabelProvider labelProvider= new WorkbenchLabelProvider(); 
		ElementTreeSelectionDialog dialog= new ElementTreeSelectionDialog(getShell(), labelProvider, provider);
		dialog.setTitle("Folder Selection");
		dialog.setMessage("Choose a folder");
		dialog.setInput(ResourcesPlugin.getWorkspace());
		dialog.addFilter(createViewerFilter());
		dialog.open();
		initContainerName((IContainer)dialog.getFirstResult());
		containerText.setText(containerName);
	}

	private ViewerFilter createViewerFilter() {
		ViewerFilter filter= new ViewerFilter() {
			public boolean select(Viewer viewer, Object parent, Object element) {
				return element instanceof IContainer;
			}
		};
		return filter;
	}
	
	
	private void initContainerName(IContainer elem) {
		containerName = (elem == null) ? "" : elem.getFullPath().makeRelative().toString(); 
	}
	
	private void checkPage() {
		if (!checkProcessNameText()) return;
		if (!checkContainerText()) return;
	}
	
	private boolean checkContainerText() {
		if ("".equals(containerText.getText())) {
			setMessage("Select the source folder or enter its name.");
			setPageComplete(false);
			return false;
		} else if (!containerExists()) {
			setMessage("The source folder does not exist and will be created.");
			setPageComplete(true);
			return true;
		} else {
			setMessage(null);
			setPageComplete(true);
			return true;
		}
	}
	
	private boolean containerExists() {
		IPath path = new Path(containerText.getText());
		IResource resource = workspaceRoot.findMember(path);
		return resource != null && resource.exists();
	}
	
	private boolean checkProcessNameText() {
		if ("".equals(processNameText.getText())) {
			setMessage("Enter a name for the process.");
			setPageComplete(false);
			return false;
		} else if (processExists()) {
			setMessage("A process with this name already exists in this container.");
			setPageComplete(false);
			return false;
		} else {
			setMessage(null);
			setPageComplete(true);
			return true;
		}
	}
	
	private boolean processExists() {
		IFolder folder = getProcessFolder();
		IFile processDefinitionFile = folder.getFile(getProcessName() + ".jpdl.xml");
		IFile gpdFile = folder.getFile("." + getProcessName() + ".gpd.xml");
		return processDefinitionFile.exists() || gpdFile.exists();
	}
	
	public IFolder getProcessFolder() {
		return workspaceRoot.getFolder(new Path(containerText.getText()));
	}
	
	public String getProcessName() {
		return processNameText.getText();
	}
	
}
