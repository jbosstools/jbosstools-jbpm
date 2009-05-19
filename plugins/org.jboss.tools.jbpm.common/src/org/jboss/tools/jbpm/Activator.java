package org.jboss.tools.jbpm;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class Activator extends AbstractUIPlugin {
	
	private static Activator activator;
	
	public static Activator getDefault() {
		return activator;
	}
	
	public Activator() {
		super();
		activator = this;
	}
	
}
