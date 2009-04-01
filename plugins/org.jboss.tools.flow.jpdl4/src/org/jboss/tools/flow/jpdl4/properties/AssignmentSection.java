package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.flow.jpdl4.command.ChangePropertyCommand;
import org.jboss.tools.flow.jpdl4.model.HumanTask;

public class AssignmentSection extends AbstractPropertySection {

	private CCombo typeCombo;
	private CLabel typeLabel;
	private Text expressionText;
	private CLabel expressionLabel;
//	private Text languageText;
//	private CLabel languageLabel;

//	private Composite parent;
	
	private IPropertySource input;
	
	private CommandStack commandStack;

//	private CommandStackListener commandStackListener = new CommandStackListener() {
//		public void commandStackChanged(EventObject event) {
//			if (!parent.isDisposed()) {
//				refresh();
//			}
//		}		
//	};

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
//		this.parent = parent;
		if (aTabbedPropertySheetPage instanceof JpdlPropertySheetPage) {
			commandStack = ((JpdlPropertySheetPage)aTabbedPropertySheetPage).getCommandStack();
//			commandStack.addCommandStackListener(commandStackListener);
		}
		Composite composite = getWidgetFactory().createFlatFormComposite(parent);
		createTypeLabel(composite);
		createTypeCombo(composite);
		createExpressionLabel(composite);
		createExpressionText(composite);
//		createLanguageLabel(composite);
//		createLanguageText(composite);
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
		data.left = new FormAttachment(0, 85);
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
		data.left = new FormAttachment(0, 80);
		expressionText.setLayoutData(data);
	}
	
//	private void createLanguageLabel(Composite parent) {
//		languageLabel = getWidgetFactory().createCLabel(parent, "Language");
//		FormData data = new FormData();
//		data.left = new FormAttachment(0, 0);
//		data.top = new FormAttachment(expressionText, 2);
//		languageLabel.setLayoutData(data);
//	}
//	
//	private void createLanguageText(Composite parent) {
//		languageText = getWidgetFactory().createText(parent, "");
//		FormData data = new FormData();
//		data.top = new FormAttachment(expressionText, 0);
//		data.left = new FormAttachment(0, 80);
//		data.right = new FormAttachment(100, 0);
//		languageText.setLayoutData(data);
//	}

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if (selection instanceof IStructuredSelection) {
			Object object = ((IStructuredSelection)selection).getFirstElement();
			if (object instanceof IAdaptable) {
				object = ((IAdaptable)object).getAdapter(IPropertySource.class);
				if (object instanceof IPropertySource) {
					input = (IPropertySource)object;
					return;
				}
			}
		}
		input = null;
	}
	
	private SelectionListener typeComboSelectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			changeProperty(HumanTask.ASSIGNMENT_TYPE, typeCombo.getSelectionIndex());
			expressionText.setEnabled(!HumanTask.NONE.equals(typeCombo.getText()));
		}		
	};
	
	private ModifyListener expressionTextModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			changeProperty(HumanTask.ASSIGNMENT_EXPRESSION, expressionText.getText());
		}		
	};
	
//	private ModifyListener languageTextModifyListener = new ModifyListener() {
//		public void modifyText(ModifyEvent e) {
//			changeProperty(HumanTask.ASSIGNMENT_EXPRESSION_LANGUAGE, languageText.getText());
//		}		
//	};
	
	protected void changeProperty(Object propertyId, Object newValue) {
		if (commandStack == null || input == null) return;
		Object oldValue = input.getPropertyValue(propertyId);
		if (oldValue == newValue) return;
		ChangePropertyCommand changePropertyCommand = new ChangePropertyCommand();
		changePropertyCommand.setPropertyId(propertyId);
		changePropertyCommand.setTarget(input);
		changePropertyCommand.setNewValue(newValue);
		commandStack.execute(changePropertyCommand);
	}
	
	protected void unhookListeners() {
		typeCombo.removeSelectionListener(typeComboSelectionListener);
		expressionText.removeModifyListener(expressionTextModifyListener);
//		languageText.removeModifyListener(languageTextModifyListener);
	}
	
	protected void hookListeners() {
		typeCombo.addSelectionListener(typeComboSelectionListener);
		expressionText.addModifyListener(expressionTextModifyListener);
//		languageText.addModifyListener(languageTextModifyListener);
	}
	
	protected void updateValues() {
		if (input == null) {
			typeCombo.setText(HumanTask.NONE);
			expressionText.setEnabled(false);
			expressionText.setText("");
//			languageText.setText("");
		} else {
			typeCombo.setText(HumanTask.ASSIGNMENT_TYPES[(Integer)input.getPropertyValue(HumanTask.ASSIGNMENT_TYPE)]);
			if (HumanTask.NONE.equals(typeCombo.getText())) {
				expressionText.setEnabled(false);
				expressionText.setText("");
			} else {
				expressionText.setEnabled(true);
				expressionText.setText((String)input.getPropertyValue(HumanTask.ASSIGNMENT_EXPRESSION));
//			    languageText.setText((String)input.getPropertyValue(HumanTask.ASSIGNMENT_EXPRESSION_LANGUAGE));
			}
		}
	}

	public void aboutToBeShown() {
		refresh();
	}
	
	public void refresh() {
		unhookListeners();
		updateValues();
		hookListeners();
	}

}
