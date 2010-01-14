package org.jbpm.gd.jpdl.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.jbpm.gd.common.util.Base64Converter;
import org.jbpm.gd.jpdl.Logger;
import org.jbpm.gd.jpdl.Plugin;

public class ProcessDeployer {
	
	private static final String boundary = "AaB03x";
	
	Shell shell;
	IFolder processFolder;
	String serverName;
	String serverPort;
	String serverDeployer;
	String targetLocation;
	List classesAndResources;
	List filesAndFolders;
	boolean useCredentials = false;
	String username;
	String password;
	
	public void setShell(Shell shell) {
		this.shell = shell;
	}
	
	public void setProcessFolder(IFolder processFolder) {
		this.processFolder = processFolder;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public void setServerDeployer(String serverDeployer) {
		this.serverDeployer = serverDeployer;
	}
	
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	
	public void setTargetLocation(String targetLocation) {
		this.targetLocation = targetLocation;
	}
	
	public void setClassesAndResources(List classesAndResources) {
		this.classesAndResources = classesAndResources;
	}
	
	public void setFilesAndFolders(List filesAndFolders) {
		this.filesAndFolders = filesAndFolders;
	}
	
	public void setUseCredentials(boolean useCredentials) {
		this.useCredentials = useCredentials;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean deploy() {
		try {
			showProgressMonitorDialog();
			showSuccessDialog();
			return true;
		}
		catch (ConnectException e) {
			MessageDialog dialog = new MessageDialog(shell, "Server Not Found", null,
					"The server could not be reached. Check your connection parameters.",
					SWT.ICON_INFORMATION, new String[] { "OK" }, 0);
			dialog.open();
			return false;
        }
		catch (IOException e) {
			if (e.getMessage().contains("Server returned HTTP response code: 403 for URL")) {
				MessageDialog dialog = new MessageDialog(shell, "Not Allowed", null,
						"The server refused to perform the deployment. Check your credentials.",
						SWT.ICON_INFORMATION, new String[] { "OK" }, 0);
				dialog.open();
				return false;
			} else {
				showErrorDialog(e);
				return false;
			}
		}
        catch (Exception e) {
            // NOTE that Error's are not caught because that might halt the JVM and mask the original Error.
			showErrorDialog(e);
			return false;
		}
	}
	
	public void pingServer() {
		try {
			String urlStr = "http://" + serverName + ":" + serverPort + serverDeployer;
			URL url = new URL(urlStr);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setDoOutput(true);
			InputStream inputStream = urlConnection.getInputStream();
			StringBuffer result = new StringBuffer();
			int read;
			while ((read = inputStream.read()) != -1) {
				result.append((char)read);
			}
			MessageDialog dialog = new MessageDialog(shell, "Connection Test", null,
					"The server connection was successfully tested.",
					SWT.ICON_INFORMATION, new String[] { "OK" }, 0);
			dialog.open();
		}
		catch (ConnectException e) {
			MessageDialog dialog = new MessageDialog(shell, "Connection Test", null,
					"The server could not be reached.",
					SWT.ICON_INFORMATION, new String[] { "OK" }, 0);
			dialog.open();
		}
		catch (FileNotFoundException e) {
			MessageDialog dialog = new MessageDialog(shell, "Connection Test", null,
					"The server can be reached, but the deployer seems unavailable.",
					SWT.ICON_INFORMATION, new String[] { "OK" }, 0);
			dialog.open();
		}
		catch (Exception e) {
			Logger.logError(e);
			ErrorDialog dialog = new ErrorDialog(shell,
					"Unexpected Exception",
					"An exception happened while testing the server connection.",
					new Status(
							Status.ERROR,
							Plugin.getDefault().getBundle()
									.getSymbolicName(),
							Status.ERROR,
							"An unexpected exception caused the test connection operation to fail",
							e), Status.ERROR);
			dialog.open();
		}
	}
	
	public void saveWithoutDeploying() {
		try {
			saveParFile(createParBytes(getProjectClasspathUrls()));
		} catch (Exception e) {
			Logger.logError(e);
			ErrorDialog dialog = new ErrorDialog(shell,
					"Unexpected Exception While Saving",
					"An exception happened while saving the process definition archive",
					new Status(
							Status.ERROR,
							Plugin.getDefault().getBundle()
									.getSymbolicName(),
							Status.ERROR,
							"An unexpected exception caused the save operation to fail",
							e), Status.ERROR);
			dialog.open();
		}
	}
	
	private void showProgressMonitorDialog() throws Exception {
		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);
		progressMonitorDialog.run(false, false, new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InterruptedException {
				try {
					byte[] baos = createParBytes(getProjectClasspathUrls());
					if (targetLocation != null) {
						saveParFile(baos);
					}
					deployProcessWithServlet(baos);
					return;
				} catch (IOException e) {
					if (e.getMessage().contains("Server returned HTTP response code: 403 for URL")) {
						Logger.logError(
								"The server refused to execute the deployment. Check your credentials.",
								e);
					}
				} catch (Exception e) {
					Logger
							.logError(
									"Exception happened while deploying",
									e);
				}
				throw new InterruptedException(
						"Error while deploying, look in the Error Log for more info");
			}
		});
	}
	
	private void saveParFile(byte[] parBytes) throws IOException {
		File file = new Path(targetLocation).toFile();
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(parBytes);
		fos.close();
	}

	private void deployProcessWithServlet(byte[] parBytes) throws Exception {
		String urlStr = "http://" + serverName + ":" + serverPort + serverDeployer;
		URL url = new URL(urlStr);
		URLConnection urlConnection = url.openConnection();
		if (useCredentials) {
			String userPassword = username + ":" + password;
			String encoding = Base64Converter.encode(userPassword);
			urlConnection.setRequestProperty ("Authorization", "Basic " + encoding);
		}
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(true);
		urlConnection.setUseCaches(false);
		urlConnection.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=AaB03x");
		DataOutputStream dataOutputStream = new DataOutputStream(urlConnection
				.getOutputStream());
		dataOutputStream.writeBytes("--" + boundary + "\r\n");
		dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"definition\"; filename=\"dummy.par\"\r\n");
		dataOutputStream.writeBytes("Content-Type: application/x-zip-compressed\r\n\r\n");
		
		dataOutputStream.write(parBytes);
		
		dataOutputStream.writeBytes("\r\n--" + boundary + "--\r\n");
		dataOutputStream.flush();
		dataOutputStream.close();
		InputStream inputStream = urlConnection.getInputStream();
		StringBuffer result = new StringBuffer();
		int read;
		while ((read = inputStream.read()) != -1) {
			result.append((char)read);
		}
	}
	
	private byte[] createParBytes(URL[] urls) throws Exception {
		URLClassLoader newLoader = new URLClassLoader(urls, getClass()
				.getClassLoader());
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
		addFilesAndFolders(zipOutputStream);
		addClassesAndResources(zipOutputStream, newLoader);
		zipOutputStream.close();
		return byteArrayOutputStream.toByteArray();
	}

	private void addFilesAndFolders(ZipOutputStream zipOutputStream) throws Exception {
		for (int i = 0; i < filesAndFolders.size(); i++) {
			IResource resource = (IResource)filesAndFolders.get(i);
			int index = processFolder.getProjectRelativePath().toString().length() + 1;
			addFile(zipOutputStream, resource.getProjectRelativePath().toString().substring(index));
		}
	}

	private void showSuccessDialog() {
		MessageDialog dialog = new MessageDialog(shell, "Deployment Successful", null,
				"The process archive deployed successfully.",
				SWT.ICON_INFORMATION, new String[] { "OK" }, 0);
		dialog.open();
	}

	private void showErrorDialog(Throwable t) {
		ErrorDialog dialog = new ErrorDialog(shell,
				"Unexpected Deployment Exception",
				"An exception happened during the deployment of the process",
				new Status(
						Status.ERROR,
						Plugin.getDefault().getBundle()
								.getSymbolicName(),
						Status.ERROR,
						"An unexpected exception caused the deployment to fail",
						t), Status.ERROR);
		dialog.open();
	}

	private void addClassesAndResources(ZipOutputStream zos, ClassLoader loader)
			throws CoreException, IOException {
		for (int i = 0; i < classesAndResources.size(); i++) {
			addClassOrResource(zos, loader, (String)classesAndResources.get(i));
		}
	}
	
	private void addClassOrResource(ZipOutputStream zos, ClassLoader loader, String classOrResource) throws IOException {
		byte[] buff = new byte[256];
		zos.putNextEntry(new ZipEntry("classes/" + classOrResource));
		InputStream is = loader.getResourceAsStream(classOrResource);
		int read;
		while ((read = is.read(buff)) != -1) {
			zos.write(buff, 0, read);
		}
		is.close();
		if (classOrResource.endsWith(".class")) {
			final String className = 
				classOrResource.substring(classOrResource.lastIndexOf('/') + 1, classOrResource.length() - 6) + '$';
			URL url = loader.getResource(classOrResource);
			File file = new File(url.getFile());
			File folder = new File(file.getParent());
			String nestedClasses[] = folder.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith(className);
				}
			});
			if (nestedClasses != null) {
				for (int i = 0; i < nestedClasses.length; i++) {
					String fileName = classOrResource.substring(0, classOrResource.lastIndexOf("/") + 1) + nestedClasses[i];
					zos.putNextEntry(new ZipEntry("classes/" + fileName));
					is = loader.getResourceAsStream(fileName);
					while ((read = is.read(buff)) != -1) {
						zos.write(buff, 0, read);
					}
					is.close();
				}
			}
		}
	}

	private void addFile(ZipOutputStream zos, String fileName)
			throws CoreException, IOException {
		byte[] buff = new byte[256];
		IFile file = processFolder.getFile(fileName);
		if (!file.exists()) return;
		InputStream is = file.getContents();
		zos.putNextEntry(new ZipEntry(fileName));
		int read;
		while ((read = is.read(buff)) != -1) {
			zos.write(buff, 0, read);
		}
	}

	private URL[] getProjectClasspathUrls() throws CoreException, MalformedURLException {
		IProject project = processFolder.getProject();
		IJavaProject javaProject = JavaCore.create(project);
		String[] pathArray = JavaRuntime
				.computeDefaultRuntimeClassPath(javaProject);
		URL[] urls = new URL[pathArray.length];
		for (int i = 0; i < pathArray.length; i++) {
			urls[i] = new File(pathArray[i]).toURI().toURL();
		}
		return urls;
	}
	
}
