package org.jbpm.gd.jpdl.deployment;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.jbpm.gd.common.util.Base64Converter;
import org.jbpm.gd.jpdl.editor.JpdlEditor;

public class ProcessArchiveDeployer {
	
	private static final String BOUNDARY = "AaB03x";

	private JpdlEditor jpdlEditor;
	
	public ProcessArchiveDeployer(JpdlEditor jpdlEditor) {
		this.jpdlEditor = jpdlEditor;
	}
	
	public boolean deploy(byte[] processArchive) {
		URL url = createConnectionUrl();
		if (url == null) return false;
		URLConnection connection = openConnection(url);
		if (connection == null) return false;
		prepareConnection(connection);
		if (!sendData(connection, processArchive)) return false;
		String response = receiveData(connection);
		if (response == null) {
			return false;
		}
		System.out.println(response);
		return true;
	}
	
	public boolean pingServer() {
		URL url = createConnectionUrl();
		if (url == null) return false;
		URLConnection connection = openConnection(url);
		if (connection == null) return false;
		connection.setDoOutput(true);
		String response = receiveData(connection);
		if (response == null) {
			return false;
		}
		System.out.println(response);
		return true;
	}
	
	private String constructUrlString() {
		String result = "http://";
		String serverName = jpdlEditor.getDeploymentInfo().getServerName();
		if (serverName != null) {
			result += serverName;
		}
		result += ":";
		String serverPort = jpdlEditor.getDeploymentInfo().getServerPort();
		if (serverPort != null) {
			result += serverPort;
		}
		String serverDeployer = jpdlEditor.getDeploymentInfo().getServerDeployer();
		if (serverDeployer != null) {
			if (!serverDeployer.startsWith("/")) {
				result += "/";
			}
			result += serverDeployer;
		}
		return result;
	}
	
	private URLConnection openConnection(URL url) {
		try {
			return url.openConnection();
		} catch (IOException e) {
			showConnectException();
			return null;
		}
	}
	
	private URL createConnectionUrl() {
		try {
			return new URL(constructUrlString());
		} catch (MalformedURLException e) {
			showConstructUrlException();
			return null;
		}
	}
	
	private void prepareConnection(URLConnection connection) {
		if (jpdlEditor.getDeploymentInfo().getUseCredentials()) {
			addCredentials(connection);
		}
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
	}
	
	private void addCredentials(URLConnection connection) {
		String userPassword = jpdlEditor.getDeploymentInfo().getUserName();
		userPassword += ":";
		userPassword += jpdlEditor.getDeploymentInfo().getPassword();
		String encoding = Base64Converter.encode(userPassword);
		connection.setRequestProperty("Authorization", "Basic " + encoding);
	}
	
	private boolean sendData(URLConnection connection, byte[] processArchive) {
		try {
			OutputStream outputStream = connection.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
			dataOutputStream.writeBytes("--" + BOUNDARY + "\r\n");
			dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"definition\"; filename=\"dummy.par\"\r\n");
			dataOutputStream.writeBytes("Content-Type: application/x-zip-compressed\r\n\r\n");
			dataOutputStream.write(processArchive);
			dataOutputStream.writeBytes("\r\n--" + BOUNDARY + "--\r\n");
			dataOutputStream.flush();
			dataOutputStream.close();
			return true;
		} catch (ConnectException e) {
			showConnectException();
			return false;
		} catch (IOException e) {
			showSendDataException();
			return false;
		}
	}
	
	private String receiveData(URLConnection connection) {
		try {
			InputStream inputStream = connection.getInputStream();
			StringBuffer result = new StringBuffer();
			int read;
			while ((read = inputStream.read()) != -1) {
				result.append((char)read);
			}
			return result.toString();
		} catch (ConnectException e) {
			showConnectException();
			return null;
		} catch (FileNotFoundException e) {
			showFileNotFoundException();
			return null;
		} catch (IOException e) {
			showReceiveDataException();
			return null;
		}
		
	}
	
	private void showConstructUrlException() {
		MessageDialog dialog = new MessageDialog(
				jpdlEditor.getSite().getShell(), 
				"Invalid Connection URL", 
				null,
				"The constructed URL is invalid. Check your connection parameters.",
				SWT.ICON_ERROR, 
				new String[] { "OK" }, 
				0);
		dialog.open();
	}

	private void showConnectException() {
		MessageDialog dialog = new MessageDialog(
				jpdlEditor.getSite().getShell(), 
				"Connection Failed", 
				null,
				"A connection to the server could not be established. " +
				"Check your connection parameters and verify that the server is running.",
				SWT.ICON_ERROR, 
				new String[] { "OK" }, 
				0);
		dialog.open();
	}

	private void showFileNotFoundException() {
		MessageDialog dialog = new MessageDialog(
				jpdlEditor.getSite().getShell(), 
				"Connection Failed", 
				null,
				"The server deployer application could not be reached. " +
				"Check your connection parameters and verify that the server is running.",
				SWT.ICON_ERROR, 
				new String[] { "OK" }, 
				0);
		dialog.open();
	}

	private void showSendDataException() {
		MessageDialog dialog = new MessageDialog(
				jpdlEditor.getSite().getShell(), 
				"Deployment Failed", 
				null,
				"Unexpected exception while sending the deployment archive to the server.",
				SWT.ICON_ERROR, 
				new String[] { "OK" }, 
				0);
		dialog.open();
	}

	private void showReceiveDataException() {
		MessageDialog dialog = new MessageDialog(
				jpdlEditor.getSite().getShell(), 
				"Deployment Failed", 
				null,
				"Unexpected exception while receiving the response from the server.",
				SWT.ICON_ERROR, 
				new String[] { "OK" }, 
				0);
		dialog.open();
	}

}
