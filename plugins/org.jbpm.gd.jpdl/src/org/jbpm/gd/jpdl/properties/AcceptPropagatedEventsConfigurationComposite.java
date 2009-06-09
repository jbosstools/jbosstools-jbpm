package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.AcceptPropagatedEventsElement;
import org.jbpm.gd.jpdl.util.BooleanTypeHelper;

public class AcceptPropagatedEventsConfigurationComposite implements SelectionListener {
	
	public static AcceptPropagatedEventsConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		AcceptPropagatedEventsConfigurationComposite result = new AcceptPropagatedEventsConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Button acceptPropagatedEventsButton;

	private AcceptPropagatedEventsElement acceptPropagatedEventsElement;
	
	private AcceptPropagatedEventsConfigurationComposite() {}
	
	public void setAcceptPropagatedEventsElement(AcceptPropagatedEventsElement acceptPropagatedEventsElement) {
		if (this.acceptPropagatedEventsElement == acceptPropagatedEventsElement) return;
		unhookSelectionListener();
		clearControls();
		this.acceptPropagatedEventsElement = acceptPropagatedEventsElement;
		if (acceptPropagatedEventsElement != null) {
			updateControls();
			hookSelectionListener();
		}
	}
	
	private void hookSelectionListener() {
		acceptPropagatedEventsButton.addSelectionListener(this);
	}
	
	private void unhookSelectionListener() {
		acceptPropagatedEventsButton.removeSelectionListener(this);
	}
	
	private void clearControls() {
		acceptPropagatedEventsButton.setSelection(false);
	}
	
	private void updateControls() {
		acceptPropagatedEventsButton.setSelection(
				BooleanTypeHelper.booleanValue(acceptPropagatedEventsElement.getAcceptPropagatedEvents()));
	}
	
	private void create() {
		acceptPropagatedEventsButton = widgetFactory.createButton(parent, "Accept Propagated Events", SWT.CHECK); 
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == acceptPropagatedEventsButton) {
			acceptPropagatedEventsElement.setAcceptPropagatedEvents(acceptPropagatedEventsButton.getSelection() ? "true" : "false");
		}
	}
	
}
