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
package org.jboss.tools.jbpm.preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.jboss.tools.jbpm.Constants;

public class PreferencesManager {
	
	private static Map<Plugin, PreferencesManager> managerMap = new HashMap<Plugin, PreferencesManager>();

	private Map<String, JbpmInstallation> jbpmInstallations = null;
	private File installationsFile = null;
	private Plugin plugin;
	
	public static PreferencesManager getPreferencesManager(Plugin key) {
		PreferencesManager preferencesManager = managerMap.get(key);
		if (preferencesManager == null) {
			preferencesManager = new PreferencesManager(key);
			managerMap.put(key, preferencesManager);
		}
		return preferencesManager;
	}
		
	private PreferencesManager(Plugin plugin) {
		this.plugin = plugin;
		initializeInstallations();
	}
	
	private File getInstallationFile() {
		if (installationsFile == null) {
			initializeInstallations();
		}
		return installationsFile;
	}
	
	private Map<String, JbpmInstallation> getJbpmInstallations() {
		if (jbpmInstallations == null) {
			jbpmInstallations = new HashMap<String, JbpmInstallation>();
		}
		return jbpmInstallations;
	}
		
	private Preferences getPreferences() {
		return plugin.getPluginPreferences();
	}
	
	private void initializeInstallations() {
		installationsFile = 
			plugin.getStateLocation().append("jbpm-installations.xml").toFile();
		if (!installationsFile.exists()) {
			createInstallationsFile();
		} else {
			loadInstallations();
		}
	}

	private void createInstallationsFile() {
		try {
			installationsFile.createNewFile();
			saveInstallations();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadInstallations() {
		Reader reader = null;
		try {
			reader = new FileReader(getInstallationFile());
			XMLMemento memento = XMLMemento.createReadRoot(reader);
			IMemento[] children = memento.getChildren("installation");
			for (int i = 0; i < children.length; i++) {
				JbpmInstallation installation = new JbpmInstallation();
				installation.name = children[i].getString("name");
				installation.location = children[i].getString("location");
				installation.version = children[i].getString("version");				
				getJbpmInstallations().put(installation.name, installation);
			}
		} catch (WorkbenchException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveInstallations() {
		XMLMemento memento = XMLMemento.createWriteRoot("installations");
		Iterator<String> iterator = getJbpmInstallations().keySet().iterator();
		while (iterator.hasNext()) {
			String name = (String)iterator.next();
			JbpmInstallation installation = (JbpmInstallation)getJbpmInstallations().get(name);
			IMemento child = memento.createChild("installation");
			child.putString("name", installation.name);
			child.putString("location", installation.location);
			child.putString("version", installation.version);
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(getInstallationFile());
			memento.save(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Map<String, JbpmInstallation> getJbpmInstallationMap() {
		return getJbpmInstallations();
	}
	
	public JbpmInstallation getJbpmInstallation(String name) {
		return (JbpmInstallation)getJbpmInstallations().get(name);
	}
	
	public String getPreferredJbpmName() {
		return getPreferences().getString(Constants.JBPM_NAME);
	}
	
	public void setPreferredJbpmName(String name) {
		getPreferences().setDefault(Constants.JBPM_NAME, name);
	}
	
	public void addJbpmInstallation(String name, String location, String version) {
		JbpmInstallation installation = new JbpmInstallation();
		installation.name = name;
		installation.location = location;
		installation.version = version;
		getJbpmInstallationMap().put(name, installation);
	}
	
	public void initializeDefaultJbpmInstallation(String name, String location, String version) {
		setPreferredJbpmName(name);
		addJbpmInstallation(name, location, version);
		saveInstallations();
	}
	
}
