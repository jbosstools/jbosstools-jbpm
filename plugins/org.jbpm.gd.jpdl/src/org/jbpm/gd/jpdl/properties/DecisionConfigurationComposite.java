package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.dialog.ChooseDelegationClassDialog;
import org.jbpm.gd.jpdl.model.Decision;
import org.jbpm.gd.jpdl.model.Handler;

public class DecisionConfigurationComposite implements SelectionListener, FocusListener {
	
	public static DecisionConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		DecisionConfigurationComposite result = new DecisionConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	private Decision decision;
	
	private Label decisionConfigurationTypeLabel;
	private CCombo decisionConfigurationTypeCombo;
	private Composite delegationComposite;
	private DelegationConfigurationComposite delegationConfigurationComposite;
	private Composite expressionComposite;
	private Label expressionLabel;
	private Text expressionText;
	
	private String selectedDecisionType;
		
	public void setDecision(Decision decision) {
		if (this.decision == decision) return;
		unhookListeners();
		clearControls();
		this.decision = decision;
		if (decision != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		decisionConfigurationTypeCombo.addSelectionListener(this);
		expressionText.addFocusListener(this);
	}
	
	private void unhookListeners() {
		decisionConfigurationTypeCombo.removeSelectionListener(this);
		expressionText.removeFocusListener(this);
	}
	
	private void clearControls() {
		decisionConfigurationTypeCombo.setText("");
		delegationConfigurationComposite.setDelegation(null);
		expressionText.setText("");
	}
	
	private void updateControls() {
		if (decision.getHandler() != null) {
			decisionConfigurationTypeCombo.setText("delegation");
			delegationConfigurationComposite.setDelegation(decision.getHandler());	
			delegationComposite.setVisible(true);
		} else if (decision.getExpression() != null) {
			decisionConfigurationTypeCombo.setText("expression");
			expressionText.setText(decision.getExpression());
			expressionComposite.setVisible(true);
		} else {
			decisionConfigurationTypeCombo.setText("");
			expressionComposite.setVisible(false);
			delegationComposite.setVisible(false);
		}
	}
	
	private void create() {
		createMain();
		createExpression();
		createDelegation();
		initializeLayouts();
	}
	
	private void createMain() {
		decisionConfigurationTypeLabel = widgetFactory.createLabel(parent, "Choose Type");
		decisionConfigurationTypeCombo = widgetFactory.createCCombo(parent);
		decisionConfigurationTypeCombo.setItems(new String[] {"", "expression", "delegation"});
		decisionConfigurationTypeCombo.setEditable(false);
	}
	
	private void createDelegation() {
		delegationComposite = widgetFactory.createFlatFormComposite(parent);
		delegationComposite.setVisible(false);
		delegationConfigurationComposite = DelegationConfigurationComposite.create(widgetFactory, delegationComposite, createChooseDecisionHandlerDialog());
	}
	
	private ChooseDelegationClassDialog createChooseDecisionHandlerDialog() {
		return new ChooseDelegationClassDialog(
				parent.getShell(), 
				"org.jbpm.graph.node.DecisionHandler",
				"Choose Decision Handler",
				"Choose a decision handler from the list");
	}
	
	private void createExpression() {
		expressionComposite = widgetFactory.createFlatFormComposite(parent);
		expressionComposite.setVisible(false);
		expressionLabel = widgetFactory.createLabel(expressionComposite, "Expression");
		expressionText = widgetFactory.createText(expressionComposite, "");
	}
	
	private void initializeLayouts() {
		decisionConfigurationTypeLabel.setLayoutData(createConfigurationTypeLabelLayoutData());
		decisionConfigurationTypeCombo.setLayoutData(createConfigurationTypeComboLayoutData());
		delegationComposite.setLayoutData(createDelegationCompositeLayoutData());
		expressionComposite.setLayoutData(createExpressionCompositeLayoutData());
		expressionLabel.setLayoutData(createExpressionLabelLayoutData());
		expressionText.setLayoutData(createExpressionTextLayoutData());
	}
	
	
	
	private FormData createExpressionLabelLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(0, 0);
		return result;
	}
	
	private FormData createExpressionTextLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(expressionLabel, 0);
		result.right = new FormAttachment(100, 0);
		return result;
	}
	
	private FormData createConfigurationTypeLabelLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 2);
		result.left = new FormAttachment(0, 0);
		return result;
	}
	
	private FormData createConfigurationTypeComboLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(decisionConfigurationTypeLabel, 0);
		result.left = new FormAttachment(0, 0);
		return result;
	}
	
	private FormData createDelegationCompositeLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, -2);
		result.left = new FormAttachment(decisionConfigurationTypeCombo, -2);
		result.right = new FormAttachment(100, 0);
		return result;
	}
	
	private FormData createExpressionCompositeLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, -2);
		result.left = new FormAttachment(decisionConfigurationTypeCombo, -2);
		result.right = new FormAttachment(100, 0);
		return result;
	}
	
	private void updateExpressionText() {
		decision.setExpression(expressionText.getText());
	}
	
	private void updateControlVisibility() {
		delegationComposite.setVisible("delegation".equals(decisionConfigurationTypeCombo.getText()));
		expressionComposite.setVisible("expression".equals(decisionConfigurationTypeCombo.getText()));
	}
	
	private void handleConfigurationTypeComboSelected() {
		String selection = decisionConfigurationTypeCombo.getText();
		if (selection.equals(selectedDecisionType)) return;
		selectedDecisionType = selection;
		updateControlVisibility();
		if (decision != null) {
			removeExpressionAndHandlerInfo();
			updateExpressionAndHandlerInfo();
		}
	}

	private void updateExpressionAndHandlerInfo() {
		if ("expression".equals(selectedDecisionType)) {
			decision.setExpression(expressionText.getText());
		} else if ("delegation".equals(selectedDecisionType)) {
			Handler handler = (Handler)decision.getFactory().createById("org.jbpm.gd.jpdl.handler");
			decision.setHandler(handler);
			delegationConfigurationComposite.setDelegation(handler);
		}
	}
	
	private void removeExpressionAndHandlerInfo() {
		decision.setExpression(null);
		decision.setHandler(null);
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
		if (e.widget == decisionConfigurationTypeCombo) {
			handleConfigurationTypeComboSelected();
		}
	}
	
}
