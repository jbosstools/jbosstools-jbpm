package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Timer;

public class TimerConfigurationComposite {
	
	public static TimerConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		TimerConfigurationComposite result = new TimerConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	private Timer timer;
	
	private CTabFolder timerTabFolder;
	private TimerGeneralConfigurationComposite timerGeneralConfigurationComposite;
	private TimerActionConfigurationComposite timerActionConfigurationComposite;
	
	public void setTimer(Timer timer) {
		if (this.timer == timer) return;
		unhookListeners();
		clearControls();
		this.timer = timer;
		if (timer != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {}
	
	private void unhookListeners() {}
	
	private void clearControls() {
		timerGeneralConfigurationComposite.setTimer(null);
		timerActionConfigurationComposite.setTimer(null);
	}
	
	private void updateControls() {
		timerGeneralConfigurationComposite.setTimer(timer);
		timerActionConfigurationComposite.setTimer(timer);
	}
	
	private void create() {
		timerTabFolder = widgetFactory.createTabFolder(parent, SWT.TOP | SWT.BORDER);
		timerTabFolder.setLayoutData(createTimerTabFolderLayoutData());
		createGeneralTabItem();
		createActionTabItem();
		timerTabFolder.setSelection(0);
	}
	
	private void createGeneralTabItem() {
		CTabItem generalTabItem = widgetFactory.createTabItem(timerTabFolder, SWT.NORMAL);
		generalTabItem.setText("General");		
		Composite generalTabControl = widgetFactory.createFlatFormComposite(timerTabFolder);
		timerGeneralConfigurationComposite = 
			TimerGeneralConfigurationComposite.create(widgetFactory, generalTabControl);
		generalTabItem.setControl(generalTabControl);
	}
	
	private void createActionTabItem() {
		CTabItem actionTabItem = widgetFactory.createTabItem(timerTabFolder, SWT.NORMAL);
		actionTabItem.setText("Action");
		Composite actionTabControl = widgetFactory.createFlatFormComposite(timerTabFolder);
		timerActionConfigurationComposite = 
			TimerActionConfigurationComposite.create(widgetFactory, actionTabControl);
		actionTabItem.setControl(actionTabControl);
	}
	
	private FormData createTimerTabFolderLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.top = new FormAttachment(0, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
}
