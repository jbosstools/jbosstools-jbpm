package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.MailElement;
import org.jbpm.gd.jpdl.model.Subject;

public class MailElementMailInfoConfigurationComposite implements SelectionListener, FocusListener {
	
	public static MailElementMailInfoConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		MailElementMailInfoConfigurationComposite result = new MailElementMailInfoConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	private MailElement mailElement;
	
	private Label destinationLabel;
	private CCombo  destinationCombo;
	private Text destinationText;
	private Label subjectLabel;
	private Text subjectText;
	private Label bodyLabel;
	private Text bodyText;
	
	public void setMailElement(MailElement mailElement) {
		if (this.mailElement == mailElement) return;
		unhookListeners();
		this.mailElement = mailElement;
		if (mailElement == null) {
			clearControls();
		} else {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		destinationCombo.addSelectionListener(this);
		destinationText.addFocusListener(this);
		subjectText.addFocusListener(this);
		bodyText.addFocusListener(this);
	}
	
	private void unhookListeners() {
		destinationCombo.removeSelectionListener(this);
		destinationText.addFocusListener(this);
		subjectText.removeFocusListener(this);
		bodyText.removeFocusListener(this);
	}
	
	private void clearControls() {
		destinationCombo.setText("To");
		destinationText.setText("");
		subjectText.setText("");
		bodyText.setText("");
	}
	
	private void updateControls() {
		destinationCombo.setText(mailElement.getActors() != null ? "Actors" : "To");
		if (mailElement.getActors() != null) {
			destinationCombo.setText("Actors");
			destinationText.setText(mailElement.getActors());
		} else {
			destinationCombo.setText("To");
			destinationText.setText(mailElement.getTo() == null ? "" : mailElement.getTo());
		}
		subjectText.setText(getSubjectText());
		bodyText.setText(getBodyText());
	}
	
	private String getSubjectText() {
		String result = "";
		Subject subject = mailElement.getSubject();
		if (subject != null) {
			result = subject.getSubject() == null ? "" : subject.getSubject();
		}
		return result;
	}
	
	private String getBodyText() {
		String result = "";
		if (mailElement.getText() != null) {
			result = mailElement.getText().getText() == null ? "" : mailElement.getText().getText();
		}
		return result;
	}
	
	private void create() {
		destinationLabel = widgetFactory.createLabel(parent, "Destination");
		destinationCombo = widgetFactory.createCCombo(parent);
		destinationCombo.setItems(new String[] { "To", "Actors"});
		destinationCombo.setText("To");
		destinationText = widgetFactory.createText(parent, "");
		subjectLabel = widgetFactory.createLabel(parent, "Subject");
		subjectText = widgetFactory.createText(parent, "");
		bodyLabel = widgetFactory.createLabel(parent, "Body");
		bodyText = widgetFactory.createText(parent, "", SWT.MULTI | SWT.V_SCROLL);
		destinationLabel.setLayoutData(createDestinationLabelLayoutData());
		destinationCombo.setLayoutData(createDestinationComboLayoutData());
		destinationText.setLayoutData(createDestinationTextLayoutData());
		subjectLabel.setLayoutData(createSubjectLabelLayoutData());
		subjectText.setLayoutData(createSubjectTextLayoutData());
		bodyLabel.setLayoutData(createBodyLabelLayoutData());
		bodyText.setLayoutData(createBodyTextLayoutData());
	}
	
	private FormData createDestinationLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 3);
		return data;
	}
	
	private FormData createDestinationComboLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(destinationLabel, 0);
		data.top = new FormAttachment(0, 0);
		return data;
	}
	
	private FormData createDestinationTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(destinationCombo, 0);
		data.top = new FormAttachment(0, 2);
		data.right = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createSubjectLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(destinationCombo, 0);
		return data;
	}
	
	private FormData createSubjectTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(destinationCombo, 0);
		data.left.alignment = SWT.LEFT;
		data.top = new FormAttachment(destinationCombo, 0);
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
		data.left = new FormAttachment(destinationCombo, 0);
		data.left.alignment = SWT.LEFT;
		data.top = new FormAttachment(subjectText, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		data.height = 50;
		return data;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	private void updateSubject() {
		Subject subject = mailElement.getSubject();
		if (subject == null) {
			subject = (Subject)mailElement.getFactory().createById("org.jbpm.gd.jpdl.subject");
			mailElement.setSubject(subject);
		}
		subject.setSubject(subjectText.getText());
	}

	private void updateBody() {
		org.jbpm.gd.jpdl.model.Text text = mailElement.getText();
		if (text == null) {
			text = (org.jbpm.gd.jpdl.model.Text)mailElement.getFactory().createById("org.jbpm.gd.jpdl.text");
			mailElement.setText(text);
		}
		text.setText(bodyText.getText());
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == destinationCombo) {
			updateDestination();
		}
	}
	
	private void updateDestination() {
		String str = destinationText.getText();
		if ("To".equals(destinationCombo.getText())) {
			mailElement.setTo(str == null ? "" : str);
			mailElement.setActors(null);
		} else {
			mailElement.setTo(null);
			mailElement.setActors(str == null ? "" : str);
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == destinationText) {
			updateDestination();
		} else if (e.widget == subjectText) {
			updateSubject();
		} else if (e.widget == bodyText) {
			updateBody();
		}
	}
	
}
