package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.Action;

public class ActionConfigurationComposite {
	
	public static ActionConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		ActionConfigurationComposite result = new ActionConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	private Action action;
	
	private CTabFolder actionTabFolder;
	private ActionNameConfigurationComposite actionNameConfigurationComposite;
	private ActionDetailsConfigurationComposite actionDetailsConfigurationComposite;
	private ActionAdvancedConfigurationComposite actionAdvancedConfigurationComposite;
	
	
	public void setAction(Action action) {
		if (this.action == action) return;
		unhookListeners();
		clearControls();
		this.action = action;
		if (action != null) {
			updateControls();
			hookListeners();
		}
	}
	
	public Action getAction() {
		return action;
	}
	
	private void unhookListeners() {}
	private void hookListeners() {}
	
	private void clearControls() {
		actionNameConfigurationComposite.setAction(null);
		actionDetailsConfigurationComposite.setAction(null);
		actionAdvancedConfigurationComposite.setAction(null);
	}
	
	private void updateControls() {
		actionNameConfigurationComposite.setAction(action);
		actionDetailsConfigurationComposite.setAction(action);
		actionAdvancedConfigurationComposite.setAction(action);
	}
	
	private void create() {
		actionTabFolder = widgetFactory.createTabFolder(parent, SWT.TOP | SWT.BORDER);
		actionTabFolder.setLayoutData(createActionTabFolderLayoutData());
		createGeneralTabItem();
		createDetailsTabItem();
		createAdvancedTabItem();
		actionTabFolder.setSelection(0);
	}
	
	private void createGeneralTabItem() {
		CTabItem generalTabItem = widgetFactory.createTabItem(actionTabFolder, SWT.NORMAL);
		generalTabItem.setText("General");		
		Composite generalTabControl = widgetFactory.createFlatFormComposite(actionTabFolder);
		actionNameConfigurationComposite = 
			ActionNameConfigurationComposite.create(widgetFactory, generalTabControl);
		generalTabItem.setControl(generalTabControl);
	}
	
	private void createDetailsTabItem() {
		CTabItem detailsTabItem = widgetFactory.createTabItem(actionTabFolder, SWT.NORMAL);
		detailsTabItem.setText("Details");
		Composite detailsTabControl = widgetFactory.createFlatFormComposite(actionTabFolder);
		actionDetailsConfigurationComposite = 
			ActionDetailsConfigurationComposite.create(widgetFactory, detailsTabControl);
		detailsTabItem.setControl(detailsTabControl);
	}
	
	private void createAdvancedTabItem() {
		CTabItem advancedTabItem = widgetFactory.createTabItem(actionTabFolder, SWT.NORMAL);
		advancedTabItem.setText("Advanced");
		Composite advancedTabControl = widgetFactory.createFlatFormComposite(actionTabFolder);
		actionAdvancedConfigurationComposite = 
			ActionAdvancedConfigurationComposite.create(widgetFactory, advancedTabControl);
		advancedTabItem.setControl(advancedTabControl);		
	}
	
	private FormData createActionTabFolderLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.top = new FormAttachment(0, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
}
