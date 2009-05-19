package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.flow.jpdl4.model.Assignment;
import org.jboss.tools.flow.jpdl4.model.HumanTask;

public class AssignmentSection extends JpdlPropertySection {

	private CCombo typeCombo;
	private CLabel typeLabel;
	private Text expressionText;
	private CLabel expressionLabel;

	private SelectionListener typeComboSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			changeProperty(HumanTask.ASSIGNMENT_TYPE, typeCombo.getSelectionIndex());
			expressionText.setEnabled(!Assignment.NONE.equals(typeCombo.getText()));
		}		
	};
	
	private ModifyListener expressionTextModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			changeProperty(Assignment.ASSIGNMENT_EXPRESSION, expressionText.getText());
		}		
	};
	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getFlatFormComposite();
		createTypeLabel(composite);
		createTypeCombo(composite);
		createExpressionLabel(composite);
		createExpressionText(composite);
	}
	
	
	private void createTypeLabel(Composite parent) {
		typeLabel = getWidgetFactory().createCLabel(parent, "Type");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 0);
		typeLabel.setLayoutData(data);
	}
	
	private void createTypeCombo(Composite parent) {
		typeCombo = getWidgetFactory().createCCombo(parent);
		typeCombo.setItems(HumanTask.ASSIGNMENT_TYPES);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(JpdlPropertySection.SECOND_COLUMN_LEFT_LIMIT, 0);
		typeCombo.setLayoutData(data);
	}
	
	private void createExpressionLabel(Composite parent) {
		expressionLabel = getWidgetFactory().createCLabel(parent, "Expression");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(typeCombo, 4);
		expressionLabel.setLayoutData(data);
	}
	
	private void createExpressionText(Composite parent) {
		expressionText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(typeCombo, 2);
		data.right = new FormAttachment(100, 0);
		data.left = new FormAttachment(JpdlPropertySection.SECOND_COLUMN_LEFT_LIMIT, 0);
		expressionText.setLayoutData(data);
	}
	
	protected void unhookListeners() {
		typeCombo.removeSelectionListener(typeComboSelectionListener);
		expressionText.removeModifyListener(expressionTextModifyListener);
	}
	
	protected void hookListeners() {
		typeCombo.addSelectionListener(typeComboSelectionListener);
		expressionText.addModifyListener(expressionTextModifyListener);
	}
	
	protected void updateValues() {
		IPropertySource input = getInput();
		if (input == null) {
			typeCombo.setText(HumanTask.NONE);
			expressionText.setEnabled(false);
			expressionText.setText("");
		} else {
			typeCombo.setText(HumanTask.ASSIGNMENT_TYPES[(Integer)input.getPropertyValue(HumanTask.ASSIGNMENT_TYPE)]);
			if (HumanTask.NONE.equals(typeCombo.getText())) {
				expressionText.setEnabled(false);
				expressionText.setText("");
			} else {
				expressionText.setEnabled(true);
				expressionText.setText((String)input.getPropertyValue(HumanTask.ASSIGNMENT_EXPRESSION));
			}
		}
	}

}
