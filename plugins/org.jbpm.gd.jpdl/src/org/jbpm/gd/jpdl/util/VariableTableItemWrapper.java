package org.jbpm.gd.jpdl.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jbpm.gd.jpdl.model.Variable;

public class VariableTableItemWrapper implements DisposeListener, SelectionListener, FocusListener {
	
	private TableItem tableItem;
	private Variable variable;
	private Button readButton, writeButton, requiredButton;
	private Text nameText, mappedNameText;
	
	public VariableTableItemWrapper(Table table, Variable variable) {
		this.variable = variable;
		initializeTableItem(table);
		initializeNameText(table);
		initializeReadButton(table);
		initializeWriteButton(table);
		initializeRequiredButton(table);
		initializeMappedNameText(table);
	}
	
	private void initializeNameText(Table table) {
		nameText = createText(table, 1);
		nameText.addSelectionListener(this);
		nameText.addFocusListener(this);
		nameText.setText(getName());
	}
	
	private void initializeMappedNameText(Table table) {
		mappedNameText = createText(table, 2);
		mappedNameText.addSelectionListener(this);
		mappedNameText.addFocusListener(this);
		mappedNameText.setText(getMappedName());
	}
	
	private void initializeReadButton(Table table) {
		readButton = createButton(table, 3);
		readButton.addSelectionListener(this);
		readButton.setSelection(variable.isReadable());
	}
	
	private void initializeWriteButton(Table table) {
		writeButton = createButton(table, 4);
		writeButton.addSelectionListener(this);
		writeButton.setSelection(variable.isWritable());
	}
	
	private void initializeRequiredButton(Table table) {
		requiredButton = createButton(table, 5);
		requiredButton.addSelectionListener(this);
		requiredButton.setSelection(variable.isRequired());
	}

	private Button createButton(Table table, int index) {
		TableEditor checkboxEditor = new TableEditor(table);
		Button result = new Button(table, SWT.CHECK);
		result.pack();
		checkboxEditor.minimumWidth = result.getSize ().x;
		checkboxEditor.horizontalAlignment = SWT.CENTER;
		checkboxEditor.setEditor(result, tableItem, index);
		return result;
	}
	
	private Text createText(Table table, int index) {
		TableEditor textEditor = new TableEditor(table);
		Text result = new Text(table, SWT.NORMAL);
		result.pack();
		textEditor.minimumWidth = result.getSize ().x;
		textEditor.horizontalAlignment = SWT.LEFT;
		textEditor.grabHorizontal = true;
		textEditor.setEditor(result, tableItem, index);
		result.setVisible(false);
		return result;
	}
	
	private void initializeTableItem(Table table) {
		tableItem = new TableItem(table, SWT.NONE);
		tableItem.addDisposeListener(this);
		tableItem.setData(this);
		tableItem.setText(1, getName());
		tableItem.setText(2, getMappedName());
	}

	public void widgetDisposed(DisposeEvent e) {
		tableItem.removeDisposeListener(this);
		nameText.removeSelectionListener(this);
		nameText.removeFocusListener(this);
		mappedNameText.removeSelectionListener(this);
		mappedNameText.removeFocusListener(this);
		readButton.removeSelectionListener(this);
		writeButton.removeSelectionListener(this);
		requiredButton.removeSelectionListener(this);
		nameText.dispose();
		mappedNameText.dispose();
		readButton.dispose();
		writeButton.dispose();
		requiredButton.dispose();
	}

	private String calculateAccessString() {
		StringBuffer stringBuffer = new StringBuffer();
		if (readButton.getSelection()) {
			stringBuffer.append("read,");
		} 
		if (writeButton.getSelection()) {
			stringBuffer.append("write,");
		}
		if (requiredButton.getSelection()) {
			stringBuffer.append("required ");
		}
		return stringBuffer.substring(0, stringBuffer.length() - 1).toString();
	}
	
	public void editCell(int column) {
		if (column == 1) {
			editName();
		} else if (column == 2) {
			editMappedName();
		}
	}

	private void editMappedName() {
		nameText.setVisible(false);
		mappedNameText.setVisible(true);
		mappedNameText.setText(getMappedName());
		mappedNameText.selectAll();
		mappedNameText.setFocus();
	}

	private void editName() {
		nameText.setVisible(true);
		nameText.setText(getName());
		nameText.setFocus();
		nameText.selectAll();
	}
	
	public void cancelEditing() {
		applyName();
		applyMappedName();
	}

	private void applyMappedName() {
		mappedNameText.setVisible(false);
		variable.setMappedName(mappedNameText.getText());
		tableItem.setText(2, getMappedName());
	}

	private String getMappedName() {
		return variable.getMappedName() == null ? "" : variable.getMappedName();
	}

	private void applyName() {
		nameText.setVisible(false);
		variable.setName(nameText.getText());
		tableItem.setText(1, getName());
	}

	private String getName() {
		return variable.getName() == null ? "" : variable.getName();
	}
	
	public TableItem getTableItem() {
		return tableItem;
	}

	public Variable getVariable() {
		return variable;
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == readButton || e.widget == writeButton || e.widget == requiredButton) {
			variable.setAccess(calculateAccessString());
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		if (e.widget == nameText) {
			variable.setName(nameText.getText());
			applyName();
		} else if (e.widget == mappedNameText) {
			variable.setMappedName(mappedNameText.getText());
			applyMappedName();
		}		
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		cancelEditing();
	}
	
}

