package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Reminder;
import org.jbpm.gd.jpdl.model.Task;

public class TaskReminderConfigurationComposite implements FocusListener, SelectionListener {
	
	public static TaskReminderConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		TaskReminderConfigurationComposite result = new TaskReminderConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Button reminderButton;
	private Composite reminderComposite;
	private Label duedateLabel;
	private Text duedateText;
	private Label repeatLabel;
	private Text repeatText;
	
	private Task task;
	
	public void setTask(Task task) {
		if (this.task == task) return;
		unhookListeners();
		clearControls();
		this.task = task;
		if (task != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		reminderButton.addSelectionListener(this);
		duedateText.addFocusListener(this);
		repeatText.addFocusListener(this);
	}
	
	private void unhookListeners() {
		reminderButton.removeSelectionListener(this);
		duedateText.removeFocusListener(this);
		repeatText.removeFocusListener(this);
	}
	
	private void clearControls() {
		reminderButton.setSelection(false);
		duedateText.setText("");
		repeatText.setText("");
		reminderComposite.setVisible(false);
	}
	
	private void updateControls() {
		reminderButton.setSelection(task.getReminder() != null);
		duedateText.setText(getDueDateText());
		repeatText.setText(getRepeatText());
		reminderComposite.setVisible(reminderButton.getSelection());
	}

	private String getDueDateText() {
		String result = "";
		if (task.getReminder() != null && task.getReminder().getDueDate() != null) {
			result = task.getReminder().getDueDate();
		}
		return result;
	}
	
	private String getRepeatText() {
		String result ="";
		if (task.getReminder() != null && task.getReminder().getRepeat() != null) {
			result = task.getReminder().getRepeat();
		}
		return result;
	}
	
	private void create() {
		reminderButton = widgetFactory.createButton(parent, "Configure Reminder", SWT.CHECK);
		reminderComposite = widgetFactory.createFlatFormComposite(parent);
        duedateLabel = widgetFactory.createLabel(reminderComposite, "Due Date");
        duedateText = widgetFactory.createText(reminderComposite, "");
        repeatLabel = widgetFactory.createLabel(reminderComposite, "Repeat");
        repeatText = widgetFactory.createText(reminderComposite, "");
        reminderButton.setLayoutData(createReminderButtonLayoutData());
        reminderComposite.setLayoutData(createReminderCompositeLayoutData());
        duedateLabel.setLayoutData(createDuedateLabelLayoutData());
        duedateText.setLayoutData(createDuedateTextLayoutData());
        repeatLabel.setLayoutData(createRepeatLabelLayoutData());
        repeatText.setLayoutData(createRepeatTextLayoutData());
	}
	
	private FormData createReminderButtonLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 2);
		return data;
	}
	
	private FormData createReminderCompositeLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, -3);
		data.top = new FormAttachment(reminderButton, -3);
		data.right = new FormAttachment(100, 3);
		return data;
	}
	
	private FormData createDuedateLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 2);
		return data;
	}
	
	private FormData createDuedateTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(duedateLabel, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		return data;
	}
	
	private FormData createRepeatLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(duedateText, 2);
		return data;
	}
	
	private FormData createRepeatTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(duedateLabel, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(duedateText, 0);
		return data;
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == duedateText) {
			task.getReminder().setDueDate(duedateText.getText());
		} else if (e.widget == repeatText) {
			task.getReminder().setRepeat("".equals(repeatText.getText()) ? null : repeatText.getText());
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == reminderButton) {
			if (reminderButton.getSelection()) {
				Reminder reminder = task.getReminder();
				if (reminder == null) {
					reminder = (Reminder)task.getFactory().createById("org.jbpm.gd.jpdl.reminder");
					task.setReminder(reminder);
				}
				reminder.setDueDate(duedateText.getText());
				reminder.setRepeat("".equals(repeatText.getText()) ? null : repeatText.getText());
			} else {
				task.setReminder(null);
			}
			reminderComposite.setVisible(reminderButton.getSelection());
		}
	}

}
