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

public class AssignmentPooledActorsInfoComposite extends AssignmentTypeComposite implements FocusListener {
	
    private Label pooledActorsLabel;
    private Text pooledActorsText;
    

	protected void hookListeners() {
		pooledActorsText.addFocusListener(this);
	}
	
	protected void unhookListeners() {
		pooledActorsText.removeFocusListener(this);
	}
	
	protected void clearControls() {
		pooledActorsText.setText("");
	}
	
	protected void updateControls() {
		if (assignable.getAssignment() != null && assignable.getAssignment().getPooledActors() != null) {
			pooledActorsText.setText(assignable.getAssignment().getPooledActors());
		}
	}
	
	protected void setActive(boolean active) {
		Assignment assignment = getAssignment();
		if (active) {
			assignment.setPooledActors(pooledActorsText.getText());
		} else {
			assignment.setPooledActors(null);
		}
	}

	protected void create() {
		Composite composite = widgetFactory.createComposite(parent);
		composite.setLayout(new FormLayout());
		pooledActorsLabel = widgetFactory.createLabel(composite, "Pooled Actors");
		pooledActorsText = widgetFactory.createText(composite, "");
		composite.setLayoutData(createCompositeLayoutData());
		pooledActorsLabel.setLayoutData(createNameLabelLayoutData());
		pooledActorsText.setLayoutData(createNameTextLayoutData());
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
		result.left = new FormAttachment(pooledActorsLabel, 5);
		result.right = new FormAttachment(100, -5);
		result.bottom = new FormAttachment(100, -5);
		return result;		
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (assignable.getAssignment() == null) return;
		assignable.getAssignment().setPooledActors(pooledActorsText.getText());
	}	
	
}
