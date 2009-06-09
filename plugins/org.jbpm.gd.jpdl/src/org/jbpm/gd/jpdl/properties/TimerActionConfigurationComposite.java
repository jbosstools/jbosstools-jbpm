package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.Script;
import org.jbpm.gd.jpdl.model.Timer;

public class TimerActionConfigurationComposite implements SelectionListener {
	
	public static TimerActionConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		TimerActionConfigurationComposite result = new TimerActionConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Label actionTypeLabel;
	private CCombo actionTypeCombo;
	private Composite actionComposite;
	private Composite scriptComposite;
	private ActionConfigurationComposite actionConfigurationComposite;
	private ScriptConfigurationComposite scriptConfigurationComposite;
	
	private Timer timer;
	
	private TimerActionConfigurationComposite() {}
	
	public void setTimer(Timer timer) {
		if (this.timer == timer) return;
		unhookListeners();
		clearControls();
		this.timer = timer;
		if (timer != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		actionTypeCombo.addSelectionListener(this);
	}
	
	private void unhookListeners() {
		actionTypeCombo.removeSelectionListener(this);
	}
	
	private void clearControls() {
		actionTypeCombo.setText("<None>");
		actionComposite.setVisible(false);
		scriptComposite.setVisible(false);
	}
	
	private void updateControls() {
		if (timer.getScript() != null) {
			actionTypeCombo.setText("Script");
			scriptConfigurationComposite.setScript(timer.getScript());
			scriptComposite.setVisible(true);
		} else if (timer.getAction() != null){
			actionTypeCombo.setText("Action");
			actionConfigurationComposite.setAction(timer.getAction());
			actionComposite.setVisible(true);
		} else {
			actionTypeCombo.setText("<None>");
		}
	}
	
	private void create() {
		actionTypeLabel = widgetFactory.createLabel(parent, "Action Type");
		actionTypeCombo = widgetFactory.createCCombo(parent);
		actionTypeCombo.setItems( new String[] {"<None>", "Action", "Script"});
		actionTypeCombo.addSelectionListener(this);
		actionComposite = widgetFactory.createComposite(parent);
		actionComposite.setLayout(new FormLayout());
		actionConfigurationComposite = ActionConfigurationComposite.create(widgetFactory, actionComposite);
		actionComposite.setVisible(false);
		scriptComposite = widgetFactory.createComposite(parent);
		scriptComposite.setLayout(new FormLayout());
		scriptConfigurationComposite = ScriptConfigurationComposite.create(widgetFactory, scriptComposite);
		scriptComposite.setVisible(false);
		actionTypeLabel.setLayoutData(createActionTypeLabelLayoutData());
		actionTypeCombo.setLayoutData(createActionTypeComboLayoutData());
		actionComposite.setLayoutData(createActionCompositeLayoutData());
		scriptComposite.setLayoutData(createActionCompositeLayoutData());
	}
	
	private FormData createActionCompositeLayoutData() {
		FormData data = new FormData();
		data.top = new FormAttachment(actionTypeCombo, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createActionTypeComboLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(actionTypeLabel, 0);
		data.top = new FormAttachment(0, 0);
		return data;
	}
	
	private FormData createActionTypeLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 2);
		return data;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == actionTypeCombo) {
			handleActionTypeComboSelected();
		}
	}
	
	private void handleActionTypeComboSelected() {
		if ("Action".equals(actionTypeCombo.getText())) {
			if (actionConfigurationComposite.getAction() == null) {
				actionConfigurationComposite.setAction(
						(Action)timer.getFactory().createById("org.jbpm.gd.jpdl.action"));
			}
			timer.setScript(null);
			timer.setAction(actionConfigurationComposite.getAction());
		} else if ("Script".equals(actionTypeCombo.getText())) {
			if (scriptConfigurationComposite.getScript() == null) {
				scriptConfigurationComposite.setScript(
						(Script)timer.getFactory().createById("org.jbpm.gd.jpdl.script"));
			}
			timer.setAction(null);
			timer.setScript(scriptConfigurationComposite.getScript());
		} else {
			timer.setScript(null);
			timer.setAction(null);
		}
		actionComposite.setVisible("Action".equals(actionTypeCombo.getText()));
		scriptComposite.setVisible("Script".equals(actionTypeCombo.getText()));
	}

}
