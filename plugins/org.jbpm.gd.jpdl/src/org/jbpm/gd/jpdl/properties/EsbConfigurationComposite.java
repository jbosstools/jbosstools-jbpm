package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.EsbElement;

public class EsbConfigurationComposite {
	
	public static EsbConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		EsbConfigurationComposite result = new EsbConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	private EsbElement esbElement;
		
	private CTabFolder actionTabFolder;
	private CTabItem outputTabItem;
	private Composite outputTabControl;
	private EsbGeneralConfigurationComposite esbGeneralConfigurationComposite;
	private EsbInputOutputConfigurationComposite esbInputConfigurationComposite;
	private EsbInputOutputConfigurationComposite esbOutputConfigurationComposite;
	
	
	public void setEsbElement(EsbElement esbElement) {
		if (this.esbElement == esbElement) return;
		unhookListeners();
		clearControls();
		this.esbElement = esbElement;
		if (esbElement != null) {
			updateControls();
			hookListeners();
		}
	}
	
	public EsbElement getEsbElement() {
		return esbElement;
	}
	
	private void unhookListeners() {
	}
	
	private void hookListeners() {
	}
	
	private void clearControls() {
		esbGeneralConfigurationComposite.setEsbElement(null);
		esbInputConfigurationComposite.setEsbElement(null);
		if (outputTabItem != null) {
			esbOutputConfigurationComposite.setEsbElement(null);
			outputTabItem.dispose();
		}
	}
	
	private void updateControls() {
		esbGeneralConfigurationComposite.setEsbElement(esbElement);
		esbInputConfigurationComposite.setEsbElement(esbElement);
		if (!esbElement.isOneWay()) {
			createOutputTabItem();
			esbOutputConfigurationComposite.setEsbElement(esbElement);
		}
	}
	
	private void create() {
		actionTabFolder = widgetFactory.createTabFolder(parent, SWT.TOP | SWT.BORDER);
		actionTabFolder.setLayoutData(createEsbNodeTabFolderLayoutData());
		createGeneralTabItem();
		createInputTabItem();
		actionTabFolder.setSelection(0);
	}
	
	private void createGeneralTabItem() {
		CTabItem generalTabItem = widgetFactory.createTabItem(actionTabFolder, SWT.NORMAL);
		generalTabItem.setText("General");		
		Composite generalTabControl = widgetFactory.createFlatFormComposite(actionTabFolder);
		esbGeneralConfigurationComposite = 
			EsbGeneralConfigurationComposite.create(widgetFactory, generalTabControl);
		generalTabItem.setControl(generalTabControl);
	}
	
	private void createInputTabItem() {
		CTabItem inputTabItem = widgetFactory.createTabItem(actionTabFolder, SWT.NORMAL);
		inputTabItem.setText("Input");
		Composite inputTabControl = widgetFactory.createFlatFormComposite(actionTabFolder);
		esbInputConfigurationComposite = 
			EsbInputOutputConfigurationComposite.create(
					widgetFactory, 
					inputTabControl, 
					EsbInputOutputConfigurationComposite.INPUT_CONFIGURATION);
		inputTabItem.setControl(inputTabControl);
	}
	
	private void createOutputTabItem() {
		outputTabItem = widgetFactory.createTabItem(actionTabFolder, SWT.NORMAL);
		outputTabItem.setText("Output");
		if (outputTabControl == null) {
			outputTabControl = widgetFactory.createFlatFormComposite(actionTabFolder);
			esbOutputConfigurationComposite = 
				EsbInputOutputConfigurationComposite.create(
						widgetFactory, 
						outputTabControl,
						EsbInputOutputConfigurationComposite.OUTPUT_CONFIGURATION);
		}
		outputTabItem.setControl(outputTabControl);		
	}
	
	private FormData createEsbNodeTabFolderLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.top = new FormAttachment(0, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}

}
