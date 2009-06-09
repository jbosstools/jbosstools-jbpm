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
package org.jbpm.gd.pf.wizard;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

public class NewPageFlowWizard extends Wizard implements INewWizard {

	private IStructuredSelection selection;
	private NewPageFlowWizardPage page;
	
	public NewPageFlowWizard() {
		setWindowTitle("New Page Flow");
	}

	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		this.selection= currentSelection;
	}
	
	public void addPages() {
		page = new NewPageFlowWizardPage();
		addPage(page);
		page.init(selection);
	}
	
	public boolean performFinish() {
		try {
			IContainer folder = page.getProcessFolder();
			IFile pageFlowFile = page.getPageFlowFile();
			pageFlowFile.create(createInitialPageFlow(), true, null);
			IFile gpdFile = folder.getFile(new Path(".gpd." + pageFlowFile.getName()));
			gpdFile.create(createInitialGpdInfo(), true, null);
			IDE.openEditor(getActivePage(), pageFlowFile);
			BasicNewResourceWizard.selectAndReveal(pageFlowFile, getActiveWorkbenchWindow());
			return true;
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
	}

	private IWorkbenchPage getActivePage() {
		return getActiveWorkbenchWindow().getActivePage();
	}

	private IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}
	
	private ByteArrayInputStream createInitialPageFlow() throws JavaModelException {
		String name = page.getPageFlowFile().getName();
		int index = name.lastIndexOf(".xml");
		name = name.substring(0, index);
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("\n");
		buffer.append("\n");
		buffer.append(
				"<pageflow-definition\n" +
				"  name=\"" + name + "\">\n" +	
				"</pageflow-definition>");	
		return new ByteArrayInputStream(buffer.toString().getBytes());
	}

	private ByteArrayInputStream createInitialGpdInfo() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("\n");
		buffer.append("\n");
		buffer.append("<root-container />");	
		return new ByteArrayInputStream(buffer.toString().getBytes());
	}
	
}
