package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.gef.commands.CommandStack;
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

public class TimerSection extends JpdlPropertySection {

	private Text dueDateText;
	private CLabel dueDateLabel;
	private Text repeatText;
	private CLabel repeatLabel;
	private Text dueDateTimeText;
	private CLabel dueDateTimeLabel;
	
	private ModifyListener textModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent event) {
			IPropertySource input = getInput();
			CommandStack commandStack = getCommandStack();
			if (input == null || commandStack == null) return;
			if (dueDateText == event.getSource()) {
				changeProperty("dueDate", getValueNullsAllowed(dueDateText.getText()));
			} else if (repeatText == event.getSource()) {
				changeProperty("repeat", getValueNullsAllowed(repeatText.getText()));
			} else if (dueDateTimeText == event.getSource()) {
				changeProperty("dueDateTime", getValueNullsAllowed(dueDateTimeText.getText()));
			}
		}
	};
	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getFlatFormComposite();
		createDueDateLabel(composite);
		createDueDateText(composite);
		createRepeatLabel(composite);
		createRepeatText(composite);
		createDueDateTimeLabel(composite);
		createDueDateTimeText(composite);
	}
	
	
	private void createDueDateLabel(Composite parent) {
		dueDateLabel = getWidgetFactory().createCLabel(parent, "Due Date (e.g. '3 business hours')             ");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 5);
		dueDateLabel.setLayoutData(data);
	}
	
	private void createDueDateText(Composite parent) {
		dueDateText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(dueDateLabel, 0, SWT.RIGHT);
		data.right = new FormAttachment(100, 0);
		dueDateText.setLayoutData(data);
	}

	private void createRepeatLabel(Composite parent) {
		repeatLabel = getWidgetFactory().createCLabel(parent, "Repeat (e.g. '30 minutes')");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(dueDateText, 5);
		repeatLabel.setLayoutData(data);
	}
	
	private void createRepeatText(Composite parent) {
		repeatText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(dueDateText, 0);
		data.left = new FormAttachment(dueDateLabel, 0, SWT.RIGHT);
		data.right = new FormAttachment(100, 0);
		repeatText.setLayoutData(data);
	}

	private void createDueDateTimeLabel(Composite parent) {
		dueDateTimeLabel = getWidgetFactory().createCLabel(parent, "Due Date-time (e.g. 'HH:mm dd/MM/yyyy')");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(repeatText, 5);
		dueDateTimeLabel.setLayoutData(data);
	}
	
	private void createDueDateTimeText(Composite parent) {
		dueDateTimeText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(repeatText, 0);
		data.left = new FormAttachment(dueDateLabel, 0, SWT.RIGHT);
		data.right = new FormAttachment(100, 0);
		dueDateTimeText.setLayoutData(data);
	}

	protected void hookListeners() {
		dueDateText.addModifyListener(textModifyListener);
		repeatText.addModifyListener(textModifyListener);
		dueDateTimeText.addModifyListener(textModifyListener);
	}

	protected void unhookListeners() {
		dueDateText.removeModifyListener(textModifyListener);
		repeatText.removeModifyListener(textModifyListener);
		dueDateTimeText.removeModifyListener(textModifyListener);
	}

	protected void updateValues() {
		IPropertySource input = getInput();
		if (input != null) {
			dueDateText.setText(getValueNotNull((String)input.getPropertyValue("dueDate")));
			repeatText.setText(getValueNotNull((String)input.getPropertyValue("repeat")));
			dueDateTimeText.setText(getValueNotNull((String)input.getPropertyValue("dueDateTime")));
		} else {
			dueDateText.setText("");
			repeatText.setText("");
			dueDateTimeText.setText("");
		}
	}


}
