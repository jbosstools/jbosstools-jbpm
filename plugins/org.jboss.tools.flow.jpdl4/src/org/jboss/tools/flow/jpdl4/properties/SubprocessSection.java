package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.flow.jpdl4.model.SubprocessTask;

public class SubprocessSection extends JpdlPropertySection {

	private Text idText;
	private CLabel idLabel;
	private Text keyText;
	private CLabel keyLabel;
	private Text outcomeText;
	private CLabel outcomeLabel;
	
	private ModifyListener textModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent event) {
			IPropertySource input = getInput();
			CommandStack commandStack = getCommandStack();
			if (input == null || commandStack == null) return;
			if (idText == event.getSource()) {
				changeProperty(SubprocessTask.ID, getValueNullsAllowed(idText.getText()));
			} else if (keyText == event.getSource()) {
				changeProperty(SubprocessTask.KEY, getValueNullsAllowed(keyText.getText()));
			} else if (outcomeText == event.getSource()) {
				changeProperty(SubprocessTask.OUTCOME, getValueNullsAllowed(outcomeText.getText()));
			}
		}
	};
	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getFlatFormComposite();
		createIdLabel(composite);
		createIdText(composite);
		createKeyLabel(composite);
		createKeyText(composite);
		createOutcomeLabel(composite);
		createOutcomeText(composite);
	}
	
	
	private void createIdLabel(Composite parent) {
		idLabel = getWidgetFactory().createCLabel(parent, "Id         ");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 5);
		idLabel.setLayoutData(data);
	}
	
	private void createIdText(Composite parent) {
		idText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(JpdlPropertySection.SECOND_COLUMN_LEFT_LIMIT, 0);
		data.right = new FormAttachment(100, 0);
		idText.setLayoutData(data);
	}

	private void createKeyLabel(Composite parent) {
		keyLabel = getWidgetFactory().createCLabel(parent, "Key");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(idText, 5);
		keyLabel.setLayoutData(data);
	}
	
	private void createKeyText(Composite parent) {
		keyText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(idText, 0);
		data.left = new FormAttachment(JpdlPropertySection.SECOND_COLUMN_LEFT_LIMIT, 0);
		data.right = new FormAttachment(100, 0);
		keyText.setLayoutData(data);
	}

	private void createOutcomeLabel(Composite parent) {
		outcomeLabel = getWidgetFactory().createCLabel(parent, "Outcome");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(keyText, 5);
		outcomeLabel.setLayoutData(data);
	}
	
	private void createOutcomeText(Composite parent) {
		outcomeText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(keyText, 0);
		data.left = new FormAttachment(JpdlPropertySection.SECOND_COLUMN_LEFT_LIMIT, 0);
		data.right = new FormAttachment(100, 0);
		outcomeText.setLayoutData(data);
	}

	protected void hookListeners() {
		idText.addModifyListener(textModifyListener);
		keyText.addModifyListener(textModifyListener);
		outcomeText.addModifyListener(textModifyListener);
	}

	protected void unhookListeners() {
		idText.removeModifyListener(textModifyListener);
		keyText.removeModifyListener(textModifyListener);
		outcomeText.removeModifyListener(textModifyListener);
	}

	protected void updateValues() {
		IPropertySource input = getInput();
		if (input != null) {
			idText.setText(getValueNotNull((String)input.getPropertyValue(SubprocessTask.ID)));
			keyText.setText(getValueNotNull((String)input.getPropertyValue(SubprocessTask.KEY)));
			outcomeText.setText(getValueNotNull((String)input.getPropertyValue(SubprocessTask.OUTCOME)));
		} else {
			idText.setText("");
			keyText.setText("");
			outcomeText.setText("");
		}
	}


}
