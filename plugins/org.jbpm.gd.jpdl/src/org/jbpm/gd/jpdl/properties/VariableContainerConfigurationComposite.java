package org.jbpm.gd.jpdl.properties;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.jbpm.util.AutoResizeTableLayout;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.model.SemanticElementFactory;
import org.jbpm.gd.jpdl.model.Variable;
import org.jbpm.gd.jpdl.model.VariableContainer;
import org.jbpm.gd.jpdl.util.VariableTableCellClickedResolver;
import org.jbpm.gd.jpdl.util.VariableTableItemWrapper;

public class VariableContainerConfigurationComposite implements SelectionListener {
	
	public static VariableContainerConfigurationComposite create(FormToolkit widgetFactory, Composite parent, boolean nameOnly, String[] columnTitles) {
		VariableContainerConfigurationComposite result = new VariableContainerConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.nameOnlyVisible = nameOnly;
		result.columnTitles = columnTitles;
		result.create();
		return result;
	}
	
	public static VariableContainerConfigurationComposite create(FormToolkit widgetFactory, Composite parent) {
		return create(widgetFactory, parent, false, new String[] {"Name", "Mapped Name", "Read", "Write", "Required"});
	}
	
		
	private FormToolkit widgetFactory;
	private Composite parent;
	
	private Label messageLabel; 
	private Table variablesTable;
	private Button addButton;
	private Button removeButton;
    private VariableContainer variableContainer;
    private VariableTableCellClickedResolver resolver;
    private String variableDefaultName = "variable";
    private boolean nameOnlyVisible = false;
    private String[] columnTitles = null;
    
	private VariableContainerConfigurationComposite() {}
	
	public void setVariableContainer(VariableContainer variableContainer) {
		if (this.variableContainer == variableContainer) return;
		unhookListeners();
		this.variableContainer = variableContainer;
		clearControls();
		if (variableContainer != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		addButton.addSelectionListener(this);
		removeButton.addSelectionListener(this);
		variablesTable.addSelectionListener(this);
		variablesTable.addMouseListener(resolver);
	}
	
	private void unhookListeners() {
		addButton.removeSelectionListener(this);
		removeButton.removeSelectionListener(this);
		variablesTable.removeSelectionListener(this);
		variablesTable.removeMouseListener(resolver);
	}
	
	private void clearControls() {
		variablesTable.removeAll();
		addButton.setEnabled(false);
		removeButton.setEnabled(false);
	}
	
	private void updateControls() {
		addButton.setEnabled(true);
		refreshTable();
	}
	
	private void create() {
		messageLabel = widgetFactory.createLabel(parent, "Define the used variables :");
		variablesTable = widgetFactory.createTable(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		addButton = widgetFactory.createButton(parent, "Add", SWT.PUSH);
		removeButton = widgetFactory.createButton(parent, "Remove", SWT.PUSH);
		resolver = new VariableTableCellClickedResolver(variablesTable);
		removeButton.setEnabled(false);
		messageLabel.setLayoutData(createMessageLabelLayoutData());
		variablesTable.setLayoutData(createVariablesTableLayoutData());
		addButton.setLayoutData(createAddButtonLayoutData());
		removeButton.setLayoutData(createRemoveButtonLayoutData());
		initializeVariablesTable();
	}
	
	private void initializeVariablesTable() {
		variablesTable.setHeaderVisible(true);
		variablesTable.setLinesVisible(true);
		variablesTable.setLayout(new AutoResizeTableLayout(variablesTable));
		initializeTableColumns();
	}

	private void initializeTableColumns() {
		TableLayout layout = (TableLayout)variablesTable.getLayout();
		TableColumn dummyColumn = new TableColumn(variablesTable, SWT.CENTER);
		dummyColumn.setText("");
		ColumnWeightData dummyColumnData = new ColumnWeightData(0, 0);
		layout.addColumnData(dummyColumnData);
		TableColumn nameColumn = new TableColumn(variablesTable, SWT.LEFT);
		nameColumn.setText(columnTitles[0]);
		ColumnWeightData nameColumnData = new ColumnWeightData(20, 100);
		layout.addColumnData(nameColumnData);
		TableColumn mappedNameColumn = new TableColumn(variablesTable, SWT.LEFT);
		mappedNameColumn.setText(columnTitles[1]);
		ColumnWeightData mappedNameColumnData = new ColumnWeightData(20, 100);
		layout.addColumnData(mappedNameColumnData);
		if (nameOnlyVisible) return;
		TableColumn readColumn = new TableColumn(variablesTable, SWT.CENTER);
		readColumn.setText(columnTitles[2]);
		ColumnWeightData readColumnData = new ColumnWeightData(10, 60);
		layout.addColumnData(readColumnData);
		TableColumn writeColumn = new TableColumn(variablesTable, SWT.CENTER);
		writeColumn.setText(columnTitles[3]);
		ColumnWeightData writeColumnData = new ColumnWeightData(10, 60);
		layout.addColumnData(writeColumnData);
		TableColumn requiredColumn = new TableColumn(variablesTable, SWT.CENTER);
		requiredColumn.setText(columnTitles[4]);
		ColumnWeightData requiredColumnData = new ColumnWeightData(15, 75);
		layout.addColumnData(requiredColumnData);
	}
	
	private FormData createMessageLabelLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 5);
		result.left = new FormAttachment(0, 5);
		result.right = new FormAttachment(100, -5);
		return result;
	}
	
	private FormData createVariablesTableLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(messageLabel, 5);
		result.left = new FormAttachment(0, 5);
		result.bottom = new FormAttachment(100, -5);
		result.right = new FormAttachment(removeButton, -5);
		return result;
	}
	
	private FormData createAddButtonLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(variablesTable, 0);
		result.top.alignment = SWT.TOP;
		result.left = new FormAttachment(removeButton, 0);
		result.left.alignment = SWT.LEFT;
		result.right = new FormAttachment(100, -5);
		return result;
	}
	
	private FormData createRemoveButtonLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(addButton, 5);
		result.right = new FormAttachment(100, -5);
		return result;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.widget == addButton) {
			handleAddButtonSelected();
		} else if (e.widget == removeButton) {
			handleRemoveButtonSelected();
		} else if (e.widget == variablesTable) {
			handleVariablesTableSelected(e);
		}
	}
	
	private Variable createVariable(SemanticElementFactory delegateFactory) {
		if (delegateFactory == null) {
			return new Variable();
		} else {
			return (Variable)delegateFactory.createById("org.jbpm.gd.jpdl.variable");
		}
	}
	
	private void handleAddButtonSelected() {
		SemanticElementFactory factory = null;
		if (variableContainer instanceof SemanticElement) {
			factory = ((SemanticElement)variableContainer).getFactory();
		}
		Variable variable = createVariable(factory);
		variable.setName(getNextAvailableVariableName());
		variable.setAccess("read,write");
		variableContainer.addVariable(variable);
		VariableTableItemWrapper wrapper = new VariableTableItemWrapper(variablesTable, variable);
		variablesTable.setSelection(wrapper.getTableItem());
		wrapper.editCell(1);
		removeButton.setEnabled(variablesTable.getSelectionIndex() != -1);
	}
	
	private String getNextAvailableVariableName() {
		String result = variableDefaultName;
		int runner = 1;
		while (true) {
			if (!hasTableVariableWithName(result + runner)) {
				return result + runner;
			}
			runner++;
		}
	}
	
	private boolean hasTableVariableWithName(String name) {
		boolean result = false;
		int max = variablesTable.getItemCount();
		for (int i = 0; i < max; i++) {
			VariableTableItemWrapper wrapper = (VariableTableItemWrapper)variablesTable.getItem(i).getData();
			if (name.equals(wrapper.getVariable().getName())) {
				result = true;
			}
		}
		return result;
	}

	private void handleRemoveButtonSelected() {
		TableItem item = variablesTable.getItem(variablesTable.getSelectionIndex());
		VariableTableItemWrapper wrapper = (VariableTableItemWrapper)item.getData();
		variableContainer.removeVariable(wrapper.getVariable());
		refreshTable();
		removeButton.setEnabled(variablesTable.getSelectionIndex() != -1);
	}
	
	private void refreshTable() {
		variablesTable.removeAll();
		Variable[] variables = variableContainer.getVariables();
		for (int i = 0; i < variables.length; i++) {
			new VariableTableItemWrapper(variablesTable, variables[i]);
		}
	}
	
	private void handleVariablesTableSelected(SelectionEvent e) {
		removeButton.setEnabled(variablesTable.getSelectionIndex() != -1);
	}
	
	public VariableContainer getVariableContainer() {
		return variableContainer;
	}
	
	void setBackground(Color color) {
		messageLabel.setBackground(color);
	}
	
	void setMessage(String message) {
		messageLabel.setText(message);
	}
	
	void setVariableDefaultName(String name) {
		this.variableDefaultName = name;
	}
	
}
