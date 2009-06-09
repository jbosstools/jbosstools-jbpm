package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Action;

public class ActionNameConfigurationComposite implements SelectionListener, FocusListener {
	
	public static ActionNameConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		ActionNameConfigurationComposite result = new ActionNameConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Label nameLabel;
	private Text nameText;
	private Button referenceButton;
	
	private Action action;
	
	private ActionNameConfigurationComposite() {}
	
	public void setAction(Action action) {
		if (this.action == action) return;
		unhookSelectionListener();
		clearControls();
		this.action = action;
		if (action != null) {
			updateControls();
			hookSelectionListener();
		}
	}
	
	private void hookSelectionListener() {
		nameText.addFocusListener(this);
		referenceButton.addSelectionListener(this);
	}
	
	private void unhookSelectionListener() {
		nameText.removeFocusListener(this);
		referenceButton.removeSelectionListener(this);
	}
	
	private void clearControls() {
		nameText.setText("");
		referenceButton.setSelection(false);
	}
	
	private void updateControls() {
		String name = action.getRefName();
		if (name != null) {
			referenceButton.setSelection(true);
		} else {
			referenceButton.setSelection(false);
			name = action.getName();
		}
		nameText.setText(name == null ? "" : name);
	}
	
	private void create() {
		nameLabel = widgetFactory.createLabel(parent, "Name");
        nameText = widgetFactory.createText(parent, "");
        referenceButton = widgetFactory.createButton(parent, "Reference", SWT.CHECK);
        nameLabel.setLayoutData(createNameLabelLayoutData());
        nameText.setLayoutData(createNameTextLayoutData());
        referenceButton.setLayoutData(createReferenceButtonLayoutData()); 
	}
	
	private FormData createNameTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 85);
		data.top = new FormAttachment(0, 5);
		data.right = new FormAttachment(referenceButton, 0);
		return data;
	}
	
	private FormData createNameLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 7);
		return data;
	}
	
	private FormData createReferenceButtonLayoutData() {
		FormData data = new FormData();
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 7);
		return data;
	}
	
	private String getNameText() {
		String text = nameText.getText();
		if ("".equals(text)) {
			text = null;
		}
		return text;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == referenceButton) {
			updateActionName();
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == nameText) {
			updateActionName();
		}
	}

	private void updateActionName() {
		if (referenceButton.getSelection()) {
			action.setRefName(getNameText());
			action.setName(null);
		} else {
			action.setName(getNameText());
			action.setRefName(null);
		}
	}
	
}
