package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jbpm.gd.jpdl.model.Assignment;

public class AssignmentExpressionInfoComposite extends AssignmentTypeComposite implements FocusListener {
	
    private Label expressionLabel;
    private Text expressionText;
    
	protected void hookListeners() {
		expressionText.addFocusListener(this);
	}
	
	protected void unhookListeners() {
		expressionText.removeFocusListener(this);
	}
	
	protected void clearControls() {
		expressionText.setText("");
	}
	
	protected void updateControls() {
		if (assignable.getAssignment() != null  && assignable.getAssignment().getExpression() != null) {
			expressionText.setText(assignable.getAssignment().getExpression());
		}
	}
	
	protected void setActive(boolean active) {
		Assignment assignment = getAssignment();
		if (active) {
			assignment.setExpression(expressionText.getText());
		} else {
			assignment.setExpression(null);
		}
	}

	protected void create() {
		Composite composite = widgetFactory.createComposite(parent);
		composite.setLayout(new FormLayout());
		expressionLabel = widgetFactory.createLabel(composite, "Expression");
		expressionText = widgetFactory.createText(composite, "", SWT.MULTI | SWT.V_SCROLL);
		composite.setLayoutData(createGroupLayoutData());
		expressionLabel.setLayoutData(createExpressionLabelLayoutData());
		expressionText.setLayoutData(createExpressionTextLayoutData());
	}
	
	private FormData createGroupLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
	private FormData createExpressionLabelLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 5);
		result.left = new FormAttachment(0, 5);
		return result;		
	}
	
	private FormData createExpressionTextLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 5);
		result.left = new FormAttachment(expressionLabel, 5);
		result.right = new FormAttachment(100, -5);
		result.bottom = new FormAttachment(100, -5);
		return result;		
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (assignable.getAssignment() == null) return;
		assignable.getAssignment().setExpression(expressionText.getText());
	}	
	
}
