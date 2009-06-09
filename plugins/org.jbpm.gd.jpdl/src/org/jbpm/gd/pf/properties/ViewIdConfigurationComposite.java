package org.jbpm.gd.pf.properties;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.pf.model.NodeElement;

public class ViewIdConfigurationComposite implements FocusListener {
	
	public static ViewIdConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		ViewIdConfigurationComposite result = new ViewIdConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Label viewIdLabel;
	private Text viewIdText;
	
	private NodeElement nodeElement;
	
	private ViewIdConfigurationComposite() {}
	
	public void setNodeElement(NodeElement nodeElement) {
		if (this.nodeElement == nodeElement) return;
		unhookSelectionListener();
		clearControls();
		this.nodeElement = nodeElement;
		if (nodeElement != null) {
			updateControls();
			hookSelectionListener();
		}
	}
	
	private void hookSelectionListener() {
		viewIdText.addFocusListener(this);
	}
	
	private void unhookSelectionListener() {
		viewIdText.removeFocusListener(this);
	}
	
	private void clearControls() {
		viewIdText.setText("");
	}
	
	private void updateControls() {
		String viewId = nodeElement.getViewId();
		viewIdText.setText(viewId == null ? "" : viewId);
	}
	
	private void create() {
		viewIdLabel = widgetFactory.createLabel(parent, "View Id");
        viewIdText = widgetFactory.createText(parent, "");
        viewIdLabel.setLayoutData(createViewIdLabelLayoutData());
        viewIdText.setLayoutData(createViewIdTextLayoutData());
	}
	
	private FormData createViewIdTextLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 85);
		data.top = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createViewIdLabelLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 2);
		return data;
	}
	
	private String getViewIdText() {
		String text = viewIdText.getText();
		if ("".equals(text)) {
			text = null;
		}
		return text;
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if (e.widget == viewIdText) {
			nodeElement.setViewId(getViewIdText());
		}
	}
	
}
