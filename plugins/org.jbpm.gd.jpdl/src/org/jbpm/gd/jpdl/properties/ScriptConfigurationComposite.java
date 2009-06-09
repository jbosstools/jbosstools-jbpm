package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.common.properties.NamedElementConfigurationComposite;
import org.jbpm.gd.jpdl.model.Script;

public class ScriptConfigurationComposite {
	
	public static ScriptConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		ScriptConfigurationComposite result = new ScriptConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	private Script script;
	
	private CTabFolder scriptTabFolder;
	private NamedElementConfigurationComposite namedElementConfigurationComposite;
	private ScriptDetailsConfigurationComposite scriptDetailsConfigurationComposite;
	private ScriptAdvancedConfigurationComposite scriptAdvancedConfigurationComposite;
	
	public void setScript(Script script) {
		if (this.script == script) return;
		unhookListeners();
		clearControls();
		this.script = script;
		if (script != null) {
			updateControls();
			hookListeners();
		}
	}
	
	public Script getScript() {
		return script;
	}
	
	private void hookListeners() {}
	
	private void unhookListeners() {}
	
	private void clearControls() {
		namedElementConfigurationComposite.setNamedElement(null);
		scriptDetailsConfigurationComposite.setScript(null);
		scriptAdvancedConfigurationComposite.setScript(null);
	}
	
	private void updateControls() {
		namedElementConfigurationComposite.setNamedElement(script);
		scriptDetailsConfigurationComposite.setScript(script);
		scriptAdvancedConfigurationComposite.setScript(script);
	}
	
	private void create() {
		scriptTabFolder = widgetFactory.createTabFolder(parent, SWT.TOP | SWT.BORDER);
		scriptTabFolder.setLayoutData(createScriptTabFolderLayoutData());
		createGeneralTabItem();
		createDetailsTabItem();
		createAdvancedTabItem();
		scriptTabFolder.setSelection(0);
	}
	
	private void createGeneralTabItem() {
		CTabItem generalTabItem = widgetFactory.createTabItem(scriptTabFolder, SWT.NORMAL);
		generalTabItem.setText("General");		
		Composite generalTabControl = widgetFactory.createFlatFormComposite(scriptTabFolder);
		namedElementConfigurationComposite = 
			NamedElementConfigurationComposite.create(widgetFactory, generalTabControl);
		generalTabItem.setControl(generalTabControl);
	}
	
	private void createDetailsTabItem() {
		CTabItem detailsTabItem = widgetFactory.createTabItem(scriptTabFolder, SWT.NORMAL);
		detailsTabItem.setText("Details");
		Composite detailsTabControl = widgetFactory.createFlatFormComposite(scriptTabFolder);
		scriptDetailsConfigurationComposite = 
			ScriptDetailsConfigurationComposite.create(widgetFactory, detailsTabControl);
		detailsTabItem.setControl(detailsTabControl);
	}
	
	private void createAdvancedTabItem() {
		CTabItem advancedTabItem = widgetFactory.createTabItem(scriptTabFolder, SWT.NORMAL);
		advancedTabItem.setText("Advanced");
		Composite advancedTabControl = widgetFactory.createFlatFormComposite(scriptTabFolder);
		scriptAdvancedConfigurationComposite = 
			ScriptAdvancedConfigurationComposite.create(widgetFactory, advancedTabControl);
		advancedTabItem.setControl(advancedTabControl);		
	}
	
	private FormData createScriptTabFolderLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.top = new FormAttachment(0, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
}
