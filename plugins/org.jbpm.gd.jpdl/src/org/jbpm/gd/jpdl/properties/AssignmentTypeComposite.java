package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Assignable;
import org.jbpm.gd.jpdl.model.Assignment;

public abstract class AssignmentTypeComposite {
	
	protected TabbedPropertySheetWidgetFactory widgetFactory;
	protected Composite parent;
	protected Assignable assignable;
	
	public void setWidgetFactory(TabbedPropertySheetWidgetFactory factory) {
		widgetFactory = factory;
	}
	
	public void setParent(Composite composite) {
		parent = composite;
	}
	
	public void setAssignable(Assignable assignable) {
		if (this.assignable == assignable) return;
		unhookListeners();
		this.assignable = assignable;
		clearControls();
		if (assignable != null) {
			updateControls();
			hookListeners();
		}
	}
	
	protected abstract void setActive(boolean active);	
	protected abstract void create();
	protected abstract void hookListeners();
	protected abstract void unhookListeners();
	protected abstract void clearControls();
	protected abstract void updateControls();

	protected Assignment getAssignment() {
		Assignment assignment = assignable.getAssignment();
		if (assignment == null) {
			assignment = (Assignment)assignable.getFactory().createById("org.jbpm.gd.jpdl.assignment");
			assignable.setAssignment(assignment);
		}
		return assignment;
	}
	
}
