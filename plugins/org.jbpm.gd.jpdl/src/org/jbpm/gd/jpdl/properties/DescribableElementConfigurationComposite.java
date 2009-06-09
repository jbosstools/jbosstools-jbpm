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
import org.jbpm.gd.jpdl.model.DescribableElement;
import org.jbpm.gd.jpdl.model.Description;

public class DescribableElementConfigurationComposite implements FocusListener {
	
	public static DescribableElementConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		DescribableElementConfigurationComposite result = new DescribableElementConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Text descriptionText;
	private Label descriptionLabel;
	
	private DescribableElement describableElement;
	private Description description;
	
	private DescribableElementConfigurationComposite() {}
	
	public void setDescribableElement(DescribableElement describableElement) {
		if (this.describableElement == describableElement) return;
		unhookSelectionListener();
		clearControls();
		this.describableElement = describableElement;
		if (describableElement != null) {
			updateControls();
			hookSelectionListener();
		}
	}
	
	private void hookSelectionListener() {
		descriptionText.addFocusListener(this);
	}
	
	private void unhookSelectionListener() {
		descriptionText.removeFocusListener(this);
	}
	
	private void clearControls() {
		descriptionText.setText("");
	}
	
	private void updateControls() {
		description = describableElement.getDescription();
		String str = "";
		if (description != null && description.getDescription() != null) {
			str = description.getDescription();
		}
		descriptionText.setText(str);
	}
	
	private void create() {
		descriptionLabel = widgetFactory.createLabel(parent, "Description");
        descriptionText = widgetFactory.createText(parent, "", SWT.MULTI | SWT.V_SCROLL);
        descriptionLabel.setLayoutData(createDescriptionLabelLayoutData());
        descriptionText.setLayoutData(createDescriptionTextLayoutData());
	}
	
	private FormData createDescriptionLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 2);
		return data;
	}
	
	private FormData createDescriptionTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 85);
		data.top = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == descriptionText) {
			String str = descriptionText.getText();
			Description description = describableElement.getDescription();
			if ("".equals(str)) {
				describableElement.setDescription(null);
			} else {
				if (description == null) {
					description = 
						(Description)describableElement.getFactory().createById("org.jbpm.gd.jpdl.description");
					describableElement.setDescription(description);
				}
				description.setDescription(str);
			}
		}
	}
	
}
