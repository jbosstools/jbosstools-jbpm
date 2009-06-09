package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.common.properties.NamedElementConfigurationComposite;
import org.jbpm.gd.jpdl.model.Task;

public class TaskConfigurationComposite implements SelectionListener, FocusListener, ControlListener, DisposeListener {
	
	public static TaskConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		TaskConfigurationComposite result = new TaskConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
	
	CTabFolder taskInfoTabFolder;
	Composite namedElementComposite;
	Composite describableElementComposite;
	NamedElementConfigurationComposite namedElementConfigurationComposite;
	DescribableElementConfigurationComposite describableElementConfigurationComposite;
	TaskDetailsConfigurationComposite detailsConfigurationComposite;
	AssignmentConfigurationComposite assignmentConfigurationComposite;
	ControllerConfigurationComposite controllerConfigurationComposite;
	TaskReminderConfigurationComposite taskReminderConfigurationComposite;
	
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
    private Task task;
	
	private TaskConfigurationComposite() {}
	
	public void setTask(Task task) {
		if (this.task == task) return;
		unhookListeners();
		this.task = task;
		clearControls();
		if (task != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
	}
	
	private void unhookListeners() {
	}
	
	private void clearControls() {
		namedElementConfigurationComposite.setNamedElement(null);
		describableElementConfigurationComposite.setDescribableElement(null);
		detailsConfigurationComposite.setTask(null);
		assignmentConfigurationComposite.setAssignable(null);
		controllerConfigurationComposite.setTask(null);
		taskReminderConfigurationComposite.setTask(null);
	}
	
	private void updateControls() {
		namedElementConfigurationComposite.setNamedElement(task);
		describableElementConfigurationComposite.setDescribableElement(task);
		detailsConfigurationComposite.setTask(task);
		assignmentConfigurationComposite.setAssignable(task);
		controllerConfigurationComposite.setTask(task);
		taskReminderConfigurationComposite.setTask(task);
	}
	
	private void create() {
		taskInfoTabFolder = widgetFactory.createTabFolder(parent, SWT.TOP | SWT.BORDER);
		taskInfoTabFolder.setLayoutData(createTaskInfoTabFolderLayoutData());
		createGeneralTabItem();
		createDetailsTabItem();
		createAssignmentTabItem();
		createControllerTabItem();
		createReminderTabItem();
		taskInfoTabFolder.setSelection(0);
		taskInfoTabFolder.addControlListener(this);
		taskInfoTabFolder.addDisposeListener(this);
	}

	private void createGeneralTabItem() {
		CTabItem generalTabItem = widgetFactory.createTabItem(taskInfoTabFolder, SWT.NORMAL);
		generalTabItem.setText("General");
		Composite generalTabControl = widgetFactory.createComposite(taskInfoTabFolder);
		generalTabControl.setLayout(new FormLayout());
		namedElementComposite = widgetFactory.createFlatFormComposite(generalTabControl);
		namedElementComposite.setLayoutData(createNamedElementCompositeLayoutData());
		namedElementConfigurationComposite = NamedElementConfigurationComposite.create(widgetFactory, namedElementComposite);
		describableElementComposite = widgetFactory.createFlatFormComposite(generalTabControl);
		describableElementComposite.setLayoutData(createDescribableElementCompositeLayoutData());
		describableElementConfigurationComposite = DescribableElementConfigurationComposite.create(widgetFactory, describableElementComposite);
		generalTabItem.setControl(generalTabControl);
	}

	private void createControllerTabItem() {
		CTabItem controllerTabItem = widgetFactory.createTabItem(taskInfoTabFolder, SWT.NORMAL);
		controllerTabItem.setText("Controller");
		Composite controllerTabControl = widgetFactory.createFlatFormComposite(taskInfoTabFolder);
		controllerConfigurationComposite = ControllerConfigurationComposite.create(widgetFactory, controllerTabControl);
		controllerTabItem.setControl(controllerTabControl);
	}

	private void createAssignmentTabItem() {
		CTabItem assignmentTabItem = widgetFactory.createTabItem(taskInfoTabFolder, SWT.NORMAL);
		assignmentTabItem.setText("Assignment");
		Composite assignmentTabControl = widgetFactory.createFlatFormComposite(taskInfoTabFolder);
		assignmentConfigurationComposite = 
			AssignmentConfigurationComposite.create(widgetFactory, assignmentTabControl);
		assignmentTabItem.setControl(assignmentTabControl);
	}

	private void createDetailsTabItem() {
		CTabItem detailsTabItem = widgetFactory.createTabItem(taskInfoTabFolder, SWT.NORMAL);
		detailsTabItem.setText("Details");		
		Composite detailsTabControl = widgetFactory.createFlatFormComposite(taskInfoTabFolder);
		detailsConfigurationComposite = 
			TaskDetailsConfigurationComposite.create(widgetFactory, detailsTabControl);
		detailsTabItem.setControl(detailsTabControl);
	}
	
	private void createReminderTabItem() {
		CTabItem reminderTabItem = widgetFactory.createTabItem(taskInfoTabFolder, SWT.NORMAL);
		reminderTabItem.setText("Reminder");
		Composite reminderTabControl = widgetFactory.createFlatFormComposite(taskInfoTabFolder);
		taskReminderConfigurationComposite = TaskReminderConfigurationComposite.create(widgetFactory, reminderTabControl);
		reminderTabItem.setControl(reminderTabControl);
	}
	
	private FormData createTaskInfoTabFolderLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.top = new FormAttachment(0, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
	private FormData createNamedElementCompositeLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.top = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		return result;
	}
	
	private FormData createDescribableElementCompositeLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.top = new FormAttachment(namedElementComposite, 0);
		result.right = new FormAttachment(100, 0);
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
	
	public void controlMoved(ControlEvent e) {
	}

	public void controlResized(ControlEvent e) {
		taskInfoTabFolder.layout(true, true);		
	}

	public void widgetDisposed(DisposeEvent e) {
		taskInfoTabFolder.removeControlListener(this);
	}
	
}
