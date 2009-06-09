package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Script;

public class ScriptDetailsConfigurationComposite implements FocusListener {
	
	public static ScriptDetailsConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		ScriptDetailsConfigurationComposite result = new ScriptDetailsConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Label scriptLabel;
	private Text scriptText;
	
	private Script script;
	
	private ScriptDetailsConfigurationComposite() {}
	
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
		scriptText.addFocusListener(this);
	}
	
	private void unhookSelectionListener() {
		scriptText.removeFocusListener(this);
	}
	
	private void clearControls() {
		scriptText.setText("");
	}
	
	private void updateControls() {
		String str = script.getScript();
		scriptText.setText(str == null ? "" : str);
	}
	
	private void create() {
		scriptLabel = widgetFactory.createLabel(parent, "Script");
        scriptText = widgetFactory.createText(parent, "", SWT.MULTI | SWT.V_SCROLL);
        scriptLabel.setLayoutData(createScriptLabelLayoutData());
        scriptText.setLayoutData(createScriptTextLayoutData());
	}
	
	private FormData createScriptTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 85);
		data.top = new FormAttachment(0, 5);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createScriptLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 7);
		return data;
	}
	
	private String getScriptText() {
		String text = scriptText.getText();
		if ("".equals(text)) {
			text = null;
		}
		return text;
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == scriptText) {
			script.setScript(getScriptText());
		}
	}
	
}
