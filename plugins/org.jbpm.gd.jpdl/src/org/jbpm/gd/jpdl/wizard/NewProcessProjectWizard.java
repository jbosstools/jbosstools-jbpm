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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.jboss.tools.jbpm.preferences.JbpmInstallation;
import org.jboss.tools.jbpm.preferences.PreferencesManager;
import org.jbpm.gd.jpdl.Logger;
import org.jbpm.gd.jpdl.util.JbpmClasspathContainer;

public class NewProcessProjectWizard extends Wizard implements INewWizard {

	private WizardNewProjectCreationPage mainPage;
	private ConfigureRuntimePage configureRuntimePage;
	private NewProcessProjectDetailsWizardPage coreJbpmPage;
	private IProject newProject;
	private IWorkbench workbench;
	
	public boolean canFinish() {
		return super.canFinish() && coreJbpmPage.combo.getItemCount() > 0 && coreJbpmPage.combo.getSelectionIndex() != -1;
	}
	   
	public void init(IWorkbench w, IStructuredSelection currentSelection) {
	    this.workbench = w;
		setNeedsProgressMonitor(true);
		setWindowTitle("New Process Project");
	}

	public void addPages() {
		addMainPage();
		if (!isJbpm3RuntimeAvailable()) {
			addConfigureRuntimePage();
		}
		addChooseRuntimePage();
	}
	
	private boolean isJbpm3RuntimeAvailable() {
		Map<String, JbpmInstallation> installations = PreferencesManager.INSTANCE.getJbpmInstallationMap();
		Iterator<String> iterator = installations.keySet().iterator();
		while (iterator.hasNext()) {
			JbpmInstallation installation = installations.get(iterator.next());
			if ("jBPM3".equals(installation.version)) {
				return true;
			}
		}
		return false;
	}
	
	private void addConfigureRuntimePage() {
		configureRuntimePage = new ConfigureRuntimePage();
		addPage(configureRuntimePage);
	}

	private void addChooseRuntimePage() {
		coreJbpmPage = new NewProcessProjectDetailsWizardPage();
		addPage(coreJbpmPage);
	}

	private void addMainPage() {
		super.addPages();
		setWindowTitle("New Process Project");
		mainPage = new WizardNewProjectCreationPage("basicNewProjectPage");
		mainPage.setTitle("Process Project");
		mainPage.setDescription("Create a new process project.");		
		addPage(mainPage);
	}
	
	private IProject createNewProject() {
		final IProject newProjectHandle = mainPage.getProjectHandle();
		final IProjectDescription description = createProjectDescription(newProjectHandle);
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				createProject(description, newProjectHandle, monitor);
			}
		};
		runProjectCreationOperation(op, newProjectHandle);
		return newProjectHandle;
	}
	
	private void addJRELibraries(IJavaProject javaProject) throws JavaModelException {		
		ArrayList entries = new ArrayList();
		entries.addAll(Arrays.asList(javaProject.getRawClasspath()));
		entries.addAll(Arrays.asList(PreferenceConstants.getDefaultJRELibrary()));
		javaProject.setRawClasspath((IClasspathEntry[])entries.toArray(new IClasspathEntry[entries.size()]), null);
	}
	
	private void addSourceFolders(IJavaProject javaProject) throws JavaModelException, CoreException {
		ArrayList entries = new ArrayList();
		entries.addAll(Arrays.asList(javaProject.getRawClasspath()));
		addSourceFolder(javaProject, entries, "src/main/java");
		addSourceFolder(javaProject, entries, "src/main/config");
		addSourceFolder(javaProject, entries, "src/main/jpdl");
		addSourceFolder(javaProject, entries, "src/test/java");
		javaProject.setRawClasspath((IClasspathEntry[])entries.toArray(new IClasspathEntry[entries.size()]), null);
	}

	private void addSourceFolder(IJavaProject javaProject, ArrayList entries, String path) throws CoreException {
		IFolder folder = javaProject.getProject().getFolder(path);
		createFolder(folder);
		IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(folder);
		entries.add(JavaCore.newSourceEntry(root.getPath()));
	}
	
	private void createFolder(IFolder folder) throws CoreException {
		IContainer parent = folder.getParent();
		if (parent != null && !parent.exists() && parent instanceof IFolder) {
			createFolder((IFolder)parent);
		}
		folder.create(true, true, null);
	}
	
	private JbpmInstallation getJbpmInstallation() {
		return PreferencesManager.INSTANCE.getJbpmInstallation(getCoreJbpmName());
	}
	
	private void createJbpmLibraryContainer(IJavaProject javaProject) throws JavaModelException {
		JavaCore.setClasspathContainer(
				new Path("JBPM/" + getJbpmInstallation().name),
				new IJavaProject[] { javaProject },
				new IClasspathContainer[] { new JbpmClasspathContainer(javaProject, getJbpmInstallation()) },
				null);		
	}
	
	private String getCoreJbpmName() {
		return coreJbpmPage.getCoreJbpmName();
	}
	
	private void addJbpmLibraries(IJavaProject javaProject) throws JavaModelException {
		createJbpmLibraryContainer(javaProject);
		ArrayList entries = new ArrayList();
		entries.addAll(Arrays.asList(javaProject.getRawClasspath()));
		entries.add(JavaCore.newContainerEntry(new Path("JBPM/" + getJbpmInstallation().name)));
		javaProject.setRawClasspath((IClasspathEntry[])entries.toArray(new IClasspathEntry[entries.size()]), null);
	}
	
	private void createOutputLocation(IJavaProject javaProject) throws JavaModelException, CoreException {
		IFolder binFolder = javaProject.getProject().getFolder("bin");
		createFolder(binFolder);
		IPath outputLocation = binFolder.getFullPath();
		javaProject.setOutputLocation(outputLocation, null);
	}
	
	private void addJavaBuilder(IJavaProject javaProject) throws CoreException {
		IProjectDescription desc = javaProject.getProject().getDescription();
		ICommand command = desc.newCommand();
		command.setBuilderName(JavaCore.BUILDER_ID);
		desc.setBuildSpec(new ICommand[] { command });
		javaProject.getProject().setDescription(desc, null);
	}
	
	private void createJavaProject() { 
		try {
			newProject = createNewProject();
			newProject.setPersistentProperty(new QualifiedName("", "jbpmName"), getCoreJbpmName());
			IJavaProject javaProject = JavaCore.create(newProject);
			createOutputLocation(javaProject);
			addJavaBuilder(javaProject);
			setClasspath(javaProject);
			createInitialContent(javaProject);
			newProject.build(IncrementalProjectBuilder.FULL_BUILD, null);
		} catch (JavaModelException e) {
			ErrorDialog.openError(getShell(), "Problem creating java project", null, e.getStatus());			
		} catch (CoreException e) {
			ErrorDialog.openError(getShell(), "Problem creating java project", null, e.getStatus());						
		} catch (IOException e) {
			ErrorDialog.openError(getShell(), "Problem creating java project", null, null);									
		}
	}
	
	private void createInitialContent(IJavaProject javaProject) throws CoreException, JavaModelException, IOException {
		if (coreJbpmPage.checkbox.getSelection()) {
			createMessageActionHandler(javaProject);
			createSimpleProcessTest(javaProject);
			createSimpleProcessDefinition(javaProject);
		}
		copyJbpmResources(javaProject);
	}
	
	private void createSimpleProcessDefinition(IJavaProject javaProject) throws CoreException, JavaModelException, IOException {
		JbpmInstallation jbpmInstallation = PreferencesManager.INSTANCE.getJbpmInstallation(getCoreJbpmName());
		if (jbpmInstallation == null) return;
//		IFolder processesFolder = javaProject.getProject().getFolder("processes");
//		if (!processesFolder.exists()) {
//			processesFolder.create(true, true, null);
//		}
		IFolder folder = javaProject.getProject().getFolder("src/main/jpdl/simple");
		if (!folder.exists()) {
			folder.create(true, true, null);
		}
		String location = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(jbpmInstallation.location);
		String fromPath = getLocation("examples", location);
		if (fromPath == null) {
			fromPath = "/src/process.examples/simple.par";
		} else {
			fromPath = fromPath + "/simple/src/main/jpdl";
		}
		fromPath = new Path(location).append(fromPath).toOSString();
		File fromDir = new File(fromPath);
		if (!fromDir.exists()) return;
		File[] files = fromDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			copyJbpmResource(files[i], folder);
		}
	}

	private void createSimpleProcessTest(IJavaProject javaProject) throws JavaModelException, IOException {
		String resourceName = "org/jbpm/gd/jpdl/resource/SimpleProcessTest.java.template";
		IFolder folder = javaProject.getProject().getFolder("src/test/java");
		IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(folder);
		IPackageFragment pack = root.createPackageFragment("com.sample", true, null);
		InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName);
		byte[] content = readStream(stream);
		pack.createCompilationUnit("SimpleProcessTest.java", new String(content), true, null);
	}

	private void createMessageActionHandler(IJavaProject javaProject) throws JavaModelException, IOException {
		String resourceName = "org/jbpm/gd/jpdl/resource/MessageActionHandler.java.template";
		IFolder folder = javaProject.getProject().getFolder("src/main/java");
		IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(folder);
		IPackageFragment pack = root.createPackageFragment("com.sample.action", true, null);
		InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName);
		byte[] content = readStream(stream);
		pack.createCompilationUnit("MessageActionHandler.java", new String(content), true, null);
	}
	
	private void copyJbpmResources(IJavaProject javaProject) throws CoreException {
		JbpmInstallation jbpmInstallation = PreferencesManager.INSTANCE.getJbpmInstallation(getCoreJbpmName());
		if (jbpmInstallation == null) return;
		String location = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(jbpmInstallation.location);
		IFolder folder = javaProject.getProject().getFolder("src/main/config");
		String fromPath = getLocation("config", location);
		if (fromPath == null) {
			fromPath = "/src/config.files";
		}
		fromPath = new Path(location).append(fromPath).toOSString();
		File fromDir = new File(fromPath);
		if (!fromDir.exists()) return;
		File[] files = fromDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory()) {
				copyJbpmResource(files[i], folder);
			}
		}
	}
	
	private String getLocation(String selector, String baseLocation) {
		String result = null;
		try { 
			IPath locationPath = new Path(baseLocation);
			Document document = new SAXReader().read(locationPath.append("src/resources/gpd/version.info.xml").toFile());
			XPath xpath = document.createXPath("/jbpm-version-info/" + selector);
			List list = xpath.selectNodes(document);
			if (!list.isEmpty()) {
				result = (String)((Element)list.get(0)).attribute("path").getData();
			}
		} 
		catch (MalformedURLException e) { }
		catch (DocumentException e) { } 	
		return result;
	}
	
	private void copyJbpmResource(File source, IFolder destination) throws CoreException {
		try {
			IFile file = destination.getFile(source.getName());
			file.create(new FileInputStream(source), true, null);			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void setClasspath(IJavaProject javaProject) throws JavaModelException, CoreException {
		javaProject.setRawClasspath(new IClasspathEntry[0], null);
		addSourceFolders(javaProject);
		addJRELibraries(javaProject);
		addJbpmLibraries(javaProject);
		// Hack to overcome the problems of the classpath container not being created in the classpath.
		javaProject.getRawClasspath();
	}

	private IProjectDescription createProjectDescription(
			IProject newProjectHandle) {
		IPath newPath = null;
		if (!mainPage.useDefaults())
			newPath = mainPage.getLocationPath();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProjectDescription description = workspace
				.newProjectDescription(newProjectHandle.getName());
		description.setLocation(newPath);
		addJavaNature(description);
		return description;
	}
	
	private void addJavaNature(IProjectDescription description) {
		ArrayList natures = new ArrayList();
		natures.addAll(Arrays.asList(description.getNatureIds()));
		natures.add(JavaCore.NATURE_ID);
		description.setNatureIds((String[])natures.toArray(new String[natures.size()]));
	}

	private void runProjectCreationOperation(WorkspaceModifyOperation op,
			IProject newProjectHandle) {
		try {
			getContainer().run(false, true, op);
		} catch (InterruptedException e) {
			Logger.logError("InterruptedException while creating project", e);
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			if (t instanceof CoreException) {
				handleCoreException(newProjectHandle, (CoreException) t);
			} else {
				handleOtherProblem(t);
			}
		}
	}

	private void handleOtherProblem(Throwable t) {
		MessageDialog.openError(getShell(), "Creation Problems",
				"Internal error: " + t.getMessage());
	}

	private void handleCoreException(final IProject newProjectHandle,
			CoreException e) {
		if (e.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
			MessageDialog
					.openError(
							getShell(),
							"Creation Problems",
							"The underlying file system is case insensitive. There is an existing project which conflicts with '"
									+ newProjectHandle.getName() + "'.");
		} else {
			ErrorDialog.openError(getShell(), "Creation Problems", null, e
					.getStatus());
		}
	}

	void createProject(IProjectDescription description, IProject projectHandle,
			IProgressMonitor monitor) throws CoreException,
			OperationCanceledException {
		try {
			monitor.beginTask("", 2000);
			projectHandle.create(description, new SubProgressMonitor(monitor,
					1000));
			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			projectHandle.open(IResource.BACKGROUND_REFRESH,
					new SubProgressMonitor(monitor, 1000));
		} finally {
			monitor.done();
		}
	}

	public IProject getNewProject() {
		return newProject;
	}

	public boolean performFinish() {
		if (!isJbpm3RuntimeAvailable()) {
			String name = configureRuntimePage.nameText.getText();
			String location = configureRuntimePage.locationText.getText();
			String version = "jBPM3";
			PreferencesManager.INSTANCE.initializeDefaultJbpmInstallation(name, location, version);
		}
		getContainer().updateButtons();
		createJavaProject();
		if (newProject == null) {
			getContainer().updateButtons();
			return false;
		}
		updatePerspective();
		selectAndReveal(newProject);
		getContainer().updateButtons();
		return true;
	}

	protected void updatePerspective() {
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (page.findView("org.eclipse.ui.views.PropertySheet") == null) {
				page.showView("org.eclipse.ui.views.PropertySheet");
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	protected void selectAndReveal(IResource newResource) {
		selectAndReveal(newResource, workbench.getActiveWorkbenchWindow());
	}

    private void selectAndReveal(IResource resource,
	           IWorkbenchWindow window) {
		if (!inputValid(resource, window)) return;   
		Iterator itr = getParts(window.getActivePage()).iterator();
		while (itr.hasNext()) {
		    selectAndRevealTarget(
					window, 
					new StructuredSelection(resource), 
					getTarget((IWorkbenchPart)itr.next()));
		}
	}
	
	private boolean inputValid(IResource resource, IWorkbenchWindow window) {
		if (window == null || resource == null) return false;
		else if (window.getActivePage() == null) return false;
		else return true;
	}

	private void selectAndRevealTarget(IWorkbenchWindow window, final ISelection selection, ISetSelectionTarget target) {
		if (target == null) return;
		final ISetSelectionTarget finalTarget = target;
		window.getShell().getDisplay().asyncExec(new Runnable() {
		    public void run() {
		        finalTarget.selectReveal(selection);
		    }
		});
	}
	
	private ISetSelectionTarget getTarget(IWorkbenchPart part) {
        ISetSelectionTarget target = null;
        if (part instanceof ISetSelectionTarget) {
            target = (ISetSelectionTarget)part;
        }
        else {
            target = (ISetSelectionTarget)part.getAdapter(ISetSelectionTarget.class);
        }
		return target;		
	}

	private List getParts(IWorkbenchPage page) {
		List result = new ArrayList();
		addParts(result, page.getViewReferences());
		addParts(result, page.getEditorReferences());
		return result;
	}
	
	private void addParts(List parts, IWorkbenchPartReference[] refs) {
		for (int i = 0; i < refs.length; i++) {
           IWorkbenchPart part = refs[i].getPart(false);
           if (part != null) {
               parts.add(part);
           }
	    }		
	}
	
	private byte[] readStream(InputStream in) throws IOException {
		byte[] contents = null;
		int fileSize = 0;
		byte[] buffer = new byte[1024];
		int bytesRead = in.read(buffer);
		while (bytesRead != -1) {
			byte[] newContents = new byte[fileSize + bytesRead];
			if (fileSize > 0) {
				System.arraycopy(contents, 0, newContents, 0, fileSize);
			}
			System.arraycopy(buffer, 0, newContents, fileSize, bytesRead);
			contents = newContents;
			fileSize += bytesRead;
			bytesRead = in.read(buffer);
		}
		return contents;
	}
	
}