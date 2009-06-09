package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.jbpm.gd.common.model.GenericElement;
import org.jbpm.gd.jpdl.dialog.ChooseDelegationClassDialog;
import org.jbpm.gd.jpdl.model.Assignment;

public class AssignmentHandlerInfoComposite extends AssignmentTypeComposite implements SelectionListener, FocusListener {
	
	private DelegationConfigurationComposite delegationConfigurationComposite;
    
	protected void hookListeners() {
	}
	
	protected void unhookListeners() {
	}
	
	protected void clearControls() {
		delegationConfigurationComposite.setDelegation(null);
	}
	
	protected void updateControls() {
		if (assignable.getAssignment() == null) return;
		delegationConfigurationComposite.setDelegation(assignable.getAssignment());
	}
	
	protected void setActive(boolean active) {
		Assignment assignment = getAssignment();
		if (active) {
			if (assignment.getClassName() == null) {
				assignment.setClassName("");
			}
			delegationConfigurationComposite.setDelegation(assignment);
		} else {
			delegationConfigurationComposite.setDelegation(null);
			assignment.setClassName(null);
			GenericElement[] genericElements = assignment.getGenericElements();
			for (int i = 0; i < genericElements.length; i++) {
				assignment.removeGenericElement(genericElements[i]);
			}			
		}
	}

	protected void create() {
		Composite composite = widgetFactory.createFlatFormComposite(parent);
		delegationConfigurationComposite = DelegationConfigurationComposite.create(widgetFactory, composite, createChooseAssignmentHandlerClassDialog());
		composite.setLayoutData(createCompositeLayoutData());
	}
	
	private ChooseDelegationClassDialog createChooseAssignmentHandlerClassDialog() {
		return new ChooseDelegationClassDialog(
				parent.getShell(), 
				"org.jbpm.taskmgmt.def.AssignmentHandler",
				"Choose Assignment Handler",
				"Choose an assignment handler from the list");
	}
	
	private FormData createCompositeLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.height = 100;
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	public void widgetSelected(SelectionEvent e) {
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
	}	
	
}
