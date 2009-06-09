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
package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jbpm.gd.jpdl.Constants;
import org.jbpm.gd.jpdl.model.Task;

public class ExpressionConfigurationComposite extends Composite implements Constants {
	
	Text expressionText;
	
	public ExpressionConfigurationComposite(Composite parent, Task task) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));
		createComposite(task);
		createSeparator();
		updateControl();
	}
	
	private void updateControl() {
		getParent().layout();
	}
	
	private void createComposite(Task task) {
//		String expression = task.getAssignmentExpression();
//		Label expressionLabel = new Label(this, SWT.NORMAL);
//		expressionLabel.setText("Enter the assignment expression: ");
//		expressionText = new Text(this, SWT.BORDER);
//		expressionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		if (expression != null) expressionText.setText(expression);
	}
	
	private void createSeparator() {
		Label separator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData separatorData = new GridData(GridData.FILL_HORIZONTAL);
		separatorData.horizontalSpan = 2;
		separatorData.heightHint = 10;
		separator.setLayoutData(separatorData);
	}
	
	public String getExpression() {
		return expressionText.getText();
	}
		
}
