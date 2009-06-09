package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.common.properties.NamedElementConfigurationComposite;
import org.jbpm.gd.jpdl.model.MailAction;

public class MailActionConfigurationComposite {
	
	public static MailActionConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		MailActionConfigurationComposite result = new MailActionConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	private MailAction mailAction;
	
	private CTabFolder mailActionTabFolder;
	private NamedElementConfigurationComposite namedElementConfigurationComposite;
	private MailElementTemplateConfigurationComposite mailElementTemplateConfigurationComposite;
	private MailElementMailInfoConfigurationComposite mailElementMailInfoConfigurationComposite;
	private AsyncableElementConfigurationComposite asyncableElementConfigurationComposite;
	
	public void setMailAction(MailAction mailAction) {
		if (this.mailAction == mailAction) return;
		unhookListeners();
		clearControls();
		this.mailAction = mailAction;
		if (mailAction != null) {
			updateControls();
			hookListeners();
		}
	}
	
	public MailAction getMailAction() {
		return mailAction;
	}
	
	private void hookListeners() {}	
	private void unhookListeners() {}
	
	private void clearControls() {
		namedElementConfigurationComposite.setNamedElement(null);
		mailElementTemplateConfigurationComposite.setMailElement(null);
		mailElementMailInfoConfigurationComposite.setMailElement(null);
		asyncableElementConfigurationComposite.setAsyncableElement(null);
	}
	
	private void updateControls() {
		namedElementConfigurationComposite.setNamedElement(mailAction);
		mailElementTemplateConfigurationComposite.setMailElement(mailAction);
		mailElementMailInfoConfigurationComposite.setMailElement(mailAction);
		asyncableElementConfigurationComposite.setAsyncableElement(mailAction);
	}
	
	private void create() {
		mailActionTabFolder = widgetFactory.createTabFolder(parent, SWT.BORDER |SWT.TOP);
		mailActionTabFolder.setLayoutData(createMailActionTabFolderLayoutData());
		createGeneralTabItem();
		createMailInfoTabItem();
		createAdvancedTabItem();
		mailActionTabFolder.setSelection(0);
	}
	
	private void createGeneralTabItem() {
		CTabItem generalTabItem = widgetFactory.createTabItem(mailActionTabFolder, SWT.NORMAL);
		generalTabItem.setText("General");
		Composite generalTabControl = widgetFactory.createComposite(mailActionTabFolder);
		generalTabControl.setLayout(new FormLayout());
		Composite namedElementSection = widgetFactory.createFlatFormComposite(generalTabControl);
		namedElementSection.setLayoutData(createSectionLayoutData());
		namedElementConfigurationComposite = NamedElementConfigurationComposite.create(widgetFactory, namedElementSection);
		Composite templateSection = widgetFactory.createFlatFormComposite(generalTabControl);
		templateSection.setLayoutData(createSectionLayoutData());
		mailElementTemplateConfigurationComposite = MailElementTemplateConfigurationComposite.create(widgetFactory, templateSection);
		generalTabItem.setControl(generalTabControl);
	}
	
	private void createMailInfoTabItem() {
		CTabItem mailInfoTabItem = widgetFactory.createTabItem(mailActionTabFolder, SWT.NORMAL);
		mailInfoTabItem.setText("Mail Info");
		Composite mailInfoTabControl = widgetFactory.createFlatFormComposite(mailActionTabFolder);
		mailElementMailInfoConfigurationComposite = MailElementMailInfoConfigurationComposite.create(widgetFactory, mailInfoTabControl);
		mailInfoTabItem.setControl(mailInfoTabControl);
	}
	
	private void createAdvancedTabItem() {
		CTabItem advancedTabItem = widgetFactory.createTabItem(mailActionTabFolder, SWT.NORMAL);
		advancedTabItem.setText("Advanced");
		Composite advancedTabControl = widgetFactory.createFlatFormComposite(mailActionTabFolder);
		asyncableElementConfigurationComposite = AsyncableElementConfigurationComposite.create(widgetFactory, advancedTabControl);
		advancedTabItem.setControl(advancedTabControl);
	}
	
	private FormData createMailActionTabFolderLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.top = new FormAttachment(0, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
	private FormData createSectionLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		return result;
	}
	
}
