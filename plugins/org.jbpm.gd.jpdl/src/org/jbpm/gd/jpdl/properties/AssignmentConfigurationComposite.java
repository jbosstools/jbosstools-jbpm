package org.jbpm.gd.jpdl.properties;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.Logger;
import org.jbpm.gd.jpdl.Plugin;
import org.jbpm.gd.jpdl.model.Assignable;
import org.jbpm.gd.jpdl.util.AssignmentTypeHelper;
import org.jbpm.gd.jpdl.util.AssignmentTypeMatcher;

public class AssignmentConfigurationComposite implements SelectionListener {
	
	public static AssignmentConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		AssignmentConfigurationComposite result = new AssignmentConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
			
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
    private Assignable assignable;
    private Composite activePage;
    
    private CCombo assignmentTypeCombo;
    private HashMap pagesMap = new HashMap();
    private HashMap configurationMap = new HashMap();
    private HashMap matcherMap = new HashMap();
    private HashMap labelMap;
	
	public void setAssignable(Assignable assignable) {
		if (this.assignable == assignable) return;
		unhookListeners();
		this.assignable = assignable;
		clearControls();
		if (assignable != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		assignmentTypeCombo.addSelectionListener(this);
	}
	
	private void unhookListeners() {
		assignmentTypeCombo.removeSelectionListener(this);
	}
	
	private void clearControls() {
		labelMap = null;
		assignmentTypeCombo.removeAll();
		if (activePage != null) {
			activePage.setVisible(false);
			activePage = null;
		}
		Iterator iter = pagesMap.keySet().iterator();
		while (iter.hasNext()) {
			Composite page = (Composite)pagesMap.get(iter.next());
			((AssignmentTypeComposite)page.getData()).setAssignable(null);
		}
	}
	
	private void setPagesAssignable(Assignable assignable) {
		Iterator iter = pagesMap.keySet().iterator();
		while (iter.hasNext()) {
			Composite page = (Composite)pagesMap.get(iter.next());
			((AssignmentTypeComposite)page.getData()).setAssignable(assignable);
		}
	}
	
	private void updateControls() {
		updateAssignmentTypeCombo();
		setPagesAssignable(assignable);
	}
	
	private void updateAssignmentTypeCombo() {
		labelMap = new HashMap();
		assignmentTypeCombo.add("<Choose>");
		assignmentTypeCombo.setText("<Choose>");
		IConfigurationElement[] configElements = AssignmentTypeHelper.getConfigurationElements(assignable);
		for (int i = 0; i < configElements.length; i++) {
			if (isAssignmentTypeDisabled(configElements[i])) continue;
			String id = configElements[i].getAttribute("id");
			String label = configElements[i].getAttribute("label");
			labelMap.put(label, configElements[i].getAttribute("id"));
			assignmentTypeCombo.add(label);
			Composite page = (Composite)pagesMap.get(id);
			((AssignmentTypeComposite)page.getData()).setAssignable(assignable);
			AssignmentTypeMatcher matcher = (AssignmentTypeMatcher)matcherMap.get(id);
			if (matcher != null && matcher.matches(assignable)) {
				assignmentTypeCombo.setText(label);
				page.setVisible(true);
				activePage = page;
			}
		}
	}
	
	private void fillAssignmentTypes() {
		IConfigurationElement[] configElements = AssignmentTypeHelper.getConfigurationElements();
		for (int i = 0; i < configElements.length; i++) {
			if (isAssignmentTypeDisabled(configElements[i])) continue;
			String id = configElements[i].getAttribute("id");
			Composite page = widgetFactory.createComposite(parent);
			page.setVisible(false);
			page.setLayoutData(createPageLayoutData());
			pagesMap.put(id, page);
			configurationMap.put(id, configElements[i]);
			page.setLayout(new FormLayout());
			try {
				AssignmentTypeComposite composite = (AssignmentTypeComposite)configElements[i].createExecutableExtension("form");
				page.setData(composite);
				matcherMap.put(id, configElements[i].createExecutableExtension("matcher"));
				composite.setWidgetFactory(widgetFactory);
				composite.setParent(page);
				composite.create();
			} catch (CoreException e) {
				Logger.logError("Unable to create configuration form for " + configElements[i].getAttribute("form"), e);
			}
		}
	}
	
	private boolean isAssignmentTypeDisabled(IConfigurationElement element) {
		String key = "assignmentType(" + element.getAttribute("id") + ").disabled";
		return Plugin.getDefault().getPreferenceStore().getBoolean(key);
	}
	
	private void create() {
		assignmentTypeCombo = widgetFactory.createCCombo(parent);
		fillAssignmentTypes();
		assignmentTypeCombo.addSelectionListener(this);
		assignmentTypeCombo.setLayoutData(createAssignmentTypeCombo());
	}
	
	private FormData createAssignmentTypeCombo() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(0, 0);
		result.width = 120;
		return result;
	}
	
	private FormData createPageLayoutData() {
		FormData result = new FormData();
		result.top = new FormAttachment(0, 0);
		result.left = new FormAttachment(assignmentTypeCombo, 0);
		result.right = new FormAttachment(100, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	public void widgetSelected(SelectionEvent e) {
		Composite newPage = null;
		Object id = labelMap.get(assignmentTypeCombo.getText());
		if (id != null) {
			newPage = (Composite)pagesMap.get(id);
		}
		if (newPage != activePage) {
			if (activePage != null) {
				activePage.setVisible(false);
				((AssignmentTypeComposite)activePage.getData()).setActive(false);
			}
			activePage = newPage;
			if (activePage != null) {
				activePage.setVisible(true);
				((AssignmentTypeComposite)activePage.getData()).setActive(true);
			}
		}
		if (id == null) {
			assignable.setAssignment(null);
		}
	}
	
}
