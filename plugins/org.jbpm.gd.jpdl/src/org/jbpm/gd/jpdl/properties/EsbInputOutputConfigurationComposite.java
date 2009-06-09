package org.jbpm.gd.jpdl.properties;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.jbpm.util.AutoResizeTableLayout;
import org.jbpm.gd.common.model.GenericElement;
import org.jbpm.gd.jpdl.model.EsbElement;

public class EsbInputOutputConfigurationComposite implements SelectionListener, FocusListener, MouseListener {
	
	public static EsbInputOutputConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent, int type) {
		if (type != 0 && type != 1) throw new RuntimeException("The type should be 0 or 1");
		EsbInputOutputConfigurationComposite result = new EsbInputOutputConfigurationComposite();
		result.type = type;
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	public static int  INPUT_CONFIGURATION = 0;
	public static int OUTPUT_CONFIGURATION = 1;
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	private int type;
	
	private Table table;
	private TableEditor editor;
	private Text text;
	private Button addButton;
	private Button removeButton;
	int selectedColumn = -1;
	
	private EsbElement esbElement;
	
	private EsbInputOutputConfigurationComposite() {}
	
	public void setEsbElement(EsbElement esbElement) {
		if (this.esbElement == esbElement) return;
		unhookSelectionListener();
		clearControls();
		this.esbElement = esbElement;
		if (esbElement != null) {
			updateControls();
			hookSelectionListener();
		}
	}
	
	private void hookSelectionListener() {
		addButton.addSelectionListener(this);
		removeButton.addSelectionListener(this);
		table.addSelectionListener(this);
		table.addMouseListener(this);
	}
	
	private void unhookSelectionListener() {
		addButton.removeSelectionListener(this);
		removeButton.removeSelectionListener(this);
		table.removeSelectionListener(this);
		table.removeMouseListener(this);
	}
	
	private void clearControls() {
		addButton.setEnabled(true);
		removeButton.setEnabled(false);
		table.removeAll();
	}
	
	private void updateControls() {
		GenericElement[] elements = getMappings();
		for (int i = 0; i < elements.length; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData(elements[i]);
			item.setText(jbpmColumnIndex(), getAttribute("bpm", elements[i]));
			item.setText(esbColumnIndex(), getAttribute("esb", elements[i]));
		}
	}
	
	private String getAttribute(String name, GenericElement element) {
		String result = (String)element.getGenericAttibutes().get(name);
		return result == null ? "" : result;
		
	}
	
	private void create() {
		table = widgetFactory.createTable(parent, SWT.FULL_SELECTION | SWT.V_SCROLL);
		addButton = widgetFactory.createButton(parent, "Add", SWT.PUSH);
		removeButton = widgetFactory.createButton(parent, "Remove", SWT.PUSH);
		table.setLayoutData(createTableLayoutData());
		addButton.setLayoutData(createAddButtonLayoutData());
		removeButton.setLayoutData(createRemoveButtonLayoutData());
		initTable();
		createEditor();
	}
	
	private void createEditor() {
		editor = new TableEditor(table);
		text = new Text(table, SWT.NORMAL);
		text.setVisible(false);
		text.setText("");
		editor.minimumWidth = text.getSize().x;
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
	}
	
	private void initTable() {
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		AutoResizeTableLayout handlerConfigBeanTableLayout = new AutoResizeTableLayout(table);
		handlerConfigBeanTableLayout.addColumnData(new ColumnWeightData(50));
		handlerConfigBeanTableLayout.addColumnData(new ColumnWeightData(50));
		table.setLayout(handlerConfigBeanTableLayout);
		TableColumn jbpmNameColumn = new TableColumn(table, SWT.NONE);
		jbpmNameColumn.setText(columnHeaders()[0]);
		TableColumn esbNameColumn = new TableColumn(table, SWT.NONE);
		esbNameColumn.setText(columnHeaders()[1]);
	}
	
	private FormData createAddButtonLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.left = new FormAttachment(removeButton, 0);
		result.left.alignment = SWT.LEFT;
		return result;
	}
	
	private FormData createRemoveButtonLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(addButton, 0);
		result.right = new FormAttachment(100, 0);
		return result;
	}
	
	private FormData createTableLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(0, 0);
		result.bottom = new FormAttachment(100, 0);
		result.right = new FormAttachment(removeButton, 0);
		return result;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == addButton) {
			handleAddButtonSelected();
		} else if (e.widget == removeButton) {
			handleRemoveButtonSelected();
		} else if (e.widget == table) {
			handleTableRowSelected();
		}
	}
	
	private void handleAddButtonSelected() {
		GenericElement element = (GenericElement)esbElement.getFactory().createById("org.jbpm.gd.jpdl.genericElement");
		element.setName("mapping");
		addElement(element);
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(jbpmColumnIndex(), getNextName("bpm", jbpmColumnIndex()));
		item.setText(esbColumnIndex(), getNextName("esb", esbColumnIndex()));
		element.addGenericAttribute("bpm", item.getText(jbpmColumnIndex()));
		element.addGenericAttribute("esb", item.getText(esbColumnIndex()));
		item.setData(element);
		table.setSelection(item);
	}
	
	private String getNextName(String kind, int pos) {
		int runner = 1;
		while (true) {
			boolean goodCandidate = true;
			String candidate = kind + "Name" + runner;
			TableItem[] items = table.getItems();
			for (int i = 0; i < items.length; i++) {
				if (candidate.equals(items[i].getText(pos))) {
					goodCandidate = false;
				}
			}
			if (goodCandidate) return candidate;		
			runner++;
		}
	}
	
	private void handleRemoveButtonSelected() {
		int i = table.getSelectionIndex();
		if (i == -1) return;
		TableItem item = table.getItem(i);
		removeElement((GenericElement)item.getData());
		table.remove(i);
		removeButton.setEnabled(table.getItemCount() == 0);
	}
	
	private void handleTableRowSelected() {
		removeButton.setEnabled(table.getSelection() != null);
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == text) {
			applyValue();
			endEdit();
		}
	}

	private void applyValue() {
		int i = table.getSelectionIndex();
		if (i == -1) return;
		TableItem item = table.getItem(i);
		item.setText(selectedColumn, text.getText());
		GenericElement element = (GenericElement)item.getData();
		if (element == null) return;
		element.addGenericAttribute("bpm", item.getText(jbpmColumnIndex()));
		element.addGenericAttribute("esb", item.getText(esbColumnIndex()));
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
		selectedColumn = getSelectedColumn(e.x, e.y);
		if (selectedColumn == -1) return;
		doEdit();
	}

	private void doEdit() {
		if (text.isVisible()) endEdit();
		if (table.getSelectionIndex() == -1 || selectedColumn == -1) return;
		TableItem selection = table.getItem(table.getSelectionIndex());
		String value = selection.getText(selectedColumn);
		text.setText(value == null ? "" : value);
		editor.setEditor(text, selection, selectedColumn);
		text.setVisible(true);
		text.selectAll();
		text.setFocus();
		text.addFocusListener(this);
	}
	
	private void endEdit() {
		text.setVisible(false);
		text.setText("");
		text.removeFocusListener(this);
	}
	
	private int getSelectedColumn(int x, int y) {
        int columnToEdit = -1;
        int columns = table.getColumnCount();
        TableItem tableItem = getSelectedTableItem();
        if (tableItem == null) return -1;
        for (int i = 0; i < columns; i++) {
            Rectangle bounds = tableItem.getBounds(i);
            if (bounds.contains(x, y)) {
                columnToEdit = i;
                break;
            }
        }	
        return columnToEdit;
	}
	
	private TableItem getSelectedTableItem() {
		TableItem[] selection = table.getSelection();
		if (selection.length > 0) {
			return selection[0];
		} else {
			return null;
		}
	}
	
	public void mouseUp(MouseEvent e) {
	}
	
	private interface InputOutputConfigurationStrategy {
		int jbpmColumnIndex();
		int esbColumnIndex();
		String[] columnHeaders();
		void addElement(GenericElement element);
		void removeElement(GenericElement element);
		GenericElement[] getMappings();
	}
	
	private InputOutputConfigurationStrategy[]configurationStrategies = new InputOutputConfigurationStrategy[] {
		new InputOutputConfigurationStrategy() {
			public void addElement(GenericElement element) {
				esbElement.addJbpmToEsbMapping(element);
			}
			public String[] columnHeaders() {
				return new String[] {"jBPM Name", "ESB Name"};
			}
			public int esbColumnIndex() {
				return 1;
			}
			public int jbpmColumnIndex() {
				return 0;
			}
			public void removeElement(GenericElement element) {
				esbElement.removeJbpmToEsbMapping(element);
			}
			public GenericElement[] getMappings() {
				return esbElement.getJbpmToEsbMappings();
			}
		},
		new InputOutputConfigurationStrategy() {
			public void addElement(GenericElement element) {
				esbElement.addEsbToJbpmMapping(element);
			}
			public String[] columnHeaders() {
				return new String[] {"ESB Name", "jBPM Name"};
			}
			public int esbColumnIndex() {
				return 0;
			}
			public int jbpmColumnIndex() {
				return 1;
			}
			public void removeElement(GenericElement element) {
				esbElement.removeEsbToJbpmMapping(element);
			}
			public GenericElement[] getMappings() {
				return esbElement.getEsbToJbpmMappings();
			}
		}
	};

	private int jbpmColumnIndex() {
		return configurationStrategies[type].jbpmColumnIndex();
	}
	
	private int esbColumnIndex() {
		return configurationStrategies[type].esbColumnIndex();
	}
	
	private String[] columnHeaders() {
		return configurationStrategies[type].columnHeaders();
	}
	
	private void addElement(GenericElement element) {
		configurationStrategies[type].addElement(element);
	}
	
	private void removeElement(GenericElement element) {
		configurationStrategies[type].removeElement(element);	
	}
	
	private GenericElement[] getMappings() {
		return configurationStrategies[type].getMappings();
	}
}
