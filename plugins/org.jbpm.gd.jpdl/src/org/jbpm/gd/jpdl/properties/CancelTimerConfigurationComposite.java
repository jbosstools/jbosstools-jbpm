package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.common.properties.NamedElementConfigurationComposite;
import org.jbpm.gd.jpdl.model.CancelTimer;

public class CancelTimerConfigurationComposite {
	
	public static CancelTimerConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		CancelTimerConfigurationComposite result = new CancelTimerConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	private CancelTimer cancelTimer;
	
	private CTabFolder cancelTimerTabFolder;
	private NamedElementConfigurationComposite namedElementConfigurationComposite;
	
	public void setCancelTimer(CancelTimer cancelTimer) {
		if (this.cancelTimer == cancelTimer) return;
		unhookListeners();
		clearControls();
		this.cancelTimer = cancelTimer;
		if (cancelTimer != null) {
			updateControls();
			hookListeners();
		}
	}
	
	public CancelTimer getCancelTimer() {
		return cancelTimer;
	}
	
	private void hookListeners() {}
	
	private void unhookListeners() {}
	
	private void clearControls() {
		namedElementConfigurationComposite.setNamedElement(null);
	}
	
	private void updateControls() {
		namedElementConfigurationComposite.setNamedElement(cancelTimer);
	}
	
	private void create() {
		cancelTimerTabFolder = widgetFactory.createTabFolder(parent, SWT.TOP | SWT.BORDER);
		cancelTimerTabFolder.setLayoutData(createCancelTimerTabFolderLayoutData());
		createGeneralTabItem();
		cancelTimerTabFolder.setSelection(0);
	}
	
	private void createGeneralTabItem() {
		CTabItem generalTabItem = widgetFactory.createTabItem(cancelTimerTabFolder, SWT.NORMAL);
		generalTabItem.setText("General");		
		Composite generalTabControl = widgetFactory.createFlatFormComposite(cancelTimerTabFolder);
		namedElementConfigurationComposite = 
			NamedElementConfigurationComposite.create(widgetFactory, generalTabControl);
		generalTabItem.setControl(generalTabControl);
	}
	
	private FormData createCancelTimerTabFolderLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.top = new FormAttachment(0, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
}
