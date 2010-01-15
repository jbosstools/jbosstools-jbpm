package org.jbpm.gd.common.properties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.common.model.NamedElement;

public class NamedElementConfigurationComposite implements FocusListener, PropertyChangeListener {
	
	public static NamedElementConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		NamedElementConfigurationComposite result = new NamedElementConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Label nameLabel;
	private Text nameText;
	
	private NamedElement namedElement;
	
	private NamedElementConfigurationComposite() {}
	
	public void setNamedElement(NamedElement namedElement) {
		if (this.namedElement == namedElement) return;
		unhookListeners();
		clearControls();
		this.namedElement = namedElement;
		updateControls();
		hookListeners();
	}
	
	private void hookListeners() {
		nameText.addFocusListener(this);
		if (namedElement != null) {
			namedElement.addPropertyChangeListener(this);
		}
	}
	
	private void unhookListeners() {
		nameText.removeFocusListener(this);
		if (namedElement != null) {
			namedElement.removePropertyChangeListener(this);
		}
	}
	
	private void clearControls() {
		nameText.setText("");
	}
	
	private void updateControls() {
		if (namedElement != null) {
			String name = namedElement.getName();
			nameText.setText(name == null ? "" : name);
		}
	}
	
	private void create() {
		nameLabel = widgetFactory.createLabel(parent, "Name");
        nameText = widgetFactory.createText(parent, "");
        nameLabel.setLayoutData(createNameLabelLayoutData());
        nameText.setLayoutData(createNameTextLayoutData());
	}
	
	private FormData createNameTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 85);
		data.top = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createNameLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 2);
		return data;
	}
	
	private String getNameText() {
		String text = nameText.getText().trim();
		if ("".equals(text) && !namedElement.isNameMandatory()) {
			text = null;
		}
		return text;
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == nameText) {
			namedElement.setName(getNameText());
		}
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if ("name".equals(evt.getPropertyName())) {
			nameText.setText(evt.getNewValue() != null ? (String)evt.getNewValue() : "");
		}
	}			
}
