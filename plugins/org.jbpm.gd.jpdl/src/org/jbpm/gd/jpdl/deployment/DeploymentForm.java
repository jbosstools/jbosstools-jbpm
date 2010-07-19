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
package org.jbpm.gd.jpdl.deployment;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jbpm.gd.jpdl.Plugin;
import org.jbpm.gd.jpdl.editor.JpdlEditor;

public class DeploymentForm {
	
	private static ILabelProvider LABELPROVIDER = new WorkbenchLabelProvider();

	private FormToolkit toolkit;
	private Composite composite;
	private JpdlEditor editor;
	private DeploymentInfo deploymentInfo;
	
	private ScrolledForm form;

	private Button includeProcessInfoFileButton;
	private Text processInfoFileText;
	private Button includeGraphicalInfoFileButton;
	private Text graphicalInfoFileText;
	private Button browseGraphicalInfoFileButton;
	private Button includeImageFileButton;
	private Text imageFileText;
	private Button browseImageFileButton;
	private Button additionalFilesAddButton;
	private Button additionalFilesRemoveButton;
	private Table additionalFilesList;
	private Button classesAndResourcesAddButton;
	private Button classesAndResourcesRemoveButton;
	private Table classesAndResourcesList;
	private Text serverNameText;
	private Text serverPortText;
	private Text serverDeployerText;
	private Button useCredentialsButton;
	private Text userNameText;
	private Text passwordText;
	
	public DeploymentForm(FormToolkit toolkit, Composite composite, JpdlEditor editor) {
		this.toolkit = toolkit;
		this.composite = composite;
		this.editor = editor;
		deploymentInfo = editor.getDeploymentInfo();
	}	
		
	public void create() {
		createMainForm();
		createMainFilesSection();
		createAdditionalFilesSection();
		createClassesAndResourcesSection();
		createUserCredentialsSection();
		createServerInfoSection();
	}
	
	private void createMainForm() {
		form = toolkit.createScrolledForm(composite);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		form.setLayoutData(layoutData);		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;
		form.getBody().setLayout(layout);
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	private void createMainFilesSection() {
		Section mainFilesSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		mainFilesSection.marginWidth = 5;
		mainFilesSection.setText("Main Process Files");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = GridData.BEGINNING;
		gridData.horizontalSpan = 2;
		mainFilesSection.setLayoutData(gridData);
		
		Composite mainFilesClient =  toolkit.createComposite(mainFilesSection);
		mainFilesSection.setClient(mainFilesClient);
		mainFilesSection.setDescription("Check and select the files to be included in the deployment.");
		toolkit.paintBordersFor(mainFilesClient);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		layout.numColumns = 3;
		mainFilesClient.setLayout(layout);
		createProcessInfoFileField(mainFilesClient);
		createGraphicalInfoFileField(mainFilesClient);
		createImageFileField(mainFilesClient);
	}
	
	private void createProcessInfoFileField(Composite parent) {
		includeProcessInfoFileButton = toolkit.createButton(parent, "Process Info File:", SWT.CHECK);
		includeProcessInfoFileButton.setSelection(true);
		includeProcessInfoFileButton.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		processInfoFileText = toolkit.createText(parent, "");
		processInfoFileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		processInfoFileText.setEditable(false);
		toolkit.createLabel(parent, "");
	}
	
	private void createGraphicalInfoFileField(Composite parent) {
		includeGraphicalInfoFileButton = toolkit.createButton(parent, "Graphical Info File:", SWT.CHECK);
		includeGraphicalInfoFileButton.setSelection(true);
		includeGraphicalInfoFileButton.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		graphicalInfoFileText = toolkit.createText(parent, "");
		graphicalInfoFileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		graphicalInfoFileText.setEditable(false);
		browseGraphicalInfoFileButton = toolkit.createButton(parent, "Browse...", SWT.NONE);
	}

	private void createImageFileField(Composite parent) {
		includeImageFileButton = toolkit.createButton(parent, "Image File:", SWT.CHECK);
		includeImageFileButton.setSelection(true);
		includeImageFileButton.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		imageFileText = toolkit.createText(parent, "");
		imageFileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		imageFileText.setEditable(false);
		browseImageFileButton = toolkit.createButton(parent, "Browse...", SWT.NONE);
	}
	
	private void createAdditionalFilesSection() {
		Section additionalFilesSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		additionalFilesSection.marginWidth = 5;
		additionalFilesSection.setText("Additional Files");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		additionalFilesSection.setLayoutData(gridData);
		
		Composite additionalFilesClient =  toolkit.createComposite(additionalFilesSection);
		additionalFilesSection.setClient(additionalFilesClient);
		additionalFilesSection.setDescription("Add additional files such as forms that need to be included in the deployment.");
		toolkit.paintBordersFor(additionalFilesClient);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		additionalFilesClient.setLayout(layout);
		createAdditionalFilesList(additionalFilesClient);
		createAdditionalFilesButtons(additionalFilesClient);
	}
	
	private void createAdditionalFilesList(Composite parent) {
		additionalFilesList = toolkit.createTable(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 70;
		gridData.widthHint = 100;
		additionalFilesList.setLayoutData(gridData);
	}
	
	private void createAdditionalFilesButtons(Composite parent) {
		Composite composite = toolkit.createComposite(parent);
		composite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);		
		additionalFilesAddButton = toolkit.createButton(composite, "Add...", SWT.NONE);
		additionalFilesAddButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		additionalFilesRemoveButton = toolkit.createButton(composite, "Remove", SWT.NONE);
		additionalFilesRemoveButton.setEnabled(false);
	}
	
	private void createClassesAndResourcesSection() {
		Section classesAndResourcesSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		classesAndResourcesSection.marginWidth = 5;
		classesAndResourcesSection.setText("Classes and Resources");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		classesAndResourcesSection.setLayoutData(gridData);
		
		Composite classesAndResourcesClient =  toolkit.createComposite(classesAndResourcesSection);
		classesAndResourcesSection.setClient(classesAndResourcesClient);
		classesAndResourcesSection.setDescription("Add classes and/or resources that need to be included in the deployment.");
		toolkit.paintBordersFor(classesAndResourcesClient);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		classesAndResourcesClient.setLayout(layout);
		createClassesAndResourcesList(classesAndResourcesClient);
		createClassesAndResourcesButtons(classesAndResourcesClient);
	}

	private void createClassesAndResourcesList(Composite parent) {
		classesAndResourcesList = toolkit.createTable(parent, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gridData = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
		gridData.heightHint = 70;
		gridData.widthHint = 100;
		classesAndResourcesList.setLayoutData(gridData);
	}
	
	private void createClassesAndResourcesButtons(Composite parent) {
		Composite composite = toolkit.createComposite(parent);
		composite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);
		
		classesAndResourcesAddButton = toolkit.createButton(composite, "Add", SWT.NONE);
		classesAndResourcesAddButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		classesAndResourcesRemoveButton = toolkit.createButton(composite, "Remove", SWT.NONE);
		classesAndResourcesRemoveButton.setEnabled(false);
	}
	
	private void createUserCredentialsSection() {
		Section userCredentialsSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		userCredentialsSection.marginWidth = 5;
		userCredentialsSection.setText("User Credentials");

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		userCredentialsSection.setLayoutData(gridData);
		
		Composite userCredentialsClient =  toolkit.createComposite(userCredentialsSection);
		userCredentialsSection.setClient(userCredentialsClient);
		userCredentialsSection.setDescription("Specify the user credentials for the chosen server.");
		toolkit.paintBordersFor(userCredentialsClient);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		userCredentialsClient.setLayout(layout);
		
		useCredentialsButton = toolkit.createButton(userCredentialsClient, "Use credentials", SWT.CHECK);
		useCredentialsButton.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		GridData buttonData = new GridData();
		buttonData.horizontalSpan = 2;
		useCredentialsButton.setLayoutData(buttonData);
		
		Label userNameLabel = toolkit.createLabel(userCredentialsClient, "Username:");
		userNameLabel.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		userNameText = toolkit.createText(userCredentialsClient, "");
		userNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label passwordLabel = toolkit.createLabel(userCredentialsClient, "Password:");
		passwordLabel.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		passwordText = toolkit.createText(userCredentialsClient, "", SWT.PASSWORD);
		passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private void createServerInfoSection() {
		Section serverInfoSection = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		serverInfoSection.marginWidth = 5;
		serverInfoSection.setText("Server Settings");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		serverInfoSection.setLayoutData(gridData);
		
		Composite serverInfoClient =  toolkit.createComposite(serverInfoSection);
		serverInfoSection.setClient(serverInfoClient);
		serverInfoSection.setDescription("Specify the settings of the server you wish to deploy to.");
		toolkit.paintBordersFor(serverInfoClient);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		serverInfoClient.setLayout(layout);
		
		Label serverNameLabel = toolkit.createLabel(serverInfoClient, "Server Name:");
		serverNameLabel.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		serverNameText = toolkit.createText(serverInfoClient, "");
		serverNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label serverPortLabel = toolkit.createLabel(serverInfoClient, "Server Port:");
		serverPortLabel.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		serverPortText = toolkit.createText(serverInfoClient, "");
		serverPortText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label serverDeployerLabel = toolkit.createLabel(serverInfoClient, "Server Deployer:");
		serverDeployerLabel.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		serverDeployerText = toolkit.createText(serverInfoClient, "");
		serverDeployerText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	public void refresh() {
		unhookListeners();
		updateControls();	
		hookListeners();
	}	
	
	private void unhookListeners() {
		includeGraphicalInfoFileButton.removeSelectionListener(includeGraphicalInfoFileButtonSelectionListener);
		browseGraphicalInfoFileButton.removeSelectionListener(browseGraphicalInfoFileButtonSelectionListener);
		includeImageFileButton.removeSelectionListener(includeImageFileButtonSelectionListener);
		browseImageFileButton.removeSelectionListener(browseImageFileButtonSelectionListener);
		additionalFilesList.removeSelectionListener(additionalFilesListSelectionListener);
		additionalFilesAddButton.removeSelectionListener(additionaFilesAddButtonSelectionListener);
		additionalFilesRemoveButton.removeSelectionListener(additionaFilesRemoveButtonSelectionListener);
		classesAndResourcesList.removeSelectionListener(classesAndResourcesListSelectionListener);
		classesAndResourcesAddButton.removeSelectionListener(classesAndResourcesAddButtonSelectionListener);
		classesAndResourcesRemoveButton.removeSelectionListener(classesAndResourcesRemoveButtonSelectionListener);
		serverNameText.removeModifyListener(serverNameTextListener);
		serverPortText.removeModifyListener(serverPortTextListener);
		serverDeployerText.removeModifyListener(serverDeployerTextListener);		
	}
	
	private void updateControls() {
		updateProcessInfoFileControls();
		updateGraphicalInfoFileControls();
		updateImageFileControls();
		updateAdditionalFilesControls();
		updateClassesAndResourcesControls();
		updateUseCredentialsControls();
		updateServerInfoControls();
	}
	
	private void updateProcessInfoFileControls() {
		IFile processInfoFile = deploymentInfo.getProcessInfoFile();
		String value = "";
		if (processInfoFile != null) {
			value = processInfoFile.getFullPath().toString();
		}
		includeProcessInfoFileButton.setSelection(!"".equals(value));
		includeProcessInfoFileButton.setEnabled(false);
		processInfoFileText.setText(value);
	}
	
	private void updateGraphicalInfoFileControls() {
		IFile graphicalInfoFile = deploymentInfo.getGraphicalInfoFile();
		String value = "";
		if (graphicalInfoFile != null) {
			value = graphicalInfoFile.getFullPath().toString();
		}
		includeGraphicalInfoFileButton.setSelection(graphicalInfoFile != null);
		graphicalInfoFileText.setText(value);
		browseGraphicalInfoFileButton.setEnabled(graphicalInfoFile != null);
	}
	
	private void updateImageFileControls() {
		IFile imageFile = deploymentInfo.getImageFile();
		String value = "";
		if (imageFile != null) {
			value = imageFile.getFullPath().toString();
		}
		includeImageFileButton.setSelection(imageFile != null);
		imageFileText.setText(value);
		browseImageFileButton.setEnabled(imageFile != null);
	}
	
	private void updateAdditionalFilesControls() {
		Object[] additionalFiles = deploymentInfo.getAdditionalFiles();
		for (Object object : additionalFiles) {
			if (object instanceof IFile) {
				IFile file = (IFile)object;
				TableItem tableItem = new TableItem(additionalFilesList, SWT.NULL);
				tableItem.setText(LABELPROVIDER.getText(file) + " (" + file.getFullPath() + ")");
				tableItem.setImage(LABELPROVIDER.getImage(file));
				tableItem.setData(object);				
			}
		}
	}
	
	private void updateClassesAndResourcesControls() {
		Object[] classesAndResources = deploymentInfo.getClassesAndResources();
		for (Object object : classesAndResources) {
			IPath path = null;
			if (object instanceof IFile) {
				path = ((IFile)object).getFullPath();
			} else if (object instanceof ICompilationUnit) {
				path = ((ICompilationUnit)object).getPath();
			} else if (object instanceof IClassFile) {
				path = ((IClassFile)object).getPath();
			}
			if (path != null) {
				TableItem tableItem = new TableItem(classesAndResourcesList, SWT.NULL);
				tableItem.setData(object);
				tableItem.setText(LABELPROVIDER.getText(object) + " (" + path + ")");
				tableItem.setImage(LABELPROVIDER.getImage(object));
			}
		}
	}
	
	private void updateUseCredentialsControls() {
		boolean useCredentials = deploymentInfo.getUseCredentials();
		useCredentialsButton.setSelection(useCredentials);
		String userName = deploymentInfo.getUserName();
		if (userName != null) {
			userNameText.setText(userName);
		}
		userNameText.setEnabled(useCredentials);
		String password = deploymentInfo.getPassword();
		if (password != null) {
			passwordText.setText(password);
		}
		passwordText.setEnabled(useCredentials);
	}
	
	private void updateServerInfoControls() {
		String serverName = deploymentInfo.getServerName();
		if (serverName != null) {
			 serverNameText.setText(serverName);
		}
		String serverPort = deploymentInfo.getServerPort();
		if (serverPort != null) {
			serverPortText.setText(serverPort);
		}
		String serverDeployer = deploymentInfo.getServerDeployer();
		if (serverDeployer != null) {
			serverDeployerText.setText(serverDeployer);
		}		
	}
	
	private void hookListeners() {
		includeGraphicalInfoFileButton.addSelectionListener(includeGraphicalInfoFileButtonSelectionListener);
		browseGraphicalInfoFileButton.addSelectionListener(browseGraphicalInfoFileButtonSelectionListener);
		includeImageFileButton.addSelectionListener(includeImageFileButtonSelectionListener);
		browseImageFileButton.addSelectionListener(browseImageFileButtonSelectionListener);
		additionalFilesList.addSelectionListener(additionalFilesListSelectionListener);
		additionalFilesAddButton.addSelectionListener(additionaFilesAddButtonSelectionListener);
		additionalFilesRemoveButton.addSelectionListener(additionaFilesRemoveButtonSelectionListener);
		classesAndResourcesList.addSelectionListener(classesAndResourcesListSelectionListener);
		classesAndResourcesAddButton.addSelectionListener(classesAndResourcesAddButtonSelectionListener);
		classesAndResourcesRemoveButton.addSelectionListener(classesAndResourcesRemoveButtonSelectionListener);
		serverNameText.addModifyListener(serverNameTextListener);
		serverPortText.addModifyListener(serverPortTextListener);
		serverDeployerText.addModifyListener(serverDeployerTextListener);
		useCredentialsButton.addSelectionListener(useCredentialsButtonSelectionListener);
	}
	
	private SelectionListener useCredentialsButtonSelectionListener = new SelectionAdapter() {		
		public void widgetSelected(SelectionEvent event) {
			userNameText.setEnabled(useCredentialsButton.getSelection());
			passwordText.setEnabled(useCredentialsButton.getSelection());
		}
	};
		
	private ModifyListener serverNameTextListener = new ModifyListener() {		
		public void modifyText(ModifyEvent event) {
			deploymentInfo.setServerName(serverNameText.getText());
			editor.setDirty(true);
		}
	};
	
	private ModifyListener serverPortTextListener = new ModifyListener() {		
		public void modifyText(ModifyEvent event) {
			deploymentInfo.setServerPort(serverPortText.getText());
			editor.setDirty(true);
		}
	};
	
	private ModifyListener serverDeployerTextListener = new ModifyListener() {		
		public void modifyText(ModifyEvent event) {
			deploymentInfo.setServerDeployer(serverDeployerText.getText());
			editor.setDirty(true);
		}
	};
	
	private SelectionListener includeGraphicalInfoFileButtonSelectionListener = new SelectionAdapter() {		
		public void widgetSelected(SelectionEvent event) {
			boolean include = includeGraphicalInfoFileButton.getSelection();
			browseGraphicalInfoFileButton.setEnabled(include);
			if (include && graphicalInfoFileText.getData() != null) {
				deploymentInfo.setGraphicalInfoFile((IFile)graphicalInfoFileText.getData());
			} else {
				deploymentInfo.setGraphicalInfoFile(null);
			}
			editor.setDirty(true);
		}
	};
	
	private SelectionListener includeImageFileButtonSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			boolean include = includeImageFileButton.getSelection();
			browseImageFileButton.setEnabled(include);
			if (include && imageFileText.getData() != null) {
				deploymentInfo.setImageFile((IFile)imageFileText.getData());
			} else {
				deploymentInfo.setImageFile(null);
			}
			editor.setDirty(true);
		}
	};
	
	private SelectionListener browseGraphicalInfoFileButtonSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(null, new WorkbenchLabelProvider(), new WorkbenchContentProvider());
			dialog.setTitle("Graphical Info File Selection");
			dialog.setMessage("Select the graphical info file.");
			dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
			dialog.setValidator(fileSelectionStatusValidator);
			dialog.open();
			if (dialog.getFirstResult() != null && dialog.getFirstResult() instanceof IFile) {
				IFile file = (IFile)dialog.getFirstResult();
				graphicalInfoFileText.setText(file.getFullPath().toString());
				graphicalInfoFileText.setData(file);
				deploymentInfo.setGraphicalInfoFile(file);
				editor.setDirty(true);
			}
		}
	};
	
	private SelectionListener browseImageFileButtonSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(null, new WorkbenchLabelProvider(), new WorkbenchContentProvider());
			dialog.setTitle("Image File Selection");
			dialog.setMessage("Select the image file.");
			dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
			dialog.setValidator(fileSelectionStatusValidator);
			dialog.open();
			if (dialog.getFirstResult() != null && dialog.getFirstResult() instanceof IFile) {
				IFile file = (IFile)dialog.getFirstResult();
				imageFileText.setText(file.getFullPath().toString());
				imageFileText.setData(file);
				deploymentInfo.setImageFile(file);
				editor.setDirty(true);
			}
		}
	};
	
	private SelectionListener additionalFilesListSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			additionalFilesRemoveButton.setEnabled(additionalFilesList.getSelectionCount() > 0);
		}
	};
	
	private SelectionListener additionaFilesAddButtonSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(null, new WorkbenchLabelProvider(), new WorkbenchContentProvider());
			dialog.setTitle("Additional File Selection");
			dialog.setMessage("Select the additional file.");
			dialog.setAllowMultiple(false);
			dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
			dialog.setValidator(fileSelectionStatusValidator);
			dialog.open();
			if (dialog.getFirstResult() != null && dialog.getFirstResult() instanceof IFile) {
				IFile file = (IFile)dialog.getFirstResult();
				TableItem tableItem = new TableItem(additionalFilesList, SWT.NULL);
				tableItem.setText(LABELPROVIDER.getText(file) + " (" + file.getFullPath() + ")");
				tableItem.setImage(LABELPROVIDER.getImage(file));
				deploymentInfo.addToAdditionalFiles(file);
				editor.setDirty(true);
			}
		}
	};
	
	private ISelectionStatusValidator fileSelectionStatusValidator = 
		new ISelectionStatusValidator() {
			public IStatus validate(Object[] arg0) {
				if (arg0.length == 1 && arg0[0] instanceof IFile) {
					return new Status(IStatus.OK, Plugin.getDefault().getBundle().getSymbolicName(), "Press OK to confirm.");
				} else {
					return new Status(IStatus.ERROR, Plugin.getDefault().getBundle().getSymbolicName(), "Select a single file.");
				}
			}
	};
	
	private SelectionListener additionaFilesRemoveButtonSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			int[] indices = additionalFilesList.getSelectionIndices();
			if (indices.length > 0) {
				TableItem tableItem = additionalFilesList.getItem(indices[0]);
				Object object = tableItem.getData();
				deploymentInfo.removeFromAdditionalFiles(object);
				additionalFilesList.remove(indices[0]);
				editor.setDirty(true);
			}
		}
	};
	
	private SelectionListener classesAndResourcesListSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			classesAndResourcesRemoveButton.setEnabled(classesAndResourcesList.getSelectionCount() > 0);
		}
	};
	
	private SelectionListener classesAndResourcesAddButtonSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(null, new JavaElementLabelProvider(), new ClassesAndResourcesContentProvider());
			dialog.setTitle("Classes and Resources Selection");
			dialog.setAllowMultiple(false);
			dialog.setMessage("Select a class or resouce.");
			dialog.setInput(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));
			dialog.setValidator(classesAndResourcesSelectionStatusValidator);
			dialog.open();
			if (dialog.getFirstResult() != null) {  //&& dialog.getFirstResult() instanceof IFile) {
				IPath path = null;
				if (dialog.getFirstResult() instanceof IFile) {
					path = ((IFile)dialog.getFirstResult()).getFullPath();
				} else if (dialog.getFirstResult() instanceof ICompilationUnit) {
					path = ((ICompilationUnit)dialog.getFirstResult()).getPath();
				} else if (dialog.getFirstResult() instanceof IClassFile) {
					path = ((IClassFile)dialog.getFirstResult()).getPath();
				}
				if (path != null) {
					TableItem tableItem = new TableItem(classesAndResourcesList, SWT.NULL);
					tableItem.setData(dialog.getFirstResult());
					tableItem.setText(LABELPROVIDER.getText(dialog.getFirstResult()) + " (" + path + ")");
					tableItem.setImage(LABELPROVIDER.getImage(dialog.getFirstResult()));
					deploymentInfo.addToClassesAndResources(dialog.getFirstResult());
					editor.setDirty(true);
				}
			}
		}
	};
	
	private ISelectionStatusValidator classesAndResourcesSelectionStatusValidator = 
		new ISelectionStatusValidator() {
			public IStatus validate(Object[] arg0) {
				if (arg0.length == 1 && (arg0[0] instanceof IFile || arg0[0] instanceof ICompilationUnit || arg0[0] instanceof IClassFile)) {
					return new Status(IStatus.OK, Plugin.getDefault().getBundle().getSymbolicName(), "Press OK to confirm.");
				} else {
					return new Status(IStatus.ERROR, Plugin.getDefault().getBundle().getSymbolicName(), "Select a single file.");
				}
			}
	};
	
	private SelectionListener classesAndResourcesRemoveButtonSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			int[] indices = classesAndResourcesList.getSelectionIndices();
			if (indices.length > 0) {
				TableItem tableItem = classesAndResourcesList.getItem(indices[0]);
				Object object = tableItem.getData();
				deploymentInfo.removeFromClassesAndResources(object);
				classesAndResourcesList.remove(indices[0]);
			}
		}
	};
	
}
