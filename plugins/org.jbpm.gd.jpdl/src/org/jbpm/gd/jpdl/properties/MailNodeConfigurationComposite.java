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
import org.jbpm.gd.jpdl.model.MailNode;
import org.jbpm.gd.jpdl.model.Subject;

public class MailNodeConfigurationComposite implements SelectionListener, FocusListener {
	
	public static MailNodeConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		MailNodeConfigurationComposite result = new MailNodeConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Label templateLabel;
	private Text templateText;
	private Button toButton;
	private Text toText;
	private Button actorsButton;
	private Text actorsText;
	private Label subjectLabel;
	private Text subjectText;
	private Label bodyLabel;
	private Text bodyText;
	
	private MailNode mailNode;
	
	private MailNodeConfigurationComposite() {}
	
	public void setMailNode(MailNode mailNode) {
		if (this.mailNode == mailNode) return;
		unhookListeners();
		this.mailNode = mailNode;
		if (mailNode == null) {
			clearControls();
		} else {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		templateText.addSelectionListener(this);
		templateText.addFocusListener(this);
		toButton.addSelectionListener(this);
		toText.addSelectionListener(this);
		toText.addFocusListener(this);
		actorsButton.addSelectionListener(this);
		actorsText.addSelectionListener(this);
		actorsText.addFocusListener(this);
		subjectText.addSelectionListener(this);
		subjectText.addFocusListener(this);
		bodyText.addSelectionListener(this);
		bodyText.addFocusListener(this);
	}
	
	private void unhookListeners() {
		templateText.removeSelectionListener(this);
		templateText.removeFocusListener(this);
		toButton.removeSelectionListener(this);
		toText.removeSelectionListener(this);
		toText.removeFocusListener(this);
		actorsButton.removeSelectionListener(this);
		actorsText.removeSelectionListener(this);
		actorsText.removeFocusListener(this);
		subjectText.removeSelectionListener(this);
		subjectText.removeFocusListener(this);
		bodyText.removeSelectionListener(this);
		bodyText.removeFocusListener(this);
	}
	
	private void clearControls() {
		templateText.setText("");
		toButton.setSelection(true);
		toText.setText("");
		toText.setEnabled(true);
		actorsButton.setSelection(false);
		actorsText.setText("");
		actorsText.setEnabled(false);
		subjectText.setText("");
		bodyText.setText("");
	}
	
	private void updateControls() {
		templateText.setText(mailNode.getTemplate() == null ? "" : mailNode.getTemplate());
		if (mailNode.getActors() != null) {
			toButton.setSelection(false);
			actorsButton.setSelection(true);
			toText.setText("");
			toText.setEnabled(false);
			actorsText.setText(mailNode.getActors());
			actorsText.setEnabled(true);
		} else {
			toButton.setSelection(true);
			actorsButton.setSelection(false);
			actorsText.setText("");
			actorsText.setEnabled(false);
			toText.setText(mailNode.getTo() == null ? "" : mailNode.getTo());
			toText.setEnabled(true);
		}
		subjectText.setText(getSubjectText());
		bodyText.setText(getBodyText());
	}
	
	private String getSubjectText() {
		String result = "";
		Subject subject = mailNode.getSubject();
		if (subject != null) {
			result = subject.getSubject() == null ? "" : subject.getSubject();
		}
		return result;
	}
	
	private String getBodyText() {
		String result = "";
		if (mailNode.getText() != null) {
			result = mailNode.getText().getText() == null ? "" : mailNode.getText().getText();
		}
		return result;
	}
	
	private void create() {
		templateLabel = widgetFactory.createLabel(parent, "Template");
		templateText = widgetFactory.createText(parent, "");
		toButton = widgetFactory.createButton(parent, "To", SWT.RADIO);
		toButton.setSelection(true);
		toText = widgetFactory.createText(parent, "");
		actorsButton = widgetFactory.createButton(parent, "Actors", SWT.RADIO);
		actorsText = widgetFactory.createText(parent, "");
		actorsText.setEnabled(false);
		subjectLabel = widgetFactory.createLabel(parent, "Subject");
		subjectText = widgetFactory.createText(parent, "");
		bodyLabel = widgetFactory.createLabel(parent, "Body");
		bodyText = widgetFactory.createText(parent, "", SWT.MULTI | SWT.V_SCROLL);
		templateLabel.setLayoutData(createTemplateLabelLayoutData());
		templateText.setLayoutData(createTemplateTextLayoutData());
		toButton.setLayoutData(createToButtonLayoutData());
		toText.setLayoutData(createToTextLayoutData());
		actorsButton.setLayoutData(createActorsButtonLayoutData());
		actorsText.setLayoutData(createActorsTextLayoutData());
		subjectLabel.setLayoutData(createSubjectLabelLayoutData());
		subjectText.setLayoutData(createSubjectTextLayoutData());
		bodyLabel.setLayoutData(createBodyLabelLayoutData());
		bodyText.setLayoutData(createBodyTextLayoutData());
	}
	
	private FormData createToButtonLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(templateText, 0);
		return data;
	}
	
	private FormData createToTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(templateText, 0);
		data.left.alignment = SWT.LEFT;
		data.top = new FormAttachment(templateText, 0);
		data.right = new FormAttachment(50, 0);
		return data;
	}
	
	private FormData createActorsButtonLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(toText, 0);
		data.top = new FormAttachment(templateText, 0);
		return data;
	}
	
	private FormData createActorsTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(actorsButton, 0);
		data.top = new FormAttachment(templateText, 0);
		data.right = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createSubjectLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(toText, 0);
		return data;
	}
	
	private FormData createSubjectTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(templateText, 0);
		data.left.alignment = SWT.LEFT;
		data.top = new FormAttachment(toText, 0);
		data.right = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createBodyLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(subjectText, 0);
		return data;
	}
	
	private FormData createBodyTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(templateText, 0);
		data.left.alignment = SWT.LEFT;
		data.top = new FormAttachment(subjectText, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		data.height = 100;
		return data;
	}
	
	private FormData createTemplateTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(templateLabel, 0);
		data.top = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createTemplateLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 0);
		return data;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		if (e.widget == templateText) {
			mailNode.setTemplate(getTemplateText());
		} else if (e.widget == toText) {
			mailNode.setTo(toText.getText());
		} else if (e.widget == actorsText) {
			mailNode.setActors(actorsText.getText());
		} else if (e.widget == subjectText) {
			handleSubjectTextChange();
		} else if (e.widget == bodyText) {
			handleBodyTextChange();
		}
	}
	
	private void handleSubjectTextChange() {
		Subject subject = mailNode.getSubject();
		if (subject == null) {
			subject = (Subject)mailNode.getFactory().createById("org.jbpm.gd.jpdl.subject");
			mailNode.setSubject(subject);
		}
		subject.setSubject(subjectText.getText());
	}

	private void handleBodyTextChange() {
		org.jbpm.gd.jpdl.model.Text text = mailNode.getText();
		if (text == null) {
			text = (org.jbpm.gd.jpdl.model.Text)mailNode.getFactory().createById("org.jbpm.gd.jpdl.text");
			mailNode.setText(text);
		}
		text.setText(bodyText.getText());
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == toButton) {
			handleToButtonSelection();
		}
	}
	
	private void handleToButtonSelection() {
		if (toButton.getSelection()) {
			mailNode.setTo(toText.getText());
			mailNode.setActors(null);
			actorsText.setEnabled(false);
			toText.setEnabled(true);
		} else {
			mailNode.setActors(actorsText.getText());
			mailNode.setTo(null);
			toText.setEnabled(false);
			actorsText.setEnabled(true);
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == templateText) {
			mailNode.setTemplate(getTemplateText());
		} else if (e.widget == toText) {
			mailNode.setTo(toText.getText());
		} else if (e.widget == actorsText) {
			mailNode.setActors(actorsText.getText());
		} else if (e.widget == subjectText) {
			handleSubjectTextChange();
		} else if (e.widget == bodyText) {
			handleBodyTextChange();
		}
	}
	
	private String getTemplateText()  {
		return "".equals(templateText.getText()) ? null : templateText.getText();
	}
	
}
