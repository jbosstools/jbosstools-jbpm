package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jbpm.gd.jpdl.model.Task;

public class AssignmentSwimlaneInfoComposite extends AssignmentTypeComposite implements FocusListener {
	
    private Label nameLabel;
    private Text nameText;
    
	protected void hookListeners() {
		nameText.addFocusListener(this);
	}
	
	protected void unhookListeners() {
		nameText.removeFocusListener(this);
	}
	
	protected void clearControls() {
		nameText.setText("");
	}
	
	protected void updateControls() {
		if (!(assignable instanceof Task)) return;
		String swimlane = ((Task)assignable).getSwimlane();
		nameText.setText(swimlane == null ? "" : swimlane);
	}
	
	protected void setActive(boolean active) {
		if (!(assignable instanceof Task)) return;
		if (active) {
			if (assignable.getAssignment() != null) {
				assignable.setAssignment(null);
			}
			((Task)assignable).setSwimlane(nameText.getText());
		} else {
			((Task)assignable).setSwimlane(null);
		}
	}

	protected void create() {
		Composite composite = widgetFactory.createComposite(parent);
		composite.setLayout(new FormLayout());
		nameLabel = widgetFactory.createLabel(composite, "Swimlane Name");
		nameText = widgetFactory.createText(composite, "");
		composite.setLayoutData(createCompositeLayoutData());
		nameLabel.setLayoutData(createNameLabelLayoutData());
		nameText.setLayoutData(createNameTextLayoutData());
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
		result.left = new FormAttachment(nameLabel, 5);
		result.right = new FormAttachment(100, -5);
		result.bottom = new FormAttachment(100, -5);
		return result;		
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (!(assignable instanceof Task)) return;
		if (e.widget == nameText) {
			((Task)assignable).setSwimlane(nameText.getText());
		}
	}	
	
}
