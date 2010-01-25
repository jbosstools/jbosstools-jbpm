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
package org.jbpm.gd.jpdl.util;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.tools.jbpm.preferences.JbpmInstallation;
import org.jbpm.gd.jpdl.Constants;
import org.jbpm.gd.jpdl.Logger;

public class JbpmClasspathContainer implements IClasspathContainer, Constants {

	IClasspathEntry[] jbpmLibraryEntries;
	JbpmInstallation jbpmInstallation;

	IJavaProject javaProject = null;

	public JbpmClasspathContainer(IJavaProject javaProject, JbpmInstallation jbpmInstallation) {
		this.javaProject = javaProject;
		this.jbpmInstallation = jbpmInstallation;

	}

	public IClasspathEntry[] getClasspathEntries() {
		if (jbpmLibraryEntries == null) {
			jbpmLibraryEntries = createJbpmLibraryEntries(javaProject);
		}
		return jbpmLibraryEntries;
	}

	public String getDescription() {
		return "jBPM Library [" + jbpmInstallation.name + "]";
	}
		
	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	public IPath getPath() {
		return new Path("JBPM/" + jbpmInstallation.name);
	}

	private IClasspathEntry[] createJbpmLibraryEntries(IJavaProject project) {
		Map jarNames = getJarNames();
		ArrayList entries = new ArrayList();
		Iterator iterator = jarNames.keySet().iterator();
		while (iterator.hasNext()) {
			IPath jarPath = (IPath)iterator.next();
			IPath srcPath = (IPath)jarNames.get(jarPath);
			IPath srcRoot = null;
			entries.add(JavaCore.newLibraryEntry(
					jarPath, 
					srcPath, 
					srcRoot));
		}
		return (IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries
				.size()]);
	}

	private Map getJarNames() {
		HashMap result = new HashMap();
		try {
			String location = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(jbpmInstallation.location);
			IPath locationPath = new Path(location);
			Document document = new SAXReader().read(locationPath.append("src/resources/gpd/version.info.xml").toFile());
			XPath xpath = document.createXPath("/jbpm-version-info/classpathentry");
			List list = xpath.selectNodes(document);
			for (int i = 0; i < list.size(); i++) {
				Element entry = (Element)list.get(i);
				IPath sourcePath = null;
				if (entry.attribute("src") != null) {
					sourcePath = locationPath.append((String)entry.attribute("src").getData());
				}
				result.put(
						locationPath.append((String)entry.attribute("path").getData()),
						sourcePath);
			}
		} 
		catch (MalformedURLException e) { }
		catch (DocumentException e) { } 	
		catch (CoreException e) {
			Logger.logError("Problem while resolving expression", e);
		}
		return result;
	}
	
}
