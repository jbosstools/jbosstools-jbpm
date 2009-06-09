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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.FileEditorInput;
import org.jbpm.gd.jpdl.Logger;
import org.jbpm.gd.jpdl.Plugin;
import org.jbpm.gd.jpdl.model.Variable;
import org.jbpm.gd.jpdl.model.VariableContainer;
import org.jbpm.gd.jpdl.taskform.Field;
import org.jbpm.gd.jpdl.taskform.FieldType;
import org.jbpm.gd.jpdl.taskform.FormGenerator;

public class TaskFormGenerationDialog extends StatusDialog {
	
	private static final String pluginId = Plugin.getDefault().getBundle().getSymbolicName();
	
	private static final IStatus okStatus = new Status(
			Status.INFO, pluginId, 0, "Press OK to generate a form in the specified filename.", null);
	private static final IStatus noFileNameStatus = new Status(
			Status.ERROR, pluginId, 0, "Please specify a correct filename.", null);
	
	String taskName;
	FormToolkit toolkit;
	VariableContainerConfigurationComposite fieldComposite;
	VariableContainer fieldContainer;
	VariableContainerConfigurationComposite buttonComposite;
	VariableContainer buttonContainer;
	
	Label fileNameLabel;
	Text fileNameText;
	Composite fieldArea;
	Composite buttonArea;
	
	
	public TaskFormGenerationDialog(Shell parentShell, String taskName) {
		super(parentShell);
		toolkit = new FormToolkit(parentShell.getDisplay());
		fieldContainer = new VariableContainerImpl();
		buttonContainer = new VariableContainerImpl();
		this.taskName = taskName;
	}
	
	public boolean close() {
		toolkit.dispose();
		return super.close();
	}
	
	protected Point getInitialSize() {
		return new Point(550, 475);
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite)super.createDialogArea(parent);
		area.setLayout(new FormLayout());
		fieldArea = toolkit.createComposite(area);
		fieldArea.setBackground(ColorConstants.menuBackground);
		fieldArea.setLayout(new FormLayout());
		buttonArea = toolkit.createComposite(area);
		buttonArea.setBackground(ColorConstants.menuBackground);
		buttonArea.setLayout(new FormLayout());
		Composite formFileArea = toolkit.createComposite(area);
		formFileArea.setBackground(ColorConstants.menuBackground);
		formFileArea.setLayout(new FormLayout());
		fieldArea.setLayoutData(createFieldAreaLayoutData());
		buttonArea.setLayoutData(createButtonAreaLayoutData());
		formFileArea.setLayoutData(createFormFileAreaLayoutData());
		createFieldComposite(fieldArea);
		createButtonComposite(buttonArea);
		createFormFileField(formFileArea);
		getShell().setText("Generate Task Form");
		return area;
	}
	
	protected Button createButton(Composite parent, int id, String label,
			boolean defaultButton) {
		return super.createButton(parent, id, label, false);
	}
	
	
	private FormData createFieldAreaLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 10);
		result.left = new FormAttachment(0, 10);
		result.right = new FormAttachment(100, -10);
		result.height = 175;
		return result;
	}
	
	private FormData createButtonAreaLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(fieldArea, 0);
		result.left = new FormAttachment(0, 10);
		result.right = new FormAttachment(100, -10);
		result.height = 125;
		return result;
	}
	
	private FormData createFormFileAreaLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(buttonArea, 0);
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
	private void createFieldComposite(Composite area) {
		String[] columnTitles = new String[] {"Variable Name", "Label", "Read", "Write", "Required"};
		fieldComposite = VariableContainerConfigurationComposite.create(toolkit, area, false, columnTitles);
		fieldComposite.setVariableContainer(fieldContainer);
		fieldComposite.setBackground(ColorConstants.menuBackground);
		fieldComposite.setMessage("Define the form fields:");
		fieldComposite.setVariableDefaultName("field");
	}
	
	private void createButtonComposite(Composite area) {
		String[] columnTitles = new String[] {"Transition Name", "Label"};
		buttonComposite = VariableContainerConfigurationComposite.create(toolkit, area, true, columnTitles);
		buttonComposite.setVariableContainer(buttonContainer);
		buttonComposite.setBackground(ColorConstants.menuBackground);
		buttonComposite.setMessage("Define the form buttons:");
		buttonComposite.setVariableDefaultName("button");
	}
	
	private void createFormFileField(Composite area) {
		fileNameLabel = toolkit.createLabel(area, "File name:");
		fileNameLabel.setBackground(ColorConstants.menuBackground);
		createFileText(area);
		fileNameLabel.setLayoutData(createFileNameLabelLayoutData());
		fileNameText.setLayoutData(createFileNameTextLayoutData());
		
	}
	
	private FormData createFileNameLabelLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 15);
		result.top = new FormAttachment(0, 12);
		return result;
	}
	
	private FormData createFileNameTextLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 10);
		result.left = new FormAttachment(fileNameLabel, 5);
		result.right = new FormAttachment(100, -77);
		return result;
	}
	
	private void createFileText(Composite area) {
		fileNameText = toolkit.createText(area, "");
		if (taskName == null || "".equals(taskName)) {
			taskName = "default";
		}
		int i = taskName.length();
		fileNameText.setText(taskName + ".xhtml");
		fileNameText.setSelection(0, i);
		fileNameText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				checkFileName();
			}			
		});
		checkFileName();
	}

	private void checkFileName() {
		String fileName = fileNameText.getText();
		if (fileName == null || "".equals(fileName)) {
			updateStatus(noFileNameStatus);
		} else {
			updateStatus(okStatus);
		}
	}
	
	protected void okPressed() {
		generateForm(); 
		super.okPressed();
	}
	
	private void generateForm() {
		createTaskFormFile();
		updateFormsXmlFile();
		refreshProcessFolder();
	}
	
	private void refreshProcessFolder() {
		try {
			getInputFile().getParent().refreshLocal(1, null);			
		} catch (CoreException e) {
			Logger.logError("Problem while refreshing process folder.", e);
		}
	}
	
	private boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
		
	private void createTaskFormFile() {
		Variable[] fieldVars = fieldContainer.getVariables();
		List fields = new ArrayList();
		for (int i = 0; i < fieldVars.length; i++) {
			Field field = new Field();
			field.setVariableName(fieldVars[i].getName());
			field.setLabel(isEmpty(fieldVars[i].getMappedName()) ? fieldVars[i].getName() : fieldVars[i].getMappedName());
			field.setReadOnly(!fieldVars[i].isWritable() && fieldVars[i].isReadable());
			field.setFieldType(FieldType.getFieldTypes()[0]);
			fields.add(field);
		}
		Variable[] buttonVariables = buttonContainer.getVariables();
		List buttons = new ArrayList();
	    buttons.add(org.jbpm.gd.jpdl.taskform.Button.BUTTON_SAVE);
	    buttons.add(org.jbpm.gd.jpdl.taskform.Button.BUTTON_CANCEL);
		for (int i = 0; i < buttonVariables.length; i++) {
			String name = "name";
			String label;
			if (!isEmpty(buttonVariables[i].getName())) {
				name = buttonVariables[i].getName();
			}
			if (!isEmpty(buttonVariables[i].getMappedName())) {
				label = buttonVariables[i].getMappedName();
			} else {
				label = name;
			}
			buttons.add(org.jbpm.gd.jpdl.taskform.Button.createTransitionButton(name, label));
		}
		getFile(fileNameText.getText(), FormGenerator.getForm(fields, buttons));
	}
	
	private void updateFormsXmlFile() {
		IFile file = getInputFile();
		IPath path = file.getProjectRelativePath();
		file = file.getProject().getFile(path.removeLastSegments(1).append("forms.xml"));
		try {
			if (!file.exists()) {
				file.create(new ByteArrayInputStream("<forms/>".getBytes()), true, null);
			}
		} catch (CoreException e) {
			Logger.logError("Could not create forms.xml", e);
		}
		Document document = getDocument(file);
		addForm(document);
		saveDocument(file, document);		
	}
	
	private void saveDocument(IFile formFile, Document document) {
		try {
			StringWriter stringWriter = new StringWriter();
			XMLWriter xmlWriter = new XMLWriter(stringWriter, OutputFormat.createPrettyPrint());
			xmlWriter.write(document);
			formFile.setContents(new ByteArrayInputStream(stringWriter.getBuffer().toString().getBytes()), true, true, null);
		} catch (IOException e) {
			Logger.logError("Problem writing xml document to file", e);						
		} catch (CoreException e) {
			Logger.logError("Problem writing xml document to file", e);						
		}
	}
	
	private Element getElementFor(String taskName, Document document) {
		List list = document.getRootElement().elements("form");
		for (int i = 0; i < list.size(); i++) {
			Element element = (Element)list.get(i);
			String candidate = element.attributeValue("task");
			if (candidate != null && candidate.equals(taskName)) {
				return element;
			}
		}
		return null;
	}
	
	private void addForm(Document document) {
		Element element = getElementFor(taskName, document);
		if (element == null) {
			element = document.getRootElement().addElement("form");
			element.addAttribute("task", taskName);
		}
		Attribute form = element.attribute("form"); 
		if (form == null) {
			element.addAttribute("form", fileNameText.getText());
		} else {
			form.setValue(fileNameText.getText());
		}
	}
	
	private Document getDocument(IFile file) {
		try {
			return new SAXReader().read(new InputStreamReader(file.getContents()));
		} catch (DocumentException e) {
			Logger.logError("Problem creating DOM document from forms.xml", e);			
		} catch (CoreException e) {
			Logger.logError("Problem getting the contents from forms.xml", e);			
		}
		return null;
		
	}
	
	private IFile getInputFile() {
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		return ((FileEditorInput)editorPart.getEditorInput()).getFile();
	}
	
	private IFile getFile(String name, String initialContents) {
		IFile file = getInputFile();
		IPath path = file.getProjectRelativePath();
		file = file.getProject().getFile(path.removeLastSegments(1).append(name));
		try {
			if (!file.exists()) {
					file.create(new ByteArrayInputStream(initialContents.getBytes()), true, null);
			} else {
				file.setContents(new ByteArrayInputStream(initialContents.getBytes()), true, true, null);
			}
		} catch (CoreException e) {
			Logger.logError("Could not create " + name, e);
		}
		return file;
	}
	
	private class VariableContainerImpl implements VariableContainer {
		ArrayList variables = new ArrayList();
		public void addVariable(Variable variable) {
			variables.add(variable);
		}
		public Variable[] getVariables() {
			return (Variable[])variables.toArray(new Variable[variables.size()]);
		}
		public void removeVariable(Variable variable) {
			variables.remove(variable);
		}
		
	}
	
}
