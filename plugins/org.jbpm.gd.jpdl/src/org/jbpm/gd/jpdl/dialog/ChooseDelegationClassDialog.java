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
package org.jbpm.gd.jpdl.dialog;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.jbpm.gd.jpdl.util.ProjectFinder;

public class ChooseDelegationClassDialog {
	
	private Shell parent;
	private String typeName;
	private String message;
	private String title;
	
	public ChooseDelegationClassDialog(Shell parent, String typeName, String title, String message) {
		this.parent = parent;
		this.typeName = typeName;
		this.title = title;
		this.message = message;
	}
	
	public String openDialog() {
		Object result = null;
		try {
			SelectionDialog dialog= JavaUI.createTypeDialog(
					parent, 
					new ProgressMonitorDialog(parent),					
					SearchEngine.createHierarchyScope(
							ProjectFinder.getCurrentProject().findType(
								typeName)),
					IJavaElementSearchConstants.CONSIDER_ALL_TYPES, 
					false);
			dialog.setTitle(title);
			dialog.setMessage(message);
			if (dialog.open() != IDialogConstants.CANCEL_ID) {
				Object[] types= dialog.getResult();
				if (types != null && types.length != 0) {
				    result = types[0];
				}
			}
		}
		catch (JavaModelException e) {
			// ignore this and return null;
		}
		return result == null ? null : ((IType)result).getFullyQualifiedName();
		
	}

}
