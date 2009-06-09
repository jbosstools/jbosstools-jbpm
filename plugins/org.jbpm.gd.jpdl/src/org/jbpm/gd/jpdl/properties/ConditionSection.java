package org.jbpm.gd.jpdl.properties;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.properties.AbstractPropertySection;
import org.jbpm.gd.jpdl.model.Condition;
import org.jbpm.gd.jpdl.model.Transition;


public class ConditionSection extends AbstractPropertySection implements SelectionListener, FocusListener {
	
	private Label conditionLabel;
	private CCombo conditionCombo;	
	private Label label;
	private Text expressionText;
	private Text scriptText;
	private Transition transition;
	
	public void createControls(Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite clientArea = getWidgetFactory().createFlatFormComposite(parent);
        conditionLabel = getWidgetFactory().createLabel(clientArea, "Condition Type");
        conditionCombo = getWidgetFactory().createCCombo(clientArea);
        conditionCombo.setItems(new String[] {"Unconditional", "Expression", "Script" });
        conditionCombo.setEditable(false);
        label = getWidgetFactory().createLabel(clientArea, "");
        expressionText = getWidgetFactory().createText(clientArea, "");
        scriptText = getWidgetFactory().createText(clientArea, "", SWT.H_SCROLL | SWT.V_SCROLL);
        conditionLabel.setLayoutData(createConditionLabelLayoutData());
        conditionCombo.setLayoutData(createConditionComboLayoutData());
        label.setLayoutData(createLabelLayoutData());
        expressionText.setLayoutData(createExpressionTextLayoutData());
        scriptText.setLayoutData(createScriptTextLayoutData());
        hookListeners();
        refresh();
    }
	
	private void hookListeners() {
		conditionCombo.addSelectionListener(this);
		expressionText.addSelectionListener(this);
		expressionText.addFocusListener(this);
		scriptText.addSelectionListener(this);
		scriptText.addFocusListener(this);
	}
	
	private FormData createConditionLabelLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 5);
		result.top = new FormAttachment(0, 5);
		return result;
	}
	
	private FormData createConditionComboLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(conditionLabel, 5);
		result.top = new FormAttachment(0, 5);
		return result;
	}
	
	private FormData createLabelLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 5);
		result.right = new FormAttachment(100, -5);
		result.top = new FormAttachment(conditionCombo, 10);
		return result;
	}
	
	private FormData createExpressionTextLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 5);
		result.right = new FormAttachment(100, -5);
		result.top = new FormAttachment(label, 0);
		return result;
	}
	
	private FormData createScriptTextLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 5);
		result.right = new FormAttachment(100, -5);
		result.top = new FormAttachment(label, 0);
		result.height = 100;
		return result;
	}
	
 	public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        if (!(selection instanceof IStructuredSelection)) return;
        Object input = ((IStructuredSelection)selection).getFirstElement();
        if (input instanceof NotationElementGraphicalEditPart) {
        	AbstractNotationElement notationElement = ((NotationElementGraphicalEditPart)input).getNotationElement();
        	input = notationElement.getSemanticElement();
        } else if (input instanceof OutlineEditPart) {
        	input = ((OutlineEditPart)input).getModel();
        }
        if (input instanceof Transition) {
        	transition = (Transition)input;
        	refresh();
        }
    }
 	
 	public void refresh() {
 		if (transition == null || transition.getCondition() == null){
 			conditionCombo.setText("Unconditional");
			label.setText("");
			expressionText.setText("");
			expressionText.setVisible(false);
			scriptText.setText("");
			scriptText.setVisible(false);
 		} else {
 			Condition condition = transition.getCondition();
 			if (condition != null && condition.getExpression() != null) {
 				conditionCombo.setText("Expression");
 				label.setText("Condition");
 				expressionText.setText(condition.getExpression());
 				expressionText.setVisible(true);
 				scriptText.setText("");
 				scriptText.setVisible(false);
 			} else if (condition != null && condition.getScript() != null) {
 				conditionCombo.setText("Script");
 				label.setText("Script");
 				scriptText.setText(condition.getScript());
 				scriptText.setVisible(true);
 				expressionText.setText("");
 				expressionText.setVisible(false);
 			} else {
 				conditionCombo.setText("Unknown");
 				label.setText("");
 				expressionText.setVisible(false);
 				scriptText.setVisible(false);
 			}
 		}
     }
 	
	public boolean shouldUseExtraSpace() {
		return true;
	}
		
	public void widgetDefaultSelected(SelectionEvent e) {
		if (e.widget == expressionText) {
			handleExpressionTextChanged();
		} else if (e.widget == scriptText) {
			handleScriptTextChanged();
		}		
	}
	
	private void handleExpressionTextChanged() {
		if (transition != null && transition.getCondition() != null) {
			transition.getCondition().setExpression(expressionText.getText());
		}
	}

	private void handleScriptTextChanged() {
		if (transition != null && transition.getCondition() != null) {
			transition.getCondition().setScript(scriptText.getText());
		}
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == conditionCombo) {
			handleConditionComboSelected();
		}
	}
	
	private void handleConditionComboSelected() {
		if ("Expression".equals(conditionCombo.getText())) {
			label.setText("Expression");
			scriptText.setVisible(false);
			expressionText.setVisible(true);
			if (transition != null) {
				updateCondition();
			}
		} else if ("Script".equals(conditionCombo.getText())) {
			label.setText("Script");
			expressionText.setVisible(false);
			scriptText.setVisible(true);
			if (transition != null) {
				updateCondition();
			}
		} else {
			label.setText("");
			expressionText.setVisible(false);
			scriptText.setVisible(false);
			if (transition != null) {
				transition.setCondition(null);
			}
		}
	}
	
	private void updateCondition() {
		Condition condition = transition.getCondition();
		if (condition == null) {
			condition = (Condition)transition.getFactory().createById("org.jbpm.gd.jpdl.condition");
			transition.setCondition(condition);
		}
		if ("Expression".equals(conditionCombo.getText())) {
			condition.setScript(null);
			condition.setExpression(expressionText.getText());
		} else if ("Script".equals(conditionCombo.getText())) {
			condition.setExpression(null);
			condition.setScript(scriptText.getText());
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == expressionText) {
			handleExpressionTextChanged();
		} else if (e.widget == scriptText) {
			handleScriptTextChanged();
		}		
	}
	
}