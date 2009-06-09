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
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Event;

public class EventConfigurationComposite implements SelectionListener, FocusListener {
	
	public static EventConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		EventConfigurationComposite result = new EventConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Label eventTypeLabel;
	private CCombo eventTypeCombo;
	
	private Event event;
	
	private EventConfigurationComposite() {}
	
	public void setEvent(Event event) {
		if (this.event == event) return;
		unhookListeners();
		this.event = event;
		if (event == null) {
			clearControls();
		} else {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		eventTypeCombo.addSelectionListener(this);
		eventTypeCombo.addFocusListener(this);
	}
	
	private void unhookListeners() {
		eventTypeCombo.removeSelectionListener(this);
		eventTypeCombo.removeSelectionListener(this);
	}
	
	private void clearControls() {
		eventTypeCombo.setText("");
	}
	
	private void updateControls() {
		String type = event.getType();
		if (type != null) {
			eventTypeCombo.setText(type);
		} else {
			eventTypeCombo.setText("");
		}
	}
	
	private void create() {
		eventTypeLabel = widgetFactory.createLabel(parent, "Event Type");
		eventTypeCombo = widgetFactory.createCCombo(parent);
		eventTypeCombo.setItems(Event.PREDEFINED_EVENT_TYPES);
		eventTypeCombo.setText("");
		eventTypeLabel.setLayoutData(createEventTypeLabelLayoutData());
		eventTypeCombo.setLayoutData(createEventTypeComboLayoutData());
	}
	
	private FormData createEventTypeComboLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(eventTypeLabel, 0);
		data.top = new FormAttachment(0, 0);
		return data;
	}
	
	private FormData createEventTypeLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 3);
		return data;
	}
	

	public void widgetDefaultSelected(SelectionEvent e) {
		if (e.widget == eventTypeCombo) {
			event.setType(eventTypeCombo.getText());
		}
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == eventTypeCombo) {
			event.setType(eventTypeCombo.getText());
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == eventTypeCombo) {
			event.setType(eventTypeCombo.getText());
		}
	}
	
}
