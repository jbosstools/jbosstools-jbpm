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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jbpm.gd.jpdl.Logger;
import org.jbpm.gd.jpdl.Plugin;
import org.jbpm.gd.jpdl.editor.JpdlEditor;
import org.jbpm.gd.jpdl.util.JavaClassNameCollector;
import org.jbpm.gd.jpdl.util.ProcessDeployer;

public class DeploymentForm {

	public static final int NONE = 0;
	public static final int EXPRESSION = 1;
	public static final int HANDLER = 2;
	
	private FormToolkit toolkit;
	private Composite composite;
	private IFolder processFolder;
	private JpdlEditor editor;
	
	private Form form;
	private Text nameText;
	private Text portText;
	private Text deployerText;
	private Text locationText;
	private Text usernameText;
	private Text passwordText;
	private Button deployButton;
	private Button saveButton;
	private Button locationButton;
	private Button testConnectionButton;
	private Button saveLocallyButton;
	private Button useCredentialsButton;
	
	private IncludeInDeploymentTreeViewer includeFilesTreeViewer;
	private IncludeInDeploymentTreeViewer includeClassesTreeViewer;
	
	public DeploymentForm(FormToolkit toolkit, Composite composite, IFolder processFolder, JpdlEditor editor) {
		this.toolkit = toolkit;
		this.composite = composite;
		this.processFolder = processFolder;
		this.editor = editor;
	}	
		
	public void create() {
		createMainForm();
		createIncludeFilesSection();
		createIncludeClassesSection();
		createLocalSaveSection();
		createServerInfoSection();
		toolkit.createForm(form.getBody()); // Create an empty grid cell
		createDeployButton();
	}
	
	private void createMainForm() {
		form = toolkit.createForm(composite);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		form.setLayoutData(layoutData);		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;
		form.getBody().setLayout(layout);
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	private void createDeployButton() {
		deployButton = toolkit.createButton(form.getBody(), "Deploy Process Archive...", SWT.PUSH);
		deployButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		deployButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (cancelOrSaveAndContinue()) {
					createProcessDeployer().deploy();
				}
			}
		});
	}
	
	private ProcessDeployer createProcessDeployer() {
		ProcessDeployer result = new ProcessDeployer();		
		String location = null;
		if (saveButton.isEnabled()) {
			location = locationText.getText();
		}
		result.setTargetLocation(location);
		result.setServerName(nameText.getText());
		result.setServerPort(portText.getText());
		result.setServerDeployer(deployerText.getText());
		result.setUseCredentials(useCredentialsButton.getSelection());
		if (useCredentialsButton.getSelection()) {
			result.setUsername(usernameText.getText() == null ? "" : usernameText.getText());
			result.setPassword(passwordText.getText() == null ? "" : passwordText.getText());
		}
		result.setShell(form.getShell());
		result.setProcessFolder(processFolder);
		result.setFilesAndFolders(getIncludedFiles());
		result.setClassesAndResources(getClassesAndResources());
		return result;
	}
	
	private ArrayList getIncludedFiles() {
		ArrayList result = new ArrayList();
		Object[] objects = includeFilesTreeViewer.getCheckedElements();
		for (int i = 0; i < objects.length; i++) {
			result.add(objects[i]);
		}
		return result;
	}
	
	private ArrayList getClassesAndResources() {
		ArrayList result = new ArrayList();
		Object[] objects = includeClassesTreeViewer.getCheckedElements();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] instanceof ICompilationUnit) {
				String string = getResourceName(((ICompilationUnit)objects[i]).getResource());
				result.add(string.substring(0, string.lastIndexOf(".java")) + ".class");
			} else if (objects[i] instanceof IFile) {
				result.add(getResourceName((IFile)objects[i]));
			}
		}
		return result;
	}
	
	private String getResourceName(IResource resource) {
		IPackageFragmentRoot root = getPackageFragmentRoot(resource);
		if (root == null) {
			return null;
		} else {
			int index = root.getResource().getProjectRelativePath().toString().length() + 1;
			return resource.getProjectRelativePath().toString().substring(index);
		}
	}
	
	private IPackageFragmentRoot getPackageFragmentRoot(IResource resource) {
		IPackageFragmentRoot root = null;
		IResource r = resource;
		while (r != null) {
			IJavaElement javaElement = JavaCore.create(r);
			if (javaElement != null && javaElement instanceof IPackageFragmentRoot) {
				root = (IPackageFragmentRoot)javaElement;
				break;
			}
			r = r.getParent();
		}
		return root;
	}
	
	private Composite createServerInfoFormClient() {
		Section serverInfoDetails = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		serverInfoDetails.marginWidth = 5;
		serverInfoDetails.setText("Deployment Server Settings");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = GridData.BEGINNING;
		serverInfoDetails.setLayoutData(gridData);
		
		Composite infoFormClient =  toolkit.createComposite(serverInfoDetails);
		serverInfoDetails.setClient(infoFormClient);
		serverInfoDetails.setDescription("Specify the settings of the server you wish to deploy to.");
		toolkit.paintBordersFor(infoFormClient);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		layout.numColumns = 3;
		infoFormClient.setLayout(layout);
		return infoFormClient;
	}
	
	private void createServerInfoSection() {		
		Composite serverInfoFormClient = createServerInfoFormClient();		
		createServerNameField(serverInfoFormClient);
		createServerPortField(serverInfoFormClient);
		createServerDeployerField(serverInfoFormClient);
		createUseCredentialsButton(serverInfoFormClient);
		createUseCredentialsGroup(serverInfoFormClient);
		createTestConnectionButton(serverInfoFormClient);
	}

	private void createLocalSaveSection() {		
		Composite localSaveFormClient = createLocalSaveFormClient();	
		createSaveLocallyCheckBox(localSaveFormClient);
		createSaveLocationField(localSaveFormClient);
		createSaveButton(localSaveFormClient);
	}

	private void createServerNameField(Composite infoFormClient) {
		Label nameLabel = toolkit.createLabel(infoFormClient, "Server Name:");
		nameLabel.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		nameText = toolkit.createText(infoFormClient, "");
		String nameString = Plugin.getDefault().getPreferenceStore().getString("server name");
		nameText.setText(nameString == null || "".equals(nameString) ? "localhost" : nameString);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		nameText.setLayoutData(gridData);
		nameText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				updateTestConnectionAndDeployButtons();
			}
		});
	}
	
	private void createServerPortField(Composite infoFormClient) {
		Label portLabel = toolkit.createLabel(infoFormClient, "Server Port:");
		portLabel.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		portText = toolkit.createText(infoFormClient, "");
		String portString = Plugin.getDefault().getPreferenceStore().getString("server port");
		portText.setText(portString == null || "".equals(portString)? "8080" : portString);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		portText.setLayoutData(gridData);
		portText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				updateTestConnectionAndDeployButtons();
			}
		});
	}
	
	private void createServerDeployerField(Composite infoFormClient) {
		Label deployerLabel = toolkit.createLabel(infoFormClient, "Server Deployer:");
		deployerLabel.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		deployerText = toolkit.createText(infoFormClient, "");
		String deployerString = Plugin.getDefault().getPreferenceStore().getString("server deployer");
		deployerText.setText(deployerString == null || "".equals(deployerString)? "/jbpm-console/upload" : deployerString);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		deployerText.setLayoutData(gridData);
		deployerText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				updateTestConnectionAndDeployButtons();
			}
		});
	}
	
	private void createUserNameField(Composite composite) {
		Label usernameLabel = toolkit.createLabel(composite, "Username:");
		usernameLabel.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		usernameText = toolkit.createText(composite, "", SWT.BORDER);
		String usernameString = Plugin.getDefault().getPreferenceStore().getString("user name");
		usernameText.setText(usernameString == null ? "" : usernameString);
		usernameText.setEnabled(useCredentialsButton.getSelection());
		usernameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private void createPasswordField(Composite composite) {
		Label passwordLabel = toolkit.createLabel(composite, "Password:");
		passwordLabel.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		passwordText = toolkit.createText(composite, "", SWT.PASSWORD | SWT.BORDER);
		String passwordString = Plugin.getDefault().getPreferenceStore().getString("password");
		passwordText.setText(passwordString == null ? "" : passwordString);
		passwordText.setEnabled(useCredentialsButton.getSelection());
		passwordText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private void createUseCredentialsButton(Composite infoFormClient) {
		useCredentialsButton = toolkit.createButton(infoFormClient, "Use credentials", SWT.CHECK);
		useCredentialsButton.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		useCredentialsButton.setSelection(Plugin.getDefault().getPreferenceStore().getBoolean("use credentials"));
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		useCredentialsButton.setLayoutData(gridData);
		useCredentialsButton.addSelectionListener(new SelectionAdapter() {			
			public void widgetSelected(SelectionEvent e) {
				usernameText.setEnabled(useCredentialsButton.getSelection());
				passwordText.setEnabled(useCredentialsButton.getSelection());
			}
		});
	}
	
	private void createUseCredentialsGroup(Composite infoFormClient) {
		Composite useCredentialsGroup = toolkit.createComposite(infoFormClient, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		useCredentialsGroup.setLayoutData(gridData);
		useCredentialsGroup.setLayout(new GridLayout(2, false));
		createUserNameField(useCredentialsGroup);
		createPasswordField(useCredentialsGroup);
	}
	
	private void createTestConnectionButton(Composite infoFormClient) {
		testConnectionButton = toolkit.createButton(infoFormClient, "Test Connection...", SWT.PUSH);
		testConnectionButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				createProcessDeployer().pingServer();
			}
		});
	}
	
	private Composite createIncludeFilesSection() {
		Section includeFilesDetails = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		includeFilesDetails.marginWidth = 5;
		includeFilesDetails.setText("Files and Folders");
		includeFilesDetails.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite includeFilesFormClient =  toolkit.createComposite(includeFilesDetails);
		includeFilesDetails.setClient(includeFilesFormClient);
		includeFilesDetails.setDescription("Select the files and folders to include in the process archive.");
		toolkit.paintBordersFor(includeFilesFormClient);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		layout.numColumns = 1;
		includeFilesFormClient.setLayout(layout);

		Tree tree = toolkit.createTree(includeFilesFormClient, SWT.CHECK);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		includeFilesTreeViewer = new IncludeInDeploymentTreeViewer(tree);
		includeFilesTreeViewer.setContentProvider(new IncludeFilesTreeContentProvider());
		includeFilesTreeViewer.setLabelProvider(new WorkbenchLabelProvider());
		includeFilesTreeViewer.setInput(processFolder);
		tree.getDisplay().asyncExec(new Runnable() {
			public void run() {
				includeFilesTreeViewer.setCheckedElements(getElementsToCheckFor(processFolder).toArray());
			}			
		});
		
		final Button includeFilesDefaultButton = toolkit.createButton(includeFilesFormClient, "Reset Defaults", SWT.PUSH);
		includeFilesDefaultButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		includeFilesDefaultButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				includeFilesDefaultButton.getDisplay().asyncExec(new Runnable() {
					public void run() {
						includeFilesTreeViewer.setCheckedElements(getElementsToCheckFor(processFolder).toArray());
					}			
				});
			}			
		});
		

		return includeFilesFormClient;
	}
	
	private ArrayList getElementsToCheckFor(IFolder folder) {
		ArrayList list = new ArrayList();
		try {
			IResource[] members = folder.members();
			for (int i = 0; i < members.length; i++) {
				list.add(members[i]);
				if (members[i] instanceof IFolder) {
					list.addAll(getElementsToCheckFor((IFolder)members[i]));
				}
			}
		} catch(CoreException e) {
			Logger.logError(e);
		}
		return list;
	}
	
	private Set getElementsToCheckFor(IJavaProject project) {
		Set result = new HashSet();
		try {
			if (project != null) {
				Set javaClassNames = JavaClassNameCollector.getJavaClassNames(editor.getProcessDefinition());
				Iterator iterator = javaClassNames.iterator();
				while (iterator.hasNext()) {
					IType type = project.findType((String)iterator.next());	
					if (type != null) {
						result.add(type.getCompilationUnit());
					}
				}
			}
		}
		catch (JavaModelException e) {
			Logger.logError(e);
		}
		return result;
	}
	
	private Composite createIncludeClassesSection() {
		Section includeClassesDetails = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		includeClassesDetails.marginWidth = 5;
		includeClassesDetails.setText("Java Classes and Resources");
		includeClassesDetails.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite includeClassesFormClient =  toolkit.createComposite(includeClassesDetails);
		includeClassesDetails.setClient(includeClassesFormClient);
		includeClassesDetails.setDescription("Select the Java classes and resources to include in the process archive.");
		toolkit.paintBordersFor(includeClassesFormClient);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		layout.numColumns = 1;
		includeClassesFormClient.setLayout(layout);

		Tree tree = toolkit.createTree(includeClassesFormClient, SWT.CHECK);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		includeClassesTreeViewer = new IncludeInDeploymentTreeViewer(tree);
		includeClassesTreeViewer.setContentProvider(new IncludeClassesTreeContentProvider());
		includeClassesTreeViewer.setLabelProvider(new WorkbenchLabelProvider());
		final IJavaProject project = JavaCore.create(processFolder.getProject());
		if (project != null) {
			includeClassesTreeViewer.setInput(project);
		}
		composite.getDisplay().asyncExec(new Runnable() {
			public void run() {
				includeClassesTreeViewer.setCheckedElements(getElementsToCheckFor(project).toArray());
			}			
		});

		final Button includeClassesDefaultButton = toolkit.createButton(includeClassesFormClient, "Reset Defaults", SWT.PUSH);
		includeClassesDefaultButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		includeClassesDefaultButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				composite.getDisplay().asyncExec(new Runnable() {
					public void run() {
						includeClassesTreeViewer.setCheckedElements(getElementsToCheckFor(project).toArray());
					}			
				});
			}			
		});
		
		return includeClassesFormClient;
	}

	private Composite createLocalSaveFormClient() {
		Section httpInfoDetails = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION);
		httpInfoDetails.marginWidth = 5;
		httpInfoDetails.setText("Local Save Settings");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = GridData.BEGINNING;
		httpInfoDetails.setLayoutData(gridData);
		
		Composite detailClient =  toolkit.createComposite(httpInfoDetails);
		httpInfoDetails.setClient(detailClient);
		httpInfoDetails.setDescription("Choose if and where you wish to save the process archive locally.");
		toolkit.paintBordersFor(detailClient);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		layout.numColumns = 3;
		detailClient.setLayout(layout);
		return detailClient;
	}

	private void createSaveLocallyCheckBox(Composite localSaveFormclient) {
		saveLocallyButton = toolkit.createButton(localSaveFormclient, "Save Process Archive Locally", SWT.CHECK);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		saveLocallyButton.setLayoutData(gridData);
		saveLocallyButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean selection = ((Button)e.widget).getSelection();
				locationText.setEditable(selection);
				locationButton.setEnabled(selection);
				updateSaveAndDeployButtons(selection);
			}
		});
	}
	
	private void updateSaveAndDeployButtons(boolean selection) {
		if (!selection) {
			deployButton.setEnabled(testConnectionButton.isEnabled());
			saveButton.setEnabled(false);
		} else {
			if (notEmpty(locationText)) {
				saveButton.setEnabled(true);
				deployButton.setEnabled(testConnectionButton.isEnabled());
			} else {
				saveButton.setEnabled(false);
				deployButton.setEnabled(false);
			}
		}
	}
	
	private boolean notEmpty(Text text) {
		String string = text.getText();
		return string != null && !"".equals(string);
	}
	
	private void updateTestConnectionAndDeployButtons() {
		if (notEmpty(nameText) && notEmpty(portText) && notEmpty(deployerText)) {
			testConnectionButton.setEnabled(true);
			if (saveLocallyButton.getSelection()) {
				deployButton.setEnabled(saveButton.isEnabled());
			} else {
				deployButton.setEnabled(true);
			}
		} else {
			testConnectionButton.setEnabled(false);
			deployButton.setEnabled(false);
		}
	}
	
	private void createSaveLocationField(Composite localSaveFormclient) {
		Label locationLabel = toolkit.createLabel(localSaveFormclient, "Location:");
		locationLabel.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		locationText = toolkit.createText(localSaveFormclient, "");
		locationText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		locationText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				updateSaveAndDeployButtons(true);
			}
		});
		locationText.setEditable(false);
		locationButton = toolkit.createButton(localSaveFormclient, "Search...", SWT.PUSH);
		locationButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				searchLocation();
			}
		});
		locationButton.setEnabled(false);
	}
	
	private void searchLocation() {
		FileDialog dialog = new FileDialog(form.getShell(), SWT.OPEN);
		String result = dialog.open();
		if (result != null) {
			locationText.setText(result);
			updateSaveAndDeployButtons(true);
		}		
	}
	
	private void createSaveButton(Composite localSaveFormClient) {
		saveButton = toolkit.createButton(localSaveFormClient, "Save Without Deploying...", SWT.PUSH);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = SWT.BEGINNING;
		saveButton.setLayoutData(gridData);
		saveButton.setEnabled(false);
		saveButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if (cancelOrSaveAndContinue()) {
					createProcessDeployer().saveWithoutDeploying();
				}
			}			
		});
	}
	
	public void refresh(final ArrayList objectsToRefresh) {		
		form.getDisplay().asyncExec(new Runnable() {
			public void run() {
				refreshIncludeClassesTreeViewer(objectsToRefresh);
				refreshIncludeFilesTreeViewer(objectsToRefresh);
			}			
		});
	}
	
	private void refreshIncludeFilesTreeViewer(ArrayList objectsToRefresh) {
		Object[] elements = includeFilesTreeViewer.getCheckedElements();
		includeFilesTreeViewer.refresh();
		includeFilesTreeViewer.setCheckedElements(elements);
		IWorkspaceRoot root = processFolder.getWorkspace().getRoot();
		for (int i = 0; i < objectsToRefresh.size(); i++) {
			IPath path = (IPath)objectsToRefresh.get(i);
			if (root.getFile(path).exists()) {
				includeFilesTreeViewer.setChecked(root.getFile(path), true);
			} else if (root.getFolder(path).exists()) {
				includeFilesTreeViewer.setChecked(root.getFolder(path), true);
			}
		}
	}
	
	private void refreshIncludeClassesTreeViewer(ArrayList objectsToRefresh) {
		Set referencedJavaClassNames = null;
		Object[] elements = includeClassesTreeViewer.getCheckedElements();
		includeClassesTreeViewer.refresh();
		includeClassesTreeViewer.setCheckedElements(elements);
		IWorkspaceRoot root = processFolder.getWorkspace().getRoot();
		for (int i = 0; i < objectsToRefresh.size(); i++) {
			IPath path = (IPath)objectsToRefresh.get(i);
			IJavaElement javaElement = JavaCore.create(root.getFile(path));
			if (javaElement != null && javaElement instanceof ICompilationUnit) {
				if (referencedJavaClassNames == null) {
					referencedJavaClassNames = JavaClassNameCollector.getJavaClassNames(editor.getProcessDefinition());
				}
				String name = getTypeName((ICompilationUnit)javaElement);
				boolean checkNeeded = referencedJavaClassNames.contains(name);
				includeClassesTreeViewer.setChecked(javaElement, checkNeeded);
			}
		}
	}
	
	private String getTypeName(ICompilationUnit unit) {
		try {
			IType[] types = unit.getTypes();
			if (types.length > 0) {
				return types[0].getFullyQualifiedName();
			} 
			} catch (JavaModelException e) {
				Logger.logError(e);
			}
		return null;
	}
	
	private boolean cancelOrSaveAndContinue() {
		IEditorPart editor = getEditorPart();
		boolean result = true;
		if (editor.isDirty()) {
			int saveProceedCancel = openSaveProceedCancelDialog();
			if (saveProceedCancel == 2) {
				result = false;
			} else if (saveProceedCancel == 0) {
				editor.doSave(null);
			}
		}
		return result;
	}

	private int openSaveProceedCancelDialog() {
        MessageDialog dialog = new MessageDialog(
        		getWorkBenchWindow().getShell(), 
        	"Save Resource", 
        	null, 
        	"'" + processFolder.getName() + "' has been modified. Save changes before deploying?", 
        	MessageDialog.QUESTION, 
        	new String[] { 
        		IDialogConstants.YES_LABEL, 
        		IDialogConstants.NO_LABEL,
        		IDialogConstants.CANCEL_LABEL}, 
        	0);
        return dialog.open();
		
	}
	
	private IWorkbenchWindow getWorkBenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}
	
	private IEditorPart getEditorPart() {
		return getWorkBenchWindow().getActivePage().getActiveEditor();
	}
	
	public void refresh() {
		if (composite.isDisposed()) return;
		final IJavaProject project = JavaCore.create(processFolder.getProject());
		composite.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (!includeClassesTreeViewer.getTree().isDisposed()) {
					includeClassesTreeViewer.refresh();
					includeClassesTreeViewer.setCheckedElements(getElementsToCheckFor(project).toArray());
				}
			}			
		});
		composite.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (!includeFilesTreeViewer.getTree().isDisposed()) {
					includeFilesTreeViewer.refresh();
					includeFilesTreeViewer.setCheckedElements(getElementsToCheckFor(processFolder).toArray());
				}
			}			
		});
	}
	
}
