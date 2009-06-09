package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.EsbElement;

public class EsbGeneralConfigurationComposite implements FocusListener, SelectionListener {
	
	public static EsbGeneralConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		EsbGeneralConfigurationComposite result = new EsbGeneralConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Label serviceNameLabel;
	private Text serviceNameText;
	private Label categoryNameLabel;
	private Text categoryNameText;
	private Label replyToFaultToLabel;
	private CCombo replyToFaultToCombo;
	
	private EsbElement esbElement;
	
	private EsbGeneralConfigurationComposite() {}
	
	public void setEsbElement(EsbElement esbElement) {
		if (this.esbElement == esbElement) return;
		unhookListeners();
		clearControls();
		this.esbElement = esbElement;
		if (esbElement != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		serviceNameText.addFocusListener(this);
		categoryNameText.addFocusListener(this);
		replyToFaultToCombo.addSelectionListener(this);
	}
	
	private void unhookListeners() {
		serviceNameText.removeFocusListener(this);
		categoryNameText.removeFocusListener(this);
		replyToFaultToCombo.removeSelectionListener(this);
	}
	
	private void clearControls() {
		serviceNameText.setText("");
		categoryNameText.setText("");
		replyToFaultToCombo.setText("");
	}
	
	private void updateControls() {
		serviceNameText.setText(esbElement.getServiceName() == null ? "" : esbElement.getServiceName());
		categoryNameText.setText(esbElement.getCategoryName() == null ? "" : esbElement.getCategoryName());
		replyToFaultToCombo.setText(esbElement.getReplyToOriginator() == null ? "" : esbElement.getReplyToOriginator());
	}
	
	private void create() {
		serviceNameLabel = widgetFactory.createLabel(parent, "Service");
        serviceNameText = widgetFactory.createText(parent, "");
        categoryNameLabel = widgetFactory.createLabel(parent, "Category");
        categoryNameText = widgetFactory.createText(parent, "");
        replyToFaultToLabel = widgetFactory.createLabel(parent, "Reply/Fault");
        replyToFaultToCombo = widgetFactory.createCCombo(parent);
        replyToFaultToCombo.add("");
        replyToFaultToCombo.add("reply");
        replyToFaultToCombo.add("fault");
        replyToFaultToCombo.setText("");
        serviceNameLabel.setLayoutData(createServiceNameLabelLayoutData());
        serviceNameText.setLayoutData(createServiceNameTextLayoutData());
        categoryNameLabel.setLayoutData(createCategoryNameLabelLayoutData());
        categoryNameText.setLayoutData(createCategoryNameTextLayoutData());
        replyToFaultToLabel.setLayoutData(createReplyToFaultToLabelLayoutData());
        replyToFaultToCombo.setLayoutData(createReplyToFaultToComboLayoutData());
	}
	
	private FormData createReplyToFaultToLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(categoryNameText, 5);
		return data;
	}
	
	private FormData createReplyToFaultToComboLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(replyToFaultToLabel, 5);
		data.top = new FormAttachment(categoryNameText, 0);
		return data;
	}
	
	private FormData createServiceNameTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(replyToFaultToLabel, 5);
		data.top = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createCategoryNameTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(replyToFaultToLabel, 5);
		data.top = new FormAttachment(serviceNameText, 0);
		data.right = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createServiceNameLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 5);
		return data;
	}
	
	private FormData createCategoryNameLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(serviceNameText, 5);
		return data;
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == serviceNameText) {
			updateServiceName();
		} else if (e.widget == categoryNameText) {
			updateCategoryName();
		}
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == replyToFaultToCombo) {
			updateReplyToFaultTo();
		}
	}

	private void updateServiceName() {
		String name = serviceNameText.getText();
		if ("".equals(name)) {
			name = null;
		}
		esbElement.setServiceName(name);
	}
	
	private void updateCategoryName() {
		String name = categoryNameText.getText();
		if ("".equals(name)) {
			name = null;
		}
		esbElement.setCategoryName(name);
	}
	
	private void updateReplyToFaultTo() {
		String value = replyToFaultToCombo.getText();
		if ("".equals(value)) {
			value = null;
		}
		esbElement.setReplyToOriginator(value);
	}

}
