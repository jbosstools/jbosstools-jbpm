package org.jbpm.gd.jpdl.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jboss.tools.jbpm.util.AutoResizeTableLayout;
import org.jbpm.gd.common.model.GenericElement;
import org.jbpm.gd.jpdl.Logger;
import org.jbpm.gd.jpdl.dialog.ChooseDelegationClassDialog;
import org.jbpm.gd.jpdl.model.Delegation;
import org.jbpm.gd.jpdl.util.ProjectFinder;

public class DelegationConfigurationComposite implements KeyListener, SelectionListener, FocusListener {
	
	public static DelegationConfigurationComposite create(
			TabbedPropertySheetWidgetFactory widgetFactory, Composite parent, ChooseDelegationClassDialog dialog) {
		DelegationConfigurationComposite result = new DelegationConfigurationComposite();
		result.chooseDelegationClassDialog = dialog;
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	private ChooseDelegationClassDialog chooseDelegationClassDialog;
	
    private Delegation delegation;
    
    private Label nameLabel;
    private Text nameText;
    private Button searchButton;
    private Label configTypeLabel;
    private CCombo configTypeCombo;
    
    private HashMap configAreaPages = new HashMap();
    
    private LabelComposite messageLabel;
    private TextComposite constructorTextComposite;
    private TextComposite compatibilityTextComposite;
    private TableComposite fieldTableComposite;
    private TableComposite beanTableComposite;
    
	private DelegationConfigurationComposite() {}
	
	public void setDelegation(Delegation delegation) {
		if (this.delegation == delegation) return;
		unhookListeners();
		this.delegation = delegation;
		clearControls();
		if (delegation != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		nameText.addKeyListener(this);
		searchButton.addSelectionListener(this);
		configTypeCombo.addSelectionListener(this);
		constructorTextComposite.text.addFocusListener(this);
		compatibilityTextComposite.text.addFocusListener(this);
	}
	
	private void unhookListeners() {
		nameText.removeKeyListener(this);
		searchButton.removeSelectionListener(this);
		configTypeCombo.removeSelectionListener(this);
		constructorTextComposite.text.removeFocusListener(this);
		compatibilityTextComposite.text.removeFocusListener(this);
	}
	
	private void clearControls() {
		nameText.setText("");
		configTypeCombo.setText("Field");
		showPage("Message");
		messageLabel.setText("");
		fieldTableComposite.table.removeAll();
		beanTableComposite.table.removeAll();
		constructorTextComposite.text.setText("");
		compatibilityTextComposite.text.setText("");
	}
	
	private void showPage(String key) {
		Iterator iter = configAreaPages.keySet().iterator();
		while (iter.hasNext()) {
			String candidate = (String)iter.next();
			((DelegationConfigurationWidget)configAreaPages.get(candidate)).setVisible(candidate.equals(key));
		}
	}
	
	private void updateControls() {
		nameText.setText(getDelegationClassName());
		configTypeCombo.setText(fromConfigType(getDelegationConfigType()));
		updatePageBook();
	}

	private void updatePageBook() {
		IType type = getClassFor(nameText.getText());
		updateFieldTableComposite(type);
		updateBeanTableComposite(type);
		updateConstructorTextComposite();
		updateCompatibilityTextComposite();
		updateVisiblePage(type != null);
	}
	
	private void updateFieldTableComposite(IType type) {
		if (type == null) return;
		List list = getFields(type);
		for (int i = 0; i < list.size(); i++) {
			TableItem item = new TableItem(fieldTableComposite.table, SWT.NONE);
			item.setText(0, (String)list.get(i));
		}
		if ("field".equals(getDelegationConfigType())) {
			updateTableItems(fieldTableComposite.table.getItems());
		}
	}

	private void updateTableItems(TableItem[] items) {
		GenericElement[] elements = delegation.getGenericElements();
		for (int i = 0; i < elements.length; i++) {
			for (int j = 0; j < items.length; j++) {
				String name = elements[i].getName() == null ? "" : elements[i].getName();
				String value = elements[i].getValue() == null ? "" : elements[i].getValue();
				if (name.equals(items[j].getText(0))) {
					items[j].setChecked(true);
					items[j].setText(1, value);
					items[j].setData(elements[i]);
					break;
				}
			}
		}
	}
	
	private void updateBeanTableComposite(IType type) {
		if (type == null) return;
		List list = getSetters(type);
		for (int i = 0; i < list.size(); i++) {
			TableItem item = new TableItem(beanTableComposite.table, SWT.NONE);
			item.setText(0, (String)list.get(i));
		}
		if ("bean".equals(getDelegationConfigType())) {
			updateTableItems(beanTableComposite.table.getItems());
		}
	}
	
	private void updateConstructorTextComposite() {
	    boolean valid = "constructor".equals(getDelegationConfigType());
		constructorTextComposite.text.setText(valid ? getDelegationConfigString() : "");
	}
	
	private void updateCompatibilityTextComposite() {
	    boolean valid = "configuration-property".equals(getDelegationConfigType());
		constructorTextComposite.text.setText(valid ? getDelegationConfigString() : "");
	}
	
	private void updateVisiblePage(boolean validClass) {
		if (!validClass) {
			showInvalidTypeMessage();
		} else {
			handleValidType();
		}
	}

	private String getDelegationConfigType() {
		return delegation.getConfigType() == null ? "field" : delegation.getConfigType();
	}

	private String getDelegationClassName() {
		return delegation.getClassName() == null ? "" : delegation.getClassName();
	}
	
	private String getDelegationConfigString() {
		return delegation.getConfigInfo() == null ? "" : delegation.getConfigInfo();
	}
	
	private void create() {
		nameLabel = widgetFactory.createLabel(parent, "Class Name");
		nameText = widgetFactory.createText(parent, "");
		searchButton = widgetFactory.createButton(parent, "Search...", SWT.PUSH);
		configTypeLabel = widgetFactory.createLabel(parent, "Config Type");
		configTypeCombo = widgetFactory.createCCombo(parent);
		configTypeCombo.setItems(getConfigurationTypes());
		configTypeCombo.setEditable(false);
		createPages(parent);
		nameLabel.setLayoutData(createNameLabelLayoutData());
		nameText.setLayoutData(createNameTextLayoutData());
		searchButton.setLayoutData(createSearchButtonLayoutData());
		configTypeLabel.setLayoutData(createConfigTypeLabelLayoutData());
		configTypeCombo.setLayoutData(createConfigTypeComboLayoutData());
	}
	
	private void createPages(Composite composite) {
		messageLabel = new LabelComposite();
		messageLabel.create(composite);
		configAreaPages.put("Message", messageLabel);
		fieldTableComposite = new TableComposite();
		fieldTableComposite.create(composite);
		configAreaPages.put("Field", fieldTableComposite);
		beanTableComposite = new TableComposite();
		beanTableComposite.create(composite);
		configAreaPages.put("Bean", beanTableComposite);
		constructorTextComposite = new TextComposite();
		constructorTextComposite.create(composite);
		configAreaPages.put("Constructor", constructorTextComposite);
		compatibilityTextComposite = new TextComposite();
		compatibilityTextComposite.create(composite);
		configAreaPages.put("Compatibility", compatibilityTextComposite);
	}
	
	private String[] getConfigurationTypes() {
		return new String[] { "Field", "Bean", "Constructor", "Compatibility" };
	}
	
	private FormData createNameLabelLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.top = new FormAttachment(0, 2);
		return result;
	}

	private FormData createNameTextLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(nameLabel, 0);
		result.right = new FormAttachment(searchButton, 0);
		result.top = new FormAttachment(0, 0);
		return result;
	}
	

	private FormData createSearchButtonLayoutData() {
		FormData result = new FormData();
		result.right = new FormAttachment(configTypeLabel, 0);
		result.top = new FormAttachment(0, -3);
		return result;
	}

	private FormData createConfigTypeLabelLayoutData() {
		FormData result = new FormData();
		result.right = new FormAttachment(configTypeCombo, 0);
		result.top = new FormAttachment(0, 2);
		return result;
	}

	private FormData createConfigTypeComboLayoutData() {
		FormData result = new FormData();
		result.right = new FormAttachment(100, 0);
		result.top = new FormAttachment(0, -2);
		return result;
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		if (e.widget == nameText) {
			handleNameTextChange();
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	private void handleNameTextChange() {
		String newName = nameText.getText();
		if (newName.equals(delegation.getClassName())) return;
		delegation.setClassName(newName);
		if (delegation.getConfigInfo() != null) {
			delegation.setConfigInfo(null);
		}
		GenericElement[] genericElements = delegation.getGenericElements();
		fieldTableComposite.table.removeAll();
		beanTableComposite.table.removeAll();
		for (int i = 0; i < genericElements.length; i++) {
			delegation.removeGenericElement(genericElements[i]);
		}
		updatePageBook();
	}
	
	private void handleValidType() {
		String configType = delegation.getConfigType();
		if ("field".equals(configType)) {
			handleFieldConfigType();
		} else if ("bean".equals(configType)) {
			handleBeanConfigType();
		} else if ("constructor".equals(configType)) {
			handleConstructorConfigType();
		} else if ("configuration-property".equals(configType)) {
			handleCompatibilityConfigType();
		}
	}
	
	private void handleFieldConfigType() {
		if (fieldTableComposite.table.getItemCount() == 0) {
			messageLabel.setText("The class does not have any fields");
			showPage("Message");
		} else {
			showPage("Field");
			restoreConfigElements(fieldTableComposite.table.getItems());
		}
	}

	private void restoreConfigElements(TableItem[] items) {
		if (delegation.getGenericElements().length == 0) {
			for (int i = 0; i < items.length; i++) {
				if (items[i].getChecked()) {
					addGenericElement(items[i]);
				}
			}
		}
	}
	
	private List getFields(IType type) {
		List result = new ArrayList();
		try {
			List types = getTypes(type);
			for (int i = 0; i < types.size(); i++) {
				IType subType = (IType)types.get(i);
				IField[] fields = subType.getFields();
				for (int j = 0; j < fields.length; j++) {
					if (!Flags.isStatic(fields[j].getFlags())) {
						String fieldName = fields[j].getElementName();
						if (!result.contains(fieldName)) {
							result.add(fieldName);
						}
					}
				}
			}
		} catch (JavaModelException  e) {
			Logger.logError("Error while getting the fields for type " + type + ".", e);
		}
		return result;
	}
	
	private IType getSupertype(IType targetType) throws JavaModelException {
		if (targetType == null) return null;
		String name = targetType.getSuperclassName();
		if (name == null) return null;
		IType result = getClassFor(name);
		if (result != null) return result;
		ICompilationUnit compilationUnit = targetType.getCompilationUnit();
		if (compilationUnit == null) return null;
		IPackageDeclaration[] packageDeclarations = compilationUnit.getPackageDeclarations();
		if (packageDeclarations != null && packageDeclarations.length > 0) {
			String qualifiedName = packageDeclarations[0].getElementName() + "." + name;
			result = getClassFor(qualifiedName);
			if (result != null) return result;
		}
		IImportDeclaration[] importDeclarations = compilationUnit.getImports();
		if (importDeclarations == null) return null;
		for (int i = 0; i < importDeclarations.length; i++) {
			String declaration = importDeclarations[i].getElementName();
			if (declaration.endsWith(name)) {
				result = getClassFor(declaration);
				if (result != null) return result;
			} else if (declaration.endsWith(".*")) {
				String qualifiedName = declaration.substring(0, declaration.length() - 1) + name;
				result = getClassFor(qualifiedName);
				if (result != null) return result;
			}
		}
		return null;
	}
	
	
	private List getTypes(IType targetType) {
		List types = new ArrayList();
		IType type = targetType;
		while (type != null && !"java.lang.Object".equals(type.getFullyQualifiedName())) {
			try {
				types.add(type);
				type = getSupertype(type);
			}
			catch (JavaModelException e) {
				Logger.logError("Error while looking up the supertypes of " + targetType.getFullyQualifiedName() + ".", e);
			}
		}
		return types;
	}

	private void handleBeanConfigType() {
		if (beanTableComposite.table.getItemCount() == 0) {
			messageLabel.setText("The class does not have any setters");
			showPage("Message");
		} else {
			showPage("Bean");
			restoreConfigElements(beanTableComposite.table.getItems());
		}
	}
	
	private List getSetters(IType type) {
		List result = new ArrayList();
		try {
			List types = getTypes(type);
			for (int i = 0; i < types.size(); i++) {
				IType subType = (IType)types.get(i);
				IMethod[] methods = subType.getMethods();
				for (int j = 0; j < methods.length; j++) {
					if (methods[j].getElementName().startsWith("set")) {
						StringBuffer buff = new StringBuffer(methods[j].getElementName().substring(3));
						buff.setCharAt(0, Character.toLowerCase(buff.charAt(0)));
						String methodName = buff.toString();
						if (!result.contains(methodName)) {
							result.add(methodName);
						}
					}
				}
			}
		} catch (JavaModelException  e) {
			Logger.logError("Error while getting the setters for type " + type + ".", e);
		}
		return result;
	}

	private void handleConstructorConfigType() {
		showPage("Constructor");
		if (delegation.getConfigInfo() == null) {
			delegation.setConfigInfo(constructorTextComposite.text.getText());
		} else {
			constructorTextComposite.text.setText(delegation.getConfigInfo());
		}
	}
	
	private void handleCompatibilityConfigType() {
		showPage("Compatibility");
		if (delegation.getConfigInfo() == null) {
			delegation.setConfigInfo(compatibilityTextComposite.text.getText());
		} else {
			compatibilityTextComposite.text.setText(delegation.getConfigInfo());
		}
	}
	
	private void showInvalidTypeMessage() {
		messageLabel.setText("The class does not exist on the project classpath.");
		showPage("Message");
	}
	
	private IType getClassFor(String className) {
		if (className == null) return null;
		try {
			return ProjectFinder.getCurrentProject().findType(className);
		} catch (JavaModelException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void addGenericElement(TableItem item) {
		String name = item.getText(0) == null ? "" : item.getText(0);
		String value = item.getText(1) == null ? "" : item.getText(1);
		GenericElement genericElement = 
			(GenericElement)delegation.getFactory().createById("org.jbpm.gd.jpdl.genericElement");
		genericElement.setName(name);
		genericElement.setValue(value);
		delegation.addGenericElement(genericElement);
		item.setData(genericElement);
	}
	
	private void removeGenericElement(TableItem item) {
		GenericElement genericElement = (GenericElement)item.getData();
		if (genericElement != null) {
			delegation.removeGenericElement(genericElement);
		}
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.widget == searchButton) {
			handleSearchButtonSelected();
		} else if (e.widget == configTypeCombo) {
			handleConfigTypeComboChanged();
		}
	}

	private void handleConfigTypeComboChanged() {
		String newConfigType = toConfigType(configTypeCombo.getText());
		if (delegation.getConfigType().equals(newConfigType)) return;
		delegation.setConfigInfo(null);
		GenericElement[] genericElements = delegation.getGenericElements();
		for (int i = 0; i < genericElements.length; i++) {
			delegation.removeGenericElement(genericElements[i]);
		}
		delegation.setConfigType(newConfigType);
		updateVisiblePage(getClassFor(nameText.getText()) != null);
	}

	private void handleSearchButtonSelected() {
		String chosenClass = chooseDelegationClassDialog.openDialog();
		if (chosenClass != null) {
			nameText.setText(chosenClass);
			handleNameTextChange();
		}
	}
	
	private String toConfigType(String configType) {
		if ("Field".equals(configType)) return "field";
		if ("Bean".equals(configType)) return "bean";
		if ("Constructor".equals(configType)) return "constructor";
		if ("Compatibility".equals(configType)) return "configuration-property";
		return null;
	}
	
	private String fromConfigType(String configType) {
		if ("field".equals(configType)) return "Field";
		if ("bean".equals(configType)) return "Bean";
		if ("constructor".equals(configType)) return "Constructor";
		if ("configuration-property".equals(configType)) return "Compatibility";
		return null;
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == constructorTextComposite.text) {
			delegation.setConfigInfo(constructorTextComposite.text.getText());
		} else if (e.widget == compatibilityTextComposite.text) {
			delegation.setConfigInfo(compatibilityTextComposite.text.getText());
		}
	}
		
	public Delegation getDelegation() {
		return delegation;
	}
	
	
	private interface DelegationConfigurationWidget {
		void setVisible(boolean visible);
	}
	
	private class LabelComposite implements DelegationConfigurationWidget {
		
		private Label label;
		
		private void create(Composite parent) {
			label = widgetFactory.createLabel(parent, "");
			label.setLayoutData(createLabelLayoutData());
		}
		
		private void setText(String message) {
			label.setText(message);
		}
		
		public void setVisible(boolean visible) {
			label.setVisible(visible);
		}
		
		private FormData createLabelLayoutData() {
			FormData result = new FormData();
			result.left = new FormAttachment(0, 0);
			result.right = new FormAttachment(100, 0);
			result.top = new FormAttachment(configTypeCombo, 2);
			return result;
		}
		
	}
	
	private class TextComposite implements DelegationConfigurationWidget {
		
		private Label label;
		private Text text;
		
		private void create(Composite parent) {
			label = widgetFactory.createLabel(parent, "Config Info");
			text = widgetFactory.createText(parent, "", SWT.MULTI | SWT.V_SCROLL);
			label.setLayoutData(createLabelLayoutData());
			text.setLayoutData(createTextLayoutData());
		}
		
		private FormData createLabelLayoutData() {
			FormData result = new FormData();
			result.left = new FormAttachment(0, 0);
			result.top = new FormAttachment(configTypeCombo, 2);
			return result;
		}
		
		private FormData createTextLayoutData() {
			FormData result = new FormData();
			result.top = new FormAttachment(configTypeCombo, 0);
			result.left = new FormAttachment(nameText, 0);
			result.left.alignment = SWT.LEFT;
			result.right = new FormAttachment(100, 0);
			result.bottom = new FormAttachment(100, 0);
			return result;
		}
		
		public void setVisible(boolean visible) {
			label.setVisible(visible);
			text.setVisible(visible);
		}
		
	}
	
	
	private class TableComposite implements FocusListener, MouseListener, SelectionListener, DelegationConfigurationWidget {
		
		private Label label;
		private Table table;
		private TableEditor valueEditor;
		private Text valueText;
		
		private void create(Composite parent) {
			label = widgetFactory.createLabel(parent, "Config Info");
			table = widgetFactory.createTable(parent, SWT.CHECK | SWT.FULL_SELECTION | SWT.V_SCROLL);
			label.setLayoutData(createLabelLayoutData());
			table.setLayoutData(createTableLayoutData());
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			table.addSelectionListener(this);
			table.addMouseListener(this);
			AutoResizeTableLayout handlerConfigBeanTableLayout = new AutoResizeTableLayout(table);
			handlerConfigBeanTableLayout.addColumnData(new ColumnWeightData(40));
			handlerConfigBeanTableLayout.addColumnData(new ColumnWeightData(60));
			table.setLayout(handlerConfigBeanTableLayout);
			TableColumn handlerConfigBeanTableNameColumn = new TableColumn(table, SWT.NONE);
			handlerConfigBeanTableNameColumn.setText("Name");
			TableColumn handlerConfigBeanTableValueColumn = new TableColumn(table, SWT.NONE);
			handlerConfigBeanTableValueColumn.setText("Value");
			createEditor();
		}
		
		private FormData createLabelLayoutData() {
			FormData result = new FormData();
			result.left = new FormAttachment(0, 0);
			result.top = new FormAttachment(configTypeCombo, 2);
			return result;
		}
		
		private FormData createTableLayoutData() {
			FormData result = new FormData();
			result.top = new FormAttachment(configTypeCombo, 0);
			result.left = new FormAttachment(nameText, 0);
			result.left.alignment = SWT.LEFT;
			result.right = new FormAttachment(100, 0);
			result.bottom = new FormAttachment(100, 0);
			return result;
		}

		private void createEditor() {
			valueEditor = new TableEditor(table);
			valueText = new Text(table, SWT.NORMAL);
			valueText.setVisible(false);
			valueText.setText("");
			valueEditor.minimumWidth = valueText.getSize().x;
			valueEditor.horizontalAlignment = SWT.LEFT;
			valueEditor.grabHorizontal = true;
		}
		
		private void doEdit() {
			if (valueText.isVisible()) endEdit();
			if (table.getSelectionIndex() == -1) return;
			TableItem selection = table.getItem(table.getSelectionIndex());
			String value = selection.getText(1);
			valueText.setText(value == null ? "" : value);
			valueEditor.setEditor(valueText, selection, 1);
			valueText.setVisible(true);
			valueText.selectAll();
			valueText.setFocus();
			valueText.addFocusListener(this);
		}
		
		private void endEdit() {
			valueText.setVisible(false);
			valueText.setText("");
			valueText.removeFocusListener(this);
		}
		
		public void mouseDoubleClick(MouseEvent e) {
		}

		public void mouseDown(MouseEvent e) {
			int column = getSelectedColumn(e.x, e.y);
			if (column == -1) return;
			if (column == 1) {
				doEdit();
			}
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

		public void widgetDefaultSelected(SelectionEvent e) {
		}

		private void applyValue() {
			TableItem item = getSelectedTableItem();
			if (item == null) return;
			item.setText(1, valueText.getText());
			GenericElement element = (GenericElement)item.getData();
			if (element == null) return;
			element.setValue(valueText.getText());
		}

		public void widgetSelected(SelectionEvent e) {
			if (e.widget == table) {
				if (e.detail == SWT.CHECK && e.item instanceof TableItem) {
					handleTableItemCheck((TableItem)e.item);
				}
			}
		}
		
		private void handleTableItemCheck(TableItem item) {
			if (item.getChecked()) {
				addGenericElement(item);
			} else {
				removeGenericElement(item);
			}
			table.setSelection(item);
		}
		
		public void setVisible(boolean visible) {
			label.setVisible(visible);
			table.setVisible(visible);
		}

		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			if (e.widget == valueText) {
				applyValue();
				endEdit();
			}
		}
		
	}


}
