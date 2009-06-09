package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Script;
import org.jbpm.gd.jpdl.util.BooleanTypeHelper;

public class ScriptAdvancedConfigurationComposite implements SelectionListener {
	
	public static ScriptAdvancedConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		ScriptAdvancedConfigurationComposite result = new ScriptAdvancedConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Button acceptPropagatedEventsButton;
	
	private Script script;
	
	private ScriptAdvancedConfigurationComposite() {}
	
	public void setScript(Script script) {
		if (this.script == script) return;
		unhookSelectionListener();
		clearControls();
		this.script = script;
		if (script != null) {
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
		acceptPropagatedEventsButton.setSelection(BooleanTypeHelper.booleanValue(script.getAcceptPropagatedEvents()));
	}
	
	private void create() {
        acceptPropagatedEventsButton = widgetFactory.createButton(parent, "Accept Propagated Events", SWT.CHECK);
        acceptPropagatedEventsButton.setLayoutData(createAcceptPropagatedEventsButtonLayoutData());
	}
	
	private FormData createAcceptPropagatedEventsButtonLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 0);
		return data;
	}
	
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == acceptPropagatedEventsButton) {
			script.setAcceptPropagatedEvents(acceptPropagatedEventsButton.getSelection() ? "true" : "false");
		}
	}

}
