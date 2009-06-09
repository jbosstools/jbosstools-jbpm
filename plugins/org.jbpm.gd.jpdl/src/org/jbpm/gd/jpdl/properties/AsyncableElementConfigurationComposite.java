package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.AsyncableElement;
import org.jbpm.gd.jpdl.util.BooleanTypeHelper;

public class AsyncableElementConfigurationComposite implements SelectionListener {
	
	public static AsyncableElementConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		AsyncableElementConfigurationComposite result = new AsyncableElementConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
	private Button asyncButton;

	private AsyncableElement asyncableElement;
	
	private AsyncableElementConfigurationComposite() {}
	
	public void setAsyncableElement(AsyncableElement asyncableElement) {
		if (this.asyncableElement == asyncableElement) return;
		unhookSelectionListener();
		clearControls();
		this.asyncableElement = asyncableElement;
		if (asyncableElement != null) {
			updateControls();
			hookSelectionListener();
		}
	}
	
	private void hookSelectionListener() {
		asyncButton.addSelectionListener(this);
	}
	
	private void unhookSelectionListener() {
		asyncButton.removeSelectionListener(this);
	}
	
	private void clearControls() {
		asyncButton.setSelection(false);
	}
	
	private void updateControls() {
		asyncButton.setSelection(BooleanTypeHelper.booleanValue(asyncableElement.getAsync()));
	}
	
	private void create() {
        asyncButton = widgetFactory.createButton(parent, "Asynchronous", SWT.CHECK); 
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget == asyncButton) {
			asyncableElement.setAsync(asyncButton.getSelection() ? "true" : "false");
		}
	}
	
}
