/*************************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.runtime.handlers;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.tools.jbpm.preferences.JbpmInstallation;
import org.jboss.tools.jbpm.preferences.PreferencesManager;
import org.jboss.tools.runtime.core.model.AbstractRuntimeDetectorDelegate;
import org.jboss.tools.runtime.core.model.IRuntimeDetectorDelegate;
import org.jboss.tools.runtime.core.model.RuntimeDefinition;

public class JbpmHandler extends AbstractRuntimeDetectorDelegate {
	
	private static final String JBPM3 = "jBPM3"; //$NON-NLS-1$
	private static final String JBPM4 = "jBPM4"; //$NON-NLS-1$
	private static final String JBPM = "JBPM"; //$NON-NLS-1$
	private static final String SOA_P = "SOA-P"; //$NON-NLS-1$
	private static final String SOA_P_STD = "SOA-P-STD"; //$NON-NLS-1$
	
	public static File getJbpmRoot(RuntimeDefinition runtimeDefinition) {
		String type = runtimeDefinition.getType();
		if (SOA_P.equals(type) || SOA_P_STD.equals(type)) {
			return new File(runtimeDefinition.getLocation(),"jbpm-jpdl"); //$NON-NLS-1$
		}
		if (JBPM.equals(type)) {
			return runtimeDefinition.getLocation();
		}
		return null;
	}
	
	@Override
	public void initializeRuntimes(List<RuntimeDefinition> runtimeDefinitions) {
		for (RuntimeDefinition runtimeDefinition : runtimeDefinitions) {
			if (runtimeDefinition.isEnabled() && !jbpmExists(runtimeDefinition)) {
				File jbpmRoot = getJbpmRoot(runtimeDefinition);
				if (jbpmRoot == null || !jbpmRoot.isDirectory()) {
					continue;
				}
				String type = runtimeDefinition.getType();
				if (JBPM.equals(type)) {
					PreferencesManager.getInstance().addJbpmInstallation(runtimeDefinition.getName(), jbpmRoot.getAbsolutePath(), runtimeDefinition.getVersion());
				}
			}
			initializeRuntimes(runtimeDefinition.getIncludedRuntimeDefinitions());
		}
		
	}
	
	/**
	 * @param serverDefinition
	 * @return
	 */
	public static boolean jbpmExists(RuntimeDefinition runtimeDefinition) {
		File jbpmRoot = getJbpmRoot(runtimeDefinition);
		if (jbpmRoot == null || !jbpmRoot.isDirectory()) {
			return false;
		}
		Map<String, JbpmInstallation> jbpmMap = PreferencesManager.getInstance().getJbpmInstallationMap();
		Collection<JbpmInstallation> jbpmInstalations = jbpmMap.values();
		for (JbpmInstallation jbpm:jbpmInstalations) {
			String location = jbpm.location;
			if (location != null && location.equals(jbpmRoot.getAbsolutePath())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public RuntimeDefinition getRuntimeDefinition(File root,
			IProgressMonitor monitor) {
		if (monitor.isCanceled() || root == null) {
			return null;
		}
		boolean isJBPM = isValidJbpmInstallation(root.getAbsolutePath());
		if (isJBPM) {
			String version = "unknown";
			if (isJbpm3(root.getAbsolutePath())) {
				version = JBPM3;
			} else if (isJbpm4(root.getAbsolutePath())) {
				version = JBPM4;
			}
			String name = root.getName();
			int index = 1;
			boolean nameExists = PreferencesManager.getInstance().getJbpmInstallation(name) != null;
			while (nameExists) {
				name = root.getName() + " " + index++;
				nameExists = PreferencesManager.getInstance().getJbpmInstallation(name) != null;
			}
			return new RuntimeDefinition(name, version, JBPM, root.getAbsoluteFile());
		}
		return null;
	}
	
	private static boolean isJbpm3(String location) {
		return new Path(location).append("/src/resources/gpd/version.info.xml").toFile().exists();
	}
	
	private static boolean isJbpm4(String location) {
		return new Path(location).append("/jbpm.jar").toFile().exists();
	}
	
	private boolean isValidJbpmInstallation(String location) {
		return isJbpm3(location) || isJbpm4(location);
	}
	
	@Override
	public boolean exists(RuntimeDefinition runtimeDefinition) {
		if (runtimeDefinition == null || runtimeDefinition.getLocation() == null) {
			return false;
		}
		return jbpmExists(runtimeDefinition);
	}

	@Override
	public void computeIncludedRuntimeDefinition(
			RuntimeDefinition runtimeDefinition) {
		if (runtimeDefinition == null || !SOA_P.equals(runtimeDefinition.getType())) {
			return;
		}
		File jbpmRoot = new File(runtimeDefinition.getLocation(),"jbpm-jpdl"); //$NON-NLS-1$
		if (jbpmRoot.isDirectory()) {
			String version = JBPM3;
			if (isJbpm4(runtimeDefinition.getLocation().getAbsolutePath())) {
				version = JBPM4;
			}
			RuntimeDefinition sd = new RuntimeDefinition(runtimeDefinition.getName(), version, JBPM, jbpmRoot);
			sd.setParent(runtimeDefinition);
			runtimeDefinition.getIncludedRuntimeDefinitions().add(sd);
		}
	}

	@Override
	public String getVersion(RuntimeDefinition runtimeDefinition) {
		return runtimeDefinition.getVersion();
	}
}
