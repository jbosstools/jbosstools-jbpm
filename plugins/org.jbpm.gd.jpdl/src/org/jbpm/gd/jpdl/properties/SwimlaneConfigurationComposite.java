package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.common.properties.NamedElementConfigurationComposite;
import org.jbpm.gd.jpdl.model.Swimlane;

public class SwimlaneConfigurationComposite implements SelectionListener, FocusListener {
	
	public static SwimlaneConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		SwimlaneConfigurationComposite result = new SwimlaneConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	CTabFolder swimlaneInfoTabFolder;
	CTabItem assignmentTabItem;
	
	NamedElementConfigurationComposite generalConfigurationComposite;
	AssignmentConfigurationComposite assignmentConfigurationComposite;
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
    private Swimlane swimlane;
	
	private SwimlaneConfigurationComposite() {}
	
	public void setSwimlane(Swimlane swimlane) {
		if (this.swimlane == swimlane) return;
		unhookListeners();
		this.swimlane = swimlane;
		clearControls();
		if (swimlane != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
	}
	
	private void unhookListeners() {
	}
	
	private void clearControls() {
		generalConfigurationComposite.setNamedElement(null);
		assignmentConfigurationComposite.setAssignable(null);
	}
	
	private void updateControls() {
		generalConfigurationComposite.setNamedElement(swimlane);
		assignmentConfigurationComposite.setAssignable(swimlane);
	}
	
	private void create() {
		swimlaneInfoTabFolder = widgetFactory.createTabFolder(parent, SWT.TOP | SWT.BORDER);
		swimlaneInfoTabFolder.setLayoutData(createTaskInfoTabFolderLayoutData());
		createGeneralTabItem();
		createAssignmentTabItem();
		swimlaneInfoTabFolder.setSelection(0);
	}

	private void createAssignmentTabItem() {
		CTabItem assignmentTabItem = widgetFactory.createTabItem(swimlaneInfoTabFolder, SWT.NORMAL);
		assignmentTabItem.setText("Assignment");
		Composite assignmentTabControl = widgetFactory.createFlatFormComposite(swimlaneInfoTabFolder);
		assignmentConfigurationComposite = 
			AssignmentConfigurationComposite.create(widgetFactory, assignmentTabControl);
		assignmentTabItem.setControl(assignmentTabControl);
	}

	private void createGeneralTabItem() {
		CTabItem generalTabItem = widgetFactory.createTabItem(swimlaneInfoTabFolder, SWT.NORMAL);
		generalTabItem.setText("General");		
		Composite generalTabControl = widgetFactory.createFlatFormComposite(swimlaneInfoTabFolder);
		generalConfigurationComposite = 
			NamedElementConfigurationComposite.create(widgetFactory, generalTabControl);
		generalTabItem.setControl(generalTabControl);
	}
	
	private FormData createTaskInfoTabFolderLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.top = new FormAttachment(0, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	public void widgetSelected(SelectionEvent e) {
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
	}	
	
}
