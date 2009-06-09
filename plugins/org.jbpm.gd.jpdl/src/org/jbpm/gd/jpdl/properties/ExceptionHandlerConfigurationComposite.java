package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.ExceptionHandler;

public class ExceptionHandlerConfigurationComposite implements FocusListener {
	
	public static ExceptionHandlerConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		ExceptionHandlerConfigurationComposite result = new ExceptionHandlerConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Label exceptionClassLabel;
	private Text exceptionClassText;
	
	private ExceptionHandler exceptionHandler;
	
	private ExceptionHandlerConfigurationComposite() {}
	
	public void setExceptionHandler(ExceptionHandler exceptionHandler) {
		if (this.exceptionHandler == exceptionHandler) return;
		unhookListeners();
		this.exceptionHandler = exceptionHandler;
		if (exceptionHandler == null) {
			clearControls();
		} else {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		exceptionClassText.addFocusListener(this);
	}
	
	private void unhookListeners() {
		exceptionClassText.removeFocusListener(this);
	}
	
	private void clearControls() {
		exceptionClassText.setText("");
	}
	
	private void updateControls() {
		String exceptionClass = exceptionHandler.getExceptionClass();
		if (exceptionClass != null) {
			exceptionClassText.setText(exceptionClass);
		} else {
			exceptionClassText.setText("");
		}
	}
	
	private void create() {
		exceptionClassLabel = widgetFactory.createLabel(parent, "Exception class");
		exceptionClassText = widgetFactory.createText(parent, "");
		exceptionClassText.setText("");
		exceptionClassLabel.setLayoutData(createExceptionClassLabelLayoutData());
		exceptionClassText.setLayoutData(createExceptionClassTextLayoutData());
	}
	
	private FormData createExceptionClassTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(exceptionClassLabel, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		return data;
	}
	
	private FormData createExceptionClassLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 2);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == exceptionClassText) {
			exceptionHandler.setExceptionClass(exceptionClassText.getText());
		}
	}

}
