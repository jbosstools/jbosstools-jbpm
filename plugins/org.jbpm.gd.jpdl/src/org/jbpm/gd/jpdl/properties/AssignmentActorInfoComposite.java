package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jbpm.gd.jpdl.model.Assignment;

public class AssignmentActorInfoComposite extends AssignmentTypeComposite implements FocusListener {
	
    private Label actorLabel;
    private Text actorText;
    
	protected void hookListeners() {
		actorText.addFocusListener(this);
	}
	
	protected void unhookListeners() {
		actorText.removeFocusListener(this);
	}
	
	protected void clearControls() {
		actorText.setText("");
	}
	
	protected void updateControls() {
		if (assignable.getAssignment() != null  && assignable.getAssignment().getActorId() != null) {
			actorText.setText(assignable.getAssignment().getActorId());
		}
	}
	
	protected void setActive(boolean active) {
		Assignment assignment = getAssignment();
		if (active) {
			assignment.setActorId(actorText.getText());
		} else {
			assignment.setActorId(null);
		}
	}

	protected void create() {
		Composite composite = widgetFactory.createComposite(parent);
		composite.setLayout(new FormLayout());
		actorLabel = widgetFactory.createLabel(composite, "Actor");
		actorText = widgetFactory.createText(composite, "");
		composite.setLayoutData(createCompositeLayoutData());
		actorLabel.setLayoutData(createNameLabelLayoutData());
		actorText.setLayoutData(createNameTextLayoutData());
	}
	
	private FormData createCompositeLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		return result;
	}
	
	private FormData createNameLabelLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 5);
		result.left = new FormAttachment(0, 5);
		result.bottom = new FormAttachment(100, -5);
		return result;		
	}
	
	private FormData createNameTextLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 5);
		result.left = new FormAttachment(actorLabel, 5);
		result.right = new FormAttachment(100, -5);
		result.bottom = new FormAttachment(100, -5);
		return result;		
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (assignable.getAssignment() == null) return;
		assignable.getAssignment().setActorId(actorText.getText());
	}	
	
}
