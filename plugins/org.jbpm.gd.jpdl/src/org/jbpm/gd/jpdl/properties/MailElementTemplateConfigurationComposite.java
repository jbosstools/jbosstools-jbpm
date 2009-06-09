package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.MailElement;

public class MailElementTemplateConfigurationComposite implements FocusListener {
	
	public static MailElementTemplateConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		MailElementTemplateConfigurationComposite result = new MailElementTemplateConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	private MailElement mailElement;
	
	private Label templateLabel;
	private Text templateText;
	
	public void setMailElement(MailElement mailElement) {
		if (this.mailElement == mailElement) return;
		unhookSelectionListener();
		clearControls();
		this.mailElement = mailElement;
		if (mailElement != null) {
			updateControls();
			hookSelectionListener();
		}
	}
	
	private void hookSelectionListener() {
		templateText.addFocusListener(this);
	}
	
	private void unhookSelectionListener() {
		templateText.removeFocusListener(this);
	}
	
	private void clearControls() {
		templateText.setText("");
	}
	
	private void updateControls() {
		templateText.setText(mailElement.getTemplate() == null ? "" : mailElement.getTemplate());
	}
	
	private void create() {
        templateLabel = widgetFactory.createLabel(parent, "Template");
        templateText = widgetFactory.createText(parent, "");
         templateLabel.setLayoutData(createTemplateLabelLayoutData());
        templateText.setLayoutData(createTemplateTextLayoutData());
	}
	
	private FormData createTemplateLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 2);
		return data;
	}
	
	private FormData createTemplateTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 85);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		return data;
	}
		
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == templateText) {
			mailElement.setTemplate("".equals(templateText.getText()) ? null : templateText.getText());
		}
	}

}
