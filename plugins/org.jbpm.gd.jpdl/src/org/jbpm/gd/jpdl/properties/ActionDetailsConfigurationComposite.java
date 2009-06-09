package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.common.model.GenericElement;
import org.jbpm.gd.jpdl.dialog.ChooseDelegationClassDialog;
import org.jbpm.gd.jpdl.model.Action;

public class ActionDetailsConfigurationComposite implements SelectionListener, FocusListener {
	
	public static ActionDetailsConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		ActionDetailsConfigurationComposite result = new ActionDetailsConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private CCombo actionTypeCombo;
	private Composite handlerComposite;
	private Composite expressionComposite;	
	private Label expressionLabel;
	private Text expressionText;
	private DelegationConfigurationComposite delegationConfigurationComposite;	
	private Action action;
	private String selectedActionType = "<Choose>";
	
	private ActionDetailsConfigurationComposite() {}
	
	public void setAction(Action action) {
		if (this.action == action) return;
		unhookListeners();
		clearControls();
		this.action = action;
		if (action != null) {
			updateControls();
			hookListeners();
		}
	}
	
	public Action getAction() {
		return action;
	}
	
	private void hookListeners() {
		actionTypeCombo.addSelectionListener(this);
		expressionText.addSelectionListener(this);
		expressionText.addFocusListener(this);
	}
	
	private void unhookListeners() {
		actionTypeCombo.removeSelectionListener(this);
		expressionText.removeSelectionListener(this);
		expressionText.removeFocusListener(this);
	}
	
	private void clearControls() {
		actionTypeCombo.setText("<Choose>");
		expressionText.setText("");
		delegationConfigurationComposite.setDelegation(null);
		handlerComposite.setVisible(false);
		expressionComposite.setVisible(false);
	}
	
	private void updateControls() {
		String expression = action.getExpression();
		expressionText.setText(expression == null ? "" : expression);
		delegationConfigurationComposite.setDelegation(action);
		if (action.getClassName() != null) {
			actionTypeCombo.setText("Handler");
		} else if (expression != null) {
			actionTypeCombo.setText("Expression");
		} else {
			actionTypeCombo.setText("<Choose>");
		}
		handlerComposite.setVisible("Handler".equals(actionTypeCombo.getText()));
		expressionComposite.setVisible("Expression".equals(actionTypeCombo.getText()));
	}
	
	private void create() {
		createTypeCombo();
		createHandlerComposite();
		createExpressionComposite();
		initializeLayouts();
	}
	
	private void createTypeCombo() {
		actionTypeCombo = widgetFactory.createCCombo(parent);
		actionTypeCombo.setItems(new String[] {"<Choose>", "Expression", "Handler"});
		actionTypeCombo.setEditable(false);
		actionTypeCombo.setText("<Choose Type>");
		actionTypeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleActionTypeComboSelected();
			}
		});
	}
	
	private void createHandlerComposite() {
		handlerComposite = widgetFactory.createFlatFormComposite(parent);
		handlerComposite.setVisible(false);
		delegationConfigurationComposite = DelegationConfigurationComposite.create(widgetFactory, handlerComposite, createChooseActionHandlerDialog());
	}
	
	private ChooseDelegationClassDialog createChooseActionHandlerDialog() {
		return new ChooseDelegationClassDialog(
				parent.getShell(), 
				"org.jbpm.graph.def.ActionHandler",
				"Choose Action Handler",
				"Choose an action handler from the list");
	}
	
	private void createExpressionComposite() {
		expressionComposite = widgetFactory.createFlatFormComposite(parent);
		expressionComposite.setVisible(false);
		expressionLabel = widgetFactory.createLabel(expressionComposite, "Expression");
		expressionText = widgetFactory.createText(expressionComposite, "", SWT.MULTI | SWT.V_SCROLL);
	}
	
	private void initializeLayouts() {
		actionTypeCombo.setLayoutData(createActionTypeComboLayoutData());
		handlerComposite.setLayoutData(createActionTypeCompositeLayoutData());
		expressionComposite.setLayoutData(createActionTypeCompositeLayoutData());
		expressionLabel.setLayoutData(createExpressionLabelLayoutData());
		expressionText.setLayoutData(createExpressionTextLayoutData());
	}
	
	private FormData createExpressionLabelLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 2);
		result.left = new FormAttachment(0, 0);
		return result;
	}
	
	private FormData createExpressionTextLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(expressionLabel, 0);
		result.right = new FormAttachment(100, -5);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
	private FormData createActionTypeComboLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.top = new FormAttachment(0, 0);
		return result;
	}
	
	private FormData createActionTypeCompositeLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(actionTypeCombo, 0);
		result.top = new FormAttachment(0, 0);
		result.bottom = new FormAttachment(100, 0);
		result.right = new FormAttachment(100, 0);
		result.height = 120;
		return result;
	}

	private void updateExpressionText() {
		action.setExpression(expressionText.getText());
	}
	
	private void updateControlVisibility() {
		handlerComposite.setVisible("Handler".equals(actionTypeCombo.getText()));
		expressionComposite.setVisible("Expression".equals(actionTypeCombo.getText()));
	}
	
	private void handleActionTypeComboSelected() {
		String selection = actionTypeCombo.getText();
		if (selection.equals(selectedActionType)) return;
		selectedActionType = selection;
		updateControlVisibility();
		if (action != null) {
			removeExpressionAndHandlerInfo();
			updateExpressionAndHandlerInfo();
		}
	}

	private void updateExpressionAndHandlerInfo() {
		if ("Expression".equals(selectedActionType)) {
			action.setExpression(expressionText.getText());
			removeHandlerInfo();
		} else if ("Handler".equals(selectedActionType)) {
			delegationConfigurationComposite.setDelegation(action);
			action.setClassName("");
			action.setExpression(null);
		} else {
			removeHandlerInfo();
			action.setExpression(null);
		}
	}

	private void removeHandlerInfo() {
		action.setClassName(null);
		action.setConfigInfo(null);
		action.setConfigType(null);
		GenericElement[] configElements = action.getGenericElements();
		for (int i = 0; i < configElements.length; i++) {
			action.removeGenericElement(configElements[i]);
		}
		delegationConfigurationComposite.setDelegation(null);
	}
	
	private void removeExpressionAndHandlerInfo() {
		action.setExpression(null);
		action.setClassName(null);
		action.setConfigInfo(null);
		action.setConfigType(null);
		GenericElement[] configElements = action.getGenericElements();
		for (int i = 0; i < configElements.length; i++) {
			action.removeGenericElement(configElements[i]);
		}
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == expressionText) {
			updateExpressionText();
		}		
	}

	public void focusGained(FocusEvent e) {
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == actionTypeCombo) {
			handleActionTypeComboSelected();
		}		
	}
	
}
