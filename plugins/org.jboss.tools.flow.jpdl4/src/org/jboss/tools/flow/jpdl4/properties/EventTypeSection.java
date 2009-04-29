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
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;

public class EventTypeSection extends JpdlPropertySection {

	private Text eventTypeText;
	private CLabel eventTypeLabel;
	
	private ModifyListener textModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			changeProperty(EventListenerContainer.EVENT_TYPE, getValueNullsAllowed(eventTypeText.getText()));
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
		eventTypeLabel = getWidgetFactory().createCLabel(parent, "Event Type");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 5);
		eventTypeLabel.setLayoutData(data);
	}
	
	private void createEventTypeText(Composite parent) {
		eventTypeText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(eventTypeLabel, 0, SWT.RIGHT);
		data.right = new FormAttachment(100, 0);
		eventTypeText.setLayoutData(data);
	}

	protected void hookListeners() {
		eventTypeText.addModifyListener(textModifyListener);
	}

	protected void unhookListeners() {
		eventTypeText.removeModifyListener(textModifyListener);
	}

	protected void updateValues() {
		IPropertySource input = getInput();
		if (input != null) {
			eventTypeText.setText(getValueNotNull((String)input.getPropertyValue(EventListenerContainer.EVENT_TYPE)));
		} else {
			eventTypeText.setText("");
		}
	}

}
