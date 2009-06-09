package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.util.BooleanTypeHelper;

public class ActionAdvancedConfigurationComposite implements SelectionListener {
	
	public static ActionAdvancedConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		ActionAdvancedConfigurationComposite result = new ActionAdvancedConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Button asyncButton;
	private Button acceptPropagatedEventsButton;
	
	private Action action;
	
	private ActionAdvancedConfigurationComposite() {}
	
	public void setAction(Action action) {
		if (this.action == action) return;
		unhookSelectionListener();
		clearControls();
		this.action = action;
		if (action != null) {
			updateControls();
			hookSelectionListener();
		}
	}
	
	private void hookSelectionListener() {
		asyncButton.addSelectionListener(this);
		acceptPropagatedEventsButton.addSelectionListener(this);
	}
	
	private void unhookSelectionListener() {
		asyncButton.removeSelectionListener(this);
		acceptPropagatedEventsButton.removeSelectionListener(this);
	}
	
	private void clearControls() {
		acceptPropagatedEventsButton.setSelection(false);
		asyncButton.setSelection(false);
	}
	
	private void updateControls() {
		acceptPropagatedEventsButton.setSelection(BooleanTypeHelper.booleanValue(action.getAcceptPropagatedEvents()));
		asyncButton.setSelection(BooleanTypeHelper.booleanValue(action.getAsync()));
	}
	
	private void create() {
        asyncButton = widgetFactory.createButton(parent, "Asynchronous", SWT.CHECK);
        acceptPropagatedEventsButton = widgetFactory.createButton(parent, "Accept Propagated Events", SWT.CHECK);
        asyncButton.setLayoutData(createAsyncButtonLayoutData());
        acceptPropagatedEventsButton.setLayoutData(createAcceptPropagatedEventsButtonLayoutData());
	}
	
	private FormData createAcceptPropagatedEventsButtonLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 0);
		return data;
	}
	
	private FormData createAsyncButtonLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(acceptPropagatedEventsButton, 0);
		return data;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == asyncButton) {
			action.setAsync(asyncButton.getSelection() ? "true" : "false");
		} else if (e.widget == acceptPropagatedEventsButton) {
			action.setAcceptPropagatedEvents(acceptPropagatedEventsButton.getSelection() ? "true" : "false");
		}
	}

}
