package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Timer;

public class TimerGeneralConfigurationComposite implements FocusListener {
	
	public static TimerGeneralConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		TimerGeneralConfigurationComposite result = new TimerGeneralConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Label nameLabel;
	private Text nameText;
	private Label transitionLabel;
	private Text transitionText;
	private Label duedateLabel;
	private Text duedateText;
	private Label repeatLabel;
	private Text repeatText;
	
	private Timer timer;
	
	public void setTimer(Timer timer) {
		if (this.timer == timer) return;
		unhookSelectionListener();
		clearControls();
		this.timer = timer;
		if (timer != null) {
			updateControls();
			hookSelectionListener();
		}
	}
	
	private void hookSelectionListener() {
		nameText.addFocusListener(this);
		transitionText.addFocusListener(this);
		duedateText.addFocusListener(this);
		repeatText.addFocusListener(this);
	}
	
	private void unhookSelectionListener() {
		nameText.removeFocusListener(this);
		transitionText.removeFocusListener(this);
		duedateText.removeFocusListener(this);
		repeatText.removeFocusListener(this);
	}
	
	private void clearControls() {
		nameText.setText("");
		transitionText.setText("");
		duedateText.setText("");
		repeatText.setText("");
	}
	
	private void updateControls() {
		nameText.setText(timer.getName() == null ? "" : timer.getName());
		transitionText.setText(timer.getTransition() == null ? "" : timer.getTransition());
		duedateText.setText(timer.getDueDate() == null ? "" : timer.getDueDate());
		repeatText.setText(timer.getRepeat() == null ? "" : timer.getRepeat());
	}
	
	private void create() {
		nameLabel = widgetFactory.createLabel(parent, "Name");
        nameText = widgetFactory.createText(parent, "");
        transitionLabel = widgetFactory.createLabel(parent, "Transition");
        transitionText = widgetFactory.createText(parent, "");
        duedateLabel = widgetFactory.createLabel(parent, "Due Date");
        duedateText = widgetFactory.createText(parent, "");
        repeatLabel = widgetFactory.createLabel(parent, "Repeat");
        repeatText = widgetFactory.createText(parent, "");
        nameLabel.setLayoutData(createNameLabelLayoutData());
        nameText.setLayoutData(createNameTextLayoutData());
        transitionLabel.setLayoutData(createTransitionLabelLayoutData());
        transitionText.setLayoutData(createTransitionTextLayoutData());
        duedateLabel.setLayoutData(createDuedateLabelLayoutData());
        duedateText.setLayoutData(createDuedateTextLayoutData());
        repeatLabel.setLayoutData(createRepeatLabelLayoutData());
        repeatText.setLayoutData(createRepeatTextLayoutData());
	}
	
	private FormData createNameTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 85);
		data.top = new FormAttachment(0, 5);
		data.right = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createNameLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 7);
		return data;
	}
	
	private FormData createTransitionLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(nameText, 2);
		return data;
	}
	
	private FormData createTransitionTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 85);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(nameText, 0);
		return data;
	}
	
	private FormData createDuedateLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(transitionText, 2);
		return data;
	}
	
	private FormData createDuedateTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 85);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(transitionText, 0);
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
		data.left = new FormAttachment(0, 85);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(duedateText, 0);
		return data;
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == nameText) {
			timer.setName("".equals(nameText.getText()) ? null : nameText.getText());
		} else if (e.widget == transitionText) {
			timer.setTransition("".equals(transitionText.getText()) ? null : transitionText.getText());
		} else if (e.widget == duedateText) {
			timer.setDueDate("".equals(duedateText.getText()) ? null : duedateText.getText());
		} else if (e.widget == repeatText) {
			timer.setRepeat("".equals(repeatText.getText()) ? null : repeatText.getText());
		}
	}

}
