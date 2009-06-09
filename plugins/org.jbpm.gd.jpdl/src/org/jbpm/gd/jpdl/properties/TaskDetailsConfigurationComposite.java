package org.jbpm.gd.jpdl.properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
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
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Task;
import org.jbpm.gd.jpdl.util.BooleanTypeHelper;

public class TaskDetailsConfigurationComposite implements SelectionListener, FocusListener {
	
	public static TaskDetailsConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		TaskDetailsConfigurationComposite result = new TaskDetailsConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
		
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;	
    private Task task;

    private Label duedateLabel;
    private Text duedateText;
    private Label priorityLabel;
    private CCombo priorityCombo;
    private Button blockingButton;
    private Button signallingButton;
    private Button notifyButton;
    private Button formButton;
	
	private TaskDetailsConfigurationComposite() {}
	
	public void setTask(Task task) {
		if (this.task == task) return;
		unhookListeners();
		this.task = task;
		clearControls();
		if (task != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		duedateText.addFocusListener(this);
		priorityCombo.addFocusListener(this);
		priorityCombo.addSelectionListener(this);
		blockingButton.addSelectionListener(this);
		signallingButton.addSelectionListener(this);
		notifyButton.addSelectionListener(this);
		formButton.addSelectionListener(this);
	}
	
	private void unhookListeners() {
		duedateText.removeFocusListener(this);
		priorityCombo.removeFocusListener(this);
		priorityCombo.removeSelectionListener(this);
		blockingButton.removeSelectionListener(this);
		signallingButton.removeSelectionListener(this);
		notifyButton.removeSelectionListener(this);
		formButton.removeSelectionListener(this);
	}
	
	private void clearControls() {
		duedateText.setText("");
		priorityCombo.setText("");
		blockingButton.setSelection(false);
		signallingButton.setSelection(false);
		notifyButton.setSelection(false);
	}
	
	private void updateControls() {
		duedateText.setText(task.getDueDate() != null ? task.getDueDate() : "");
		priorityCombo.setText(task.getPriority());
		blockingButton.setSelection(BooleanTypeHelper.booleanValue(task.getBlocking()));
		signallingButton.setSelection(BooleanTypeHelper.booleanValue(task.getSignalling()));
		notifyButton.setSelection(BooleanTypeHelper.booleanValue(task.getNotify()));		
	}
	
	private void create() {
		duedateLabel = widgetFactory.createLabel(parent, "Due Date");
		duedateText = widgetFactory.createText(parent, "");
		priorityLabel = widgetFactory.createLabel(parent, "Priority");
		priorityCombo = widgetFactory.createCCombo(parent);
		priorityCombo.setItems(getPriorityItems());
		priorityCombo.setEditable(true);
		blockingButton = widgetFactory.createButton(parent, "Blocking", SWT.CHECK);
		signallingButton = widgetFactory.createButton(parent, "Signalling", SWT.CHECK);
		notifyButton = widgetFactory.createButton(parent, "Notify", SWT.CHECK);
		formButton = widgetFactory.createButton(parent, "Generate Form...", SWT.PUSH);
		duedateLabel.setLayoutData(createDuedateLabelLayoutData());
		duedateText.setLayoutData(createDuedateTextLayoutData());
		priorityLabel.setLayoutData(createPriorityLabelLayoutData());
		priorityCombo.setLayoutData(createPriorityComboLayoutData());
		blockingButton.setLayoutData(createBlockingButtonLayoutData());
		signallingButton.setLayoutData(createSignallingButtonLayoutData());
		notifyButton.setLayoutData(createNotifyButtonLayoutData());
		formButton.setLayoutData(createFormButtonLayoutData());
	}
	
	private String[] getPriorityItems() {
		return new String[] {"highest", "high", "normal", "low", "lowest"};
	}
	
	private FormData createDuedateLabelLayoutData() {
		FormData data = new FormData();
		data.top = new FormAttachment(0, 2);
		data.left = new FormAttachment(0, 0);
		return data;
	}
	
	private FormData createDuedateTextLayoutData() {
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(duedateLabel, 0);
		data.right = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createPriorityLabelLayoutData() {
		FormData data = new FormData();
		data.top = new FormAttachment(duedateText, 2);
		data.left = new FormAttachment(0, 0);
		return data;
	}

	private FormData createPriorityComboLayoutData() {
		FormData data = new FormData();
		data.top =  new FormAttachment(duedateText, 0);
		data.left = new FormAttachment(duedateLabel, 0);
		data.right = new FormAttachment(blockingButton);
		return data;
	}

	private FormData createBlockingButtonLayoutData() {
		FormData data = new FormData();
		data.top = new FormAttachment(duedateText, 2);
		data.right = new FormAttachment(signallingButton, 0);
		return data;
	}

	private FormData createSignallingButtonLayoutData() {
		FormData data = new FormData();
		data.top = new FormAttachment(duedateText, 2);
		data.right = new FormAttachment(notifyButton, 0);
		return data;
	}

	private FormData createNotifyButtonLayoutData() {
		FormData data = new FormData();
		data.top = new FormAttachment(duedateText, 2);
		data.right = new FormAttachment(100, 0);
		return data;
	}

	private FormData createFormButtonLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(priorityCombo, 0);
		return data;
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.widget == formButton) {
			handleFormButtonSelected();
		} else if (e.widget == priorityCombo) {
			handlePriorityComboEdited();
		} else if (e.widget == blockingButton) {
			handleBlockingButtonSelected();
		} else if (e.widget == signallingButton) {
			handleSignallingButtonSelected();
		} else if (e.widget == notifyButton) {
			handleNotifyButtonSelected();
		}
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		handleEdited(e.widget);
	}	
	
	private void handleFormButtonSelected() {	
		String name = task.getName();
		if (name == null || "".equals(name)) {
			new MessageDialog(
					null, 
					"Unnamed Task", 
					null, 
					"The task for which you want to generate a form has no name. " +
					"Please return to the 'Name' field on the 'General' properties " +
					"tab for this task and enter a name before trying to generate " +
					"the form again.",
					MessageDialog.INFORMATION,
					new String[] {"OK"},
					0).open();
		} else {
			new TaskFormGenerationDialog(parent.getShell(), name).open();
		}
	}
	
	private void handleBlockingButtonSelected() {
		if (blockingButton.getSelection()) {
			task.setBlocking("true");
		} else {
			task.setBlocking("false");
		}
	}
	
	private void handleSignallingButtonSelected() {
		if (signallingButton.getSelection()) {
			task.setSignalling("true");
		} else {
			task.setSignalling("false");
		}
	}
	
	private void handleNotifyButtonSelected() {
		if (notifyButton.getSelection()) {
			task.setNotify("true");
		} else {
			task.setNotify("false");
		}
	}
	
	private void handleEdited(Widget widget) {
		if (widget == duedateText) {
			task.setDueDate("".equals(duedateText.getText()) ? null : duedateText.getText());
		} else if (widget == priorityCombo) {
			handlePriorityComboEdited();
		}
	}
	
	private void handlePriorityComboEdited() {
		task.setPriority(priorityCombo.getText());
	}
	
}
