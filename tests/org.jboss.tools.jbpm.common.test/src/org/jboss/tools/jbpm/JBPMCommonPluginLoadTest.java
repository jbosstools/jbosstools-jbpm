package org.jboss.tools.jbpm;

import java.text.MessageFormat;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class JBPMCommonPluginLoadTest extends TestCase {
	public void testOrgJbossToolsJbpmCommonPluginIsLoaded() {
		assertPluginResolved("org.jboss.tools.jbpm.common");
	}
	
	public void assertPluginsResolved(String[] ids) {
		for (String id : ids) {
			Bundle bundle = Platform.getBundle(id);
			assertNotNull(MessageFormat.format("Could not get bundle {0} instance",id), bundle);
			assertTrue(MessageFormat.format("Plugin '{0}' is not resolved",bundle.getSymbolicName()), //$NON-NLS-1$ //$NON-NLS-2$
			isPluginResolved(bundle.getSymbolicName()));
			System.out.println(MessageFormat.format("{0} was resolved and activated",bundle.getSymbolicName()));
		}
	}
	
	public void assertPluginResolved(String id) {
		assertPluginsResolved(new String[] {id});
	}
	
	public boolean isPluginResolved(String pluginId) {
		Bundle bundle = Platform.getBundle(pluginId);
		assertNotNull(pluginId + " failed to load.", bundle); //$NON-NLS-1$
		try {
			// this line is needed to to force plug-in loading and to change it state to ACTIVE 
			bundle.loadClass("fake class"); //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			// It happens always because loaded class doesn't not exist
		}
		return ((bundle.getState() & Bundle.RESOLVED) > 0)
				|| ((bundle.getState() & Bundle.ACTIVE) > 0);
	}
}
