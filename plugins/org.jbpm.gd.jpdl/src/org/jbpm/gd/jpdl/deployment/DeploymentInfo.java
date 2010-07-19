package org.jbpm.gd.jpdl.deployment;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jbpm.gd.jpdl.Plugin;
import org.jbpm.gd.jpdl.prefs.PreferencesConstants;

public class DeploymentInfo implements PreferencesConstants {
	
	private IFile processInfoFile = null;
	private IFile graphicalInfoFile = null;
	private IFile imageFile = null;
	private String serverName = null;
	private String serverPort = null;
	private String serverDeployer = null;
	private ArrayList<Object> classesAndResources = new ArrayList<Object>();
	private ArrayList<Object> additionalFiles = new ArrayList<Object>();
	private Boolean useCredentials = null;
	private String userName = null;
	private String password = null;

	public void setProcessInfoFile(IFile processInfoFile) {
		this.processInfoFile = processInfoFile;
	}
	
	public IFile getProcessInfoFile() {
		return processInfoFile;
	}

	public void setGraphicalInfoFile(IFile graphicalInfoFile) {
		this.graphicalInfoFile = graphicalInfoFile;
	}
	
	public IFile getGraphicalInfoFile() {
		return graphicalInfoFile;
	}

	public void setImageFile(IFile imageFile) {
		this.imageFile = imageFile;
	}
	
	public IFile getImageFile() {
		return imageFile;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public String getServerName() {
		if (serverName == null) {
			serverName = getPrefs().getString(SERVER_NAME);
		}
		return serverName;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	
	public String getServerPort() {
		if (serverPort == null) {
			serverPort = getPrefs().getString(SERVER_PORT);
		}
		return serverPort;
	}

	public void setServerDeployer(String serverDeployer) {
		this.serverDeployer = serverDeployer;
	}
	
	public String getServerDeployer() {
		if (serverDeployer == null) {
			serverDeployer = getPrefs().getString(SERVER_DEPLOYER);
		}
		return serverDeployer;
	}

	public void addToClassesAndResources(Object element) {
		classesAndResources.add(element);
	}
	
	public void removeFromClassesAndResources(Object element) {
		classesAndResources.remove(element);
	}
	
	public Object[] getClassesAndResources() {
		return classesAndResources.toArray(new Object[classesAndResources.size()]);
	}

	public void addToAdditionalFiles(Object element) {
		additionalFiles.add(element);
	}
	
	public void removeFromAdditionalFiles(Object element) {
		additionalFiles.remove(element);
	}
	
	public Object[] getAdditionalFiles() {
		return additionalFiles.toArray(new Object[additionalFiles.size()]);
	}
	
	public boolean getUseCredentials() {
		if (useCredentials == null) {
			useCredentials = getPrefs().getBoolean(USE_CREDENTIALS);
			if (useCredentials == null) {
				useCredentials = false;
			}
		}
		return useCredentials;
	}
	
	public void setUseCredentials(boolean useCredentials) {
		this.useCredentials = useCredentials;
	}
	
	public String getUserName() {
		if (userName == null) {
			userName = getPrefs().getString(USER_NAME);
		}
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		if (password == null) {
			password = getPrefs().getString(PASSWORD);
		}
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isEmpty() {
		return 
			graphicalInfoFile == null && 
			imageFile == null &&
			isServerNameDefault() &&
			isServerPortDefault() &&
			isServerDeployerDefault() &&
			classesAndResources.isEmpty() &&
			additionalFiles.isEmpty();
	}
	
	private boolean isServerNameDefault() {
		return serverName == null || serverName.equals(getPrefs().getString(SERVER_NAME));
	}
	
	private boolean isServerPortDefault() {
		return serverPort == null || serverPort.equals(getPrefs().getString(SERVER_PORT));
	}
	
	private boolean isServerDeployerDefault() {
		return serverDeployer == null || serverDeployer.equals(getPrefs().getString(SERVER_DEPLOYER));
	}
	
	private IPreferenceStore getPrefs() {
		return Plugin.getDefault().getPreferenceStore();
	}

}
