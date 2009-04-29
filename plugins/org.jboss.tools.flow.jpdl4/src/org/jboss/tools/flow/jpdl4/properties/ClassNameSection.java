package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.flow.jpdl4.model.EventListener;

public class ClassNameSection extends JpdlPropertySection {

	private Text classNameText;
	private CLabel classNameLabel;
	
	private ModifyListener textModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			changeProperty(EventListener.CLASS_NAME, getValueNullsAllowed(classNameText.getText()));
		}
	};
	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getFlatFormComposite();
		createEventTypeLabel(composite);
		createEventTypeText(composite);
	}
	
	
	private void createEventTypeLabel(Composite parent) {
		classNameLabel = getWidgetFactory().createCLabel(parent, "Class");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 5);
		classNameLabel.setLayoutData(data);
	}
	
	private void createEventTypeText(Composite parent) {
		classNameText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(classNameLabel, 0, SWT.RIGHT);
		data.right = new FormAttachment(100, 0);
		classNameText.setLayoutData(data);
	}

	protected void hookListeners() {
		classNameText.addModifyListener(textModifyListener);
	}

	protected void unhookListeners() {
		classNameText.removeModifyListener(textModifyListener);
	}

	protected void updateValues() {
		IPropertySource input = getInput();
		if (input != null) {
			classNameText.setText(getValueNotNull((String)input.getPropertyValue(EventListener.CLASS_NAME)));
		} else {
			classNameText.setText("");
		}
	}

}
