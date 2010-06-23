package org.jbpm.gd.jpdl.deployment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jbpm.gd.jpdl.Logger;
import org.jbpm.gd.jpdl.editor.JpdlEditor;

public class ProcessArchiveBuilder {
	
	private JpdlEditor jpdlEditor;
	
	public ProcessArchiveBuilder(JpdlEditor jpdlEditor) {
		this.jpdlEditor = jpdlEditor;
	}
	
	public byte[] build() {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			if (jpdlEditor.getDeploymentInfo() != null) {
				addProcessInfoFile(zipOutputStream);
				addGraphicalInfoFile(zipOutputStream);
				addProcessImageFile(zipOutputStream);
				addAdditionalFiles(zipOutputStream);
				addClassesAndResources(zipOutputStream);
			}
			zipOutputStream.close();
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			showBuildProcessArchiveException();
			return null;
		}
	}
	
	private void showBuildProcessArchiveException() {
		MessageDialog dialog = new MessageDialog(
				jpdlEditor.getSite().getShell(), 
				"Build Process Archive Failed", 
				null,
				"There was a problem creating the process archive to deploy.",
				SWT.ICON_ERROR, 
				new String[] { "OK" }, 
				0);
		dialog.open();
	}

	private void addFile(ZipOutputStream zipOutputStream, IFile file, String entryName) throws Exception {
		byte[] buffer = new byte[256];
		InputStream inputStream;
		try {
			inputStream = file.getContents();		
		} catch (CoreException e) {
			Logger.logError("CoreException getting contents of file " + file.getName(), e);
			throw e;
		}
		try {
			zipOutputStream.putNextEntry(new ZipEntry(entryName));
		} catch (IOException e) {
			Logger.logError("IOException creating entry '" + entryName + "'.", e);
		}
		int read;
		try {
			while ((read = inputStream.read(buffer)) != -1) {
				zipOutputStream.write(buffer, 0, read);
			}
		} catch(IOException e) {
			Logger.logError("IOException writing file '"+ entryName +"'.", e);
		}
	}
	
	private void addProcessInfoFile(ZipOutputStream zipOutputStream) throws Exception {
		IFile processInfoFile = jpdlEditor.getDeploymentInfo().getProcessInfoFile();
		if (processInfoFile != null) {
			addFile(zipOutputStream, processInfoFile, "processdefinition.xml");
		}
	}
	
	private void addGraphicalInfoFile(ZipOutputStream zipOutputStream) throws Exception {
		IFile graphicalInfoFile = jpdlEditor.getDeploymentInfo().getGraphicalInfoFile();
		if (graphicalInfoFile != null) {
			addFile(zipOutputStream, graphicalInfoFile, "gpd.xml");
		}
	}
	
	private void addProcessImageFile(ZipOutputStream zipOutputStream) throws Exception {
		IFile processImageFile = jpdlEditor.getDeploymentInfo().getImageFile();
		if (processImageFile != null) {
			addFile(zipOutputStream, processImageFile, "processimage.jpg");
		}
	}
	
	private void addAdditionalFiles(ZipOutputStream zipOutputStream) throws Exception {
		Object[] additionalFiles = jpdlEditor.getDeploymentInfo().getAdditionalFiles();
		if (additionalFiles == null) return;
		for (int i = 0; i < additionalFiles.length; i++) {
			if (!(additionalFiles[i] instanceof IFile)) continue;
			IFile file = (IFile)additionalFiles[i];
			addFile(zipOutputStream, file, file.getName());
		}
	}
	
	private void addClassesAndResources(ZipOutputStream zipOutputStream) throws Exception {
		Object[] classesAndResources = jpdlEditor.getDeploymentInfo().getClassesAndResources();
		if (classesAndResources == null) return;
		for (int i = 0; i < classesAndResources.length; i++) {
			if (classesAndResources[i] instanceof IFile) {
				addFile(zipOutputStream, (IFile)classesAndResources[i]);
			} else if (classesAndResources[i] instanceof ICompilationUnit) {
				addCompilationUnit(zipOutputStream, (ICompilationUnit)classesAndResources[i]);
			} else if (classesAndResources[i] instanceof IClassFile) {
				addClassFile(zipOutputStream, (IClassFile)classesAndResources[i]);
			}
		}
	}
	
	private void addFile(ZipOutputStream zipOutputStream, String name, ClassLoader classLoader) throws Exception {
		byte[] buff = new byte[256];
		zipOutputStream.putNextEntry(new ZipEntry("classes/" + name));
		InputStream is = classLoader.getResourceAsStream(name);
		int read;
		while ((read = is.read(buff)) != -1) {
			zipOutputStream.write(buff, 0, read);
		}
		is.close();
	}
	
	private void addFile(ZipOutputStream zipOutputStream, IFile file) throws Exception {
		addFile(zipOutputStream, getResourceName(file), getClassLoader());
	}
	
	private void addCompilationUnit(ZipOutputStream zipOutputStream, ICompilationUnit compilationUnit) throws Exception {
		String name = getResourceName(compilationUnit.getResource());
		name = name.substring(0, name.lastIndexOf(".java")) + ".class";
		ClassLoader classLoader = getClassLoader();
		addFile(zipOutputStream, name, classLoader);
		addInnerClasses(zipOutputStream, name, classLoader);
	}
	
	private String[] getNestedClassNames(String name, ClassLoader classLoader) {
		final String className = 
			name.substring(name.lastIndexOf('/') + 1, name.length() - 6) + '$';
		URL url = classLoader.getResource(name);
		File file = new File(url.getFile());
		File folder = new File(file.getParent());
		return folder.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(className);
			}
		});
	}
	
	private void addInnerClasses(ZipOutputStream zipOutputStream, String name, ClassLoader classLoader) throws Exception {
		String[] nestedClassNames = getNestedClassNames(name, classLoader);
		if (nestedClassNames == null) return;
		for (int i = 0; i < nestedClassNames.length; i++) {
			String fileName = name.substring(0, name.lastIndexOf("/") + 1) + nestedClassNames[i];
			addFile(zipOutputStream, fileName, classLoader);
		}
		
	}
	
	private void addClassFile(ZipOutputStream zipOutputStream, IClassFile classFile) throws Exception {
		String name = classFile.getParent().getElementName().replace('.', '/') + "/" + classFile.getElementName();
		zipOutputStream.putNextEntry(new ZipEntry("classes/" + name));
		zipOutputStream.write(classFile.getBytes());
	}
	
	private ClassLoader getClassLoader() throws Exception {
		return new URLClassLoader(getProjectClasspathUrls(), getClass().getClassLoader());
	}

	private URL[] getProjectClasspathUrls() throws Exception {
		URL[] urls = new URL[0];		
		IProject project = getProject();
		IJavaProject javaProject = JavaCore.create(project);
		if (javaProject == null) {
			return urls;
		}
		String[] pathArray = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);
		urls = new URL[pathArray.length];
		for (int i = 0; i < pathArray.length; i++) {
			urls[i] = new File(pathArray[i]).toURI().toURL();
		}
		return urls;
	}
	
	private IProject getProject() {
		IEditorInput editorInput = jpdlEditor.getEditorInput();
		if (!(editorInput instanceof IFileEditorInput)) return null; 
		IFile file = ((IFileEditorInput)editorInput).getFile();
		return file.getProject();
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
	
	private String getResourceName(IResource resource) {
		IPackageFragmentRoot root = getPackageFragmentRoot(resource);
		if (root == null) {
			return null;
		} else {
			int index = root.getResource().getProjectRelativePath().toString().length() + 1;
			return resource.getProjectRelativePath().toString().substring(index);
		}
	}
	
}
