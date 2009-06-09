package org.jbpm.gd.jpdl.properties;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.dialog.ChooseDelegationClassDialog;
import org.jbpm.gd.jpdl.model.Controller;
import org.jbpm.gd.jpdl.model.Task;

public class ControllerConfigurationComposite implements SelectionListener, FocusListener {
	
	public static ControllerConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		ControllerConfigurationComposite result = new ControllerConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
		
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
    private Task task;
    
    private CCombo controllerTypeCombo;
    private HashMap pagesMap = new HashMap();

    private DelegationConfigurationComposite delegationConfigurationComposite;
    private VariableContainerConfigurationComposite variableContainerConfigurationComposite;
    
	private ControllerConfigurationComposite() {}
	
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
		controllerTypeCombo.setEnabled(true);
		controllerTypeCombo.addSelectionListener(this);
	}
	
	private void unhookListeners() {
		controllerTypeCombo.setEnabled(false);
		controllerTypeCombo.removeSelectionListener(this);
	}
	
	private void clearControls() {
		controllerTypeCombo.setText("");
		Iterator iter = pagesMap.keySet().iterator();
		while (iter.hasNext()) {
			((Composite)pagesMap.get(iter.next())).setVisible(false);
		}
		variableContainerConfigurationComposite.setVariableContainer(null);
		delegationConfigurationComposite.setDelegation(null);
	}
	
	private void updateControls() {
		Controller controller = task.getController();
		if (controller != null) {
			String className = controller.getClassName();
			if (className == null) {
				controllerTypeCombo.setText("Default");
				variableContainerConfigurationComposite.setVariableContainer(controller);
			} else {
				controllerTypeCombo.setText("Custom");
				delegationConfigurationComposite.setDelegation(controller);
			}
		} else {
			controllerTypeCombo.setText("<Choose>");
		}
		Iterator iter = pagesMap.keySet().iterator();
		while (iter.hasNext()) {
			String str = (String)iter.next();
			((Composite)pagesMap.get(str)).setVisible(str.equals(controllerTypeCombo.getText()));
		}
	}
	
	private void create() {
		controllerTypeCombo = widgetFactory.createCCombo(parent);
		controllerTypeCombo.setItems(new String[] {"<Choose>", "Default", "Custom"});
		controllerTypeCombo.setLayoutData(createControllerTypeComboLayoutData());
		createDefaultPage();
		createCustomPage();
	}
	
	private void createDefaultPage() {
		Composite defaultPage = widgetFactory.createFlatFormComposite(parent);
		pagesMap.put("Default", defaultPage);
		defaultPage.setLayoutData(createGroupLayoutData());
		variableContainerConfigurationComposite = 
			VariableContainerConfigurationComposite.create(widgetFactory, defaultPage);
	}
	
	private void createCustomPage() {
		Composite customPage = widgetFactory.createFlatFormComposite(parent);
		pagesMap.put("Custom", customPage);
		customPage.setLayoutData(createGroupLayoutData());
		delegationConfigurationComposite = 
			DelegationConfigurationComposite.create(widgetFactory, customPage, createChooseControllerClassDialog());
	}
	
	private ChooseDelegationClassDialog createChooseControllerClassDialog() {
		return new ChooseDelegationClassDialog(
				parent.getShell(), 
				"org.jbpm.taskmgmt.def.TaskControllerHandler",
				"Choose Controller Handler",
				"Choose a task controller handler from the list");
	}
	
	private FormData createGroupLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(controllerTypeCombo, 0);
		result.right = new FormAttachment(100, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
	private FormData createControllerTypeComboLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.top = new FormAttachment(0, 0);
		return result;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.widget == controllerTypeCombo) {
			handleControllerTypeComboSelected();
		}
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
	}	
	
	private void handleControllerTypeComboSelected() {
		String type = controllerTypeCombo.getText();
		if ("".equals(type)) {
			task.setController(null);
		} else if ("Default".equals(type)){
			Controller controller = (Controller)variableContainerConfigurationComposite.getVariableContainer();
			if (controller == null) {
				controller = createController();
				variableContainerConfigurationComposite.setVariableContainer(controller);
			}
			task.setController(controller);
		} else if ("Custom".equals(type)) {
			Controller controller = (Controller)delegationConfigurationComposite.getDelegation();
			if (controller == null) {
				controller = createController();
				controller.setClassName("");
				delegationConfigurationComposite.setDelegation(controller);
			}
			task.setController(controller);
		}
		Iterator iter = pagesMap.keySet().iterator();
		while (iter.hasNext()) {
			String str = (String)iter.next();
			((Composite)pagesMap.get(str)).setVisible(type.equals(str));
		}
	}
	
	private Controller createController() {
		return (Controller)task.getFactory().createById("org.jbpm.gd.jpdl.controller");
	}
	
}
