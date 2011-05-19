/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, JBoss Inc., and individual contributors as indicated
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
package org.jbpm.gd.jpdl.prefs;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jbpm.gd.jpdl.Constants;
import org.jbpm.gd.jpdl.Plugin;

/**
 * @since 3.4
 */
public class CompatibilityPage extends PreferencePage implements IWorkbenchPreferencePage, Constants {
	
	private Button transCondWarningButton;
  
	protected Control createContents(Composite parent) {
		Composite clientArea = createClientArea(parent);
		createIncludeXsdButton(clientArea);
		return null;
	}
	
	private void createIncludeXsdButton(Composite parent) {
		transCondWarningButton = new Button(parent, SWT.CHECK);
		transCondWarningButton.setText("Suppress warning on invalid transitions with conditions." );
		transCondWarningButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		transCondWarningButton.setSelection(getPreferenceStore().getBoolean(Plugin.TRANS_COND_WARN_PREFERENCE));
	}
	
	private Composite createClientArea(Composite parent) {
		Composite clientArea = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		clientArea.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		clientArea.setLayoutData(gridData);
		return clientArea;
	}

	public void init(IWorkbench workbench) {
	  setPreferenceStore(Plugin.getDefault().getPreferenceStore());
	}
	
	public void performDefaults() {
		getPreferenceStore().setToDefault(Plugin.TRANS_COND_WARN_PREFERENCE);
	}

	public boolean performOk() {
		getPreferenceStore().setValue(Plugin.TRANS_COND_WARN_PREFERENCE, transCondWarningButton.getSelection());
		return true;
	}
	
}
