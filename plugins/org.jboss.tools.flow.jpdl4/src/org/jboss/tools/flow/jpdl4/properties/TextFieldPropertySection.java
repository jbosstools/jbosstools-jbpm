package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class TextFieldPropertySection extends JpdlPropertySection {

	private Text propertyText;
	private CLabel propertyLabel;
	
	private String name;
	private String label;
	
	public TextFieldPropertySection(String name, String label) {
		this.name = name;
		this.label = label;
	}
	
	private ModifyListener textModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			changeProperty(name, getValueNullsAllowed(propertyText.getText()));
		}
	};
	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getFlatFormComposite();
		createPropertyLabel(composite);
		createPropertyText(composite);
	}
	
	
	private void createPropertyLabel(Composite parent) {
		propertyLabel = getWidgetFactory().createCLabel(parent, label);
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 5);
		propertyLabel.setLayoutData(data);
	}
	
	private void createPropertyText(Composite parent) {
		propertyText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(JpdlPropertySection.SECOND_COLUMN_LEFT_LIMIT, 0);
		data.right = new FormAttachment(100, 0);
		propertyText.setLayoutData(data);
	}

	protected void hookListeners() {
		propertyText.addModifyListener(textModifyListener);
	}

	protected void unhookListeners() {
		propertyText.removeModifyListener(textModifyListener);
	}

	protected void updateValues() {
		IPropertySource input = getInput();
		if (input != null) {
			propertyText.setText(getValueNotNull((String)input.getPropertyValue(name)));
		} else {
			propertyText.setText("");
		}
	}

}
