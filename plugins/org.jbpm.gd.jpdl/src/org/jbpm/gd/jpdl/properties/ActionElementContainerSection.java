package org.jbpm.gd.jpdl.properties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.properties.AbstractPropertySection;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.ActionElement;
import org.jbpm.gd.jpdl.model.ActionElementContainer;
import org.jbpm.gd.jpdl.model.CancelTimer;
import org.jbpm.gd.jpdl.model.CreateTimer;
import org.jbpm.gd.jpdl.model.EsbElement;
import org.jbpm.gd.jpdl.model.EsbNotifier;
import org.jbpm.gd.jpdl.model.MailAction;
import org.jbpm.gd.jpdl.model.Script;


public class ActionElementContainerSection extends AbstractPropertySection implements PropertyChangeListener {
	
    private TabbedPropertySheetPage tabbedPropertySheetPage;	
    private ActionElementContainer actionElementContainer;
    private ActionElement selectedActionElement;
    private ActionElementContainerSectionActionBarContributor actionBarContributor;
    
    private Table actionElementTable;
    
    private HashMap detailsPages = new HashMap();
    private ActionConfigurationComposite actionConfigurationComposite;
    private ScriptConfigurationComposite scriptConfigurationComposite;
    private TimerConfigurationComposite createTimerConfigurationComposite;
    private CancelTimerConfigurationComposite cancelTimerConfigurationComposite;
    private MailActionConfigurationComposite mailActionConfigurationComposite;
    private EsbConfigurationComposite esbConfigurationComposite;
	 	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);	
		actionBarContributor = new ActionElementContainerSectionActionBarContributor(this);
		tabbedPropertySheetPage = aTabbedPropertySheetPage;
		final Composite composite = getWidgetFactory().createFlatFormComposite(parent);		
		createMasterArea(composite);
		createDetailsArea(composite);
	}
	
	private void createMasterArea(Composite composite) {
		actionElementTable = getWidgetFactory().createTable(
				composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		actionElementTable.setLayoutData(createActionElementTableLayoutData());
		actionElementTable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleActionTableSelected();
			}			
		});
		actionBarContributor.createPopupMenu(actionElementTable);
	}
	
	private void createDetailsArea(Composite composite) {
		createActionPage(composite);
		createScriptPage(composite);
		createCreateTimerPage(composite);
		createCancelTimerPage(composite);
		createMailPage(composite);
		createEsbPage(composite);
	}
	
	private void createActionPage(Composite composite) {
		Composite actionPage = getWidgetFactory().createComposite(composite);
		actionPage.setLayout(new FormLayout());
		actionPage.setLayoutData(createDetailsAreaLayoutData());
		detailsPages.put("org.jbpm.gd.jpdl.action", actionPage);
		actionConfigurationComposite = ActionConfigurationComposite.create(getWidgetFactory(), actionPage);
	}
	
	private void createScriptPage(Composite composite) {
		Composite scriptPage = getWidgetFactory().createComposite(composite);
		scriptPage.setLayout(new FormLayout());
		scriptPage.setLayoutData(createDetailsAreaLayoutData());
		detailsPages.put("org.jbpm.gd.jpdl.script", scriptPage);
		scriptConfigurationComposite = ScriptConfigurationComposite.create(getWidgetFactory(), scriptPage);
	}
	
	private void createCreateTimerPage(Composite composite) {
		Composite createTimerPage = getWidgetFactory().createComposite(composite);
		createTimerPage.setLayout(new FormLayout());
		createTimerPage.setLayoutData(createDetailsAreaLayoutData());
		detailsPages.put("org.jbpm.gd.jpdl.createTimer", createTimerPage);
		createTimerConfigurationComposite = TimerConfigurationComposite.create(getWidgetFactory(), createTimerPage);
	}
	
	private void createCancelTimerPage(Composite composite) {
		Composite cancelTimerPage = getWidgetFactory().createComposite(composite);
		cancelTimerPage.setLayout(new FormLayout());
		cancelTimerPage.setLayoutData(createDetailsAreaLayoutData());
		detailsPages.put("org.jbpm.gd.jpdl.cancelTimer", cancelTimerPage);
		cancelTimerConfigurationComposite = CancelTimerConfigurationComposite.create(getWidgetFactory(), cancelTimerPage);
	}
	
	private void createMailPage(Composite composite) {
		Composite mailPage = getWidgetFactory().createComposite(composite);
		mailPage.setLayout(new FormLayout());
		mailPage.setLayoutData(createDetailsAreaLayoutData());
		detailsPages.put("org.jbpm.gd.jpdl.mail", mailPage);
		mailActionConfigurationComposite = MailActionConfigurationComposite.create(getWidgetFactory(), mailPage);
	}
	
	private void createEsbPage(Composite composite) {
		Composite esbPage = getWidgetFactory().createComposite(composite);
		esbPage.setLayout(new FormLayout());
		esbPage.setLayoutData(createDetailsAreaLayoutData());
		detailsPages.put("org.jbpm.gd.jpdl.esbNotifier", esbPage);
		esbConfigurationComposite = EsbConfigurationComposite.create(getWidgetFactory(), esbPage);
	}
	
	private void handleActionTableSelected() {
		TableItem[] selectedItems = actionElementTable.getSelection();
		if (selectedItems.length == 0) {
			unSelectAll();
		} else {
			selectActionElement((ActionElement)selectedItems[0].getData());
		}
		updateDetailsArea();
	}
	
	private void updateDetailsArea() {
		String key = getSelectedKey();
		Iterator iter = detailsPages.keySet().iterator();
		while (iter.hasNext()) {
			String candidate = (String)iter.next();
			((Composite)detailsPages.get(candidate)).setVisible(candidate.equals(key));
		}
	}
	
	private String getSelectedKey() {
		String result = null;
		if (selectedActionElement != null) {
			result = selectedActionElement.getElementId();
		}
		return result;
	}
	
 	private void clearControls() {
		actionElementTable.removeAll();
  		Iterator iter = detailsPages.values().iterator();
 		while (iter.hasNext()) {
 			((Composite)iter.next()).setVisible(false);
 		}
 	}
 	
	private void selectActionElement(ActionElement actionElement) {
		if (actionElement == getSelectedActionElement()) return;
		actionBarContributor.setRemoveEnabled(actionElement != null);
		if (selectedActionElement != null) {
			selectedActionElement.removePropertyChangeListener(this);
		}
		selectedActionElement = actionElement;
		updateActionElementDetails();
		if (actionElement != null) {
			actionElement.addPropertyChangeListener(this);
		}
	}
	
	private void updateActionElementDetails() {
		if (selectedActionElement instanceof EsbNotifier) {
			esbConfigurationComposite.setEsbElement((EsbElement)selectedActionElement);
		} else if (selectedActionElement instanceof Action) {
			actionConfigurationComposite.setAction((Action)selectedActionElement);
		} else if (selectedActionElement instanceof Script) {
			scriptConfigurationComposite.setScript((Script)selectedActionElement);
		} else if (selectedActionElement instanceof CreateTimer) {
			createTimerConfigurationComposite.setTimer((CreateTimer)selectedActionElement);
		} else if (selectedActionElement instanceof CancelTimer) {
			cancelTimerConfigurationComposite.setCancelTimer((CancelTimer)selectedActionElement);
		} else if (selectedActionElement instanceof MailAction) {
			mailActionConfigurationComposite.setMailAction((MailAction)selectedActionElement);
		}
	}
	
	private FormData createActionElementTableLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(20, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createDetailsAreaLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(actionElementTable, 0);
		data.top = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
 	public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        if (!(selection instanceof IStructuredSelection)) return;
        Object input = ((IStructuredSelection)selection).getFirstElement();
        if (input instanceof NotationElementGraphicalEditPart) {
        	AbstractNotationElement notationElement = ((NotationElementGraphicalEditPart)input).getNotationElement();
        	setActionElementContainer((ActionElementContainer)notationElement.getSemanticElement());
        } else if (input instanceof OutlineEditPart) {
        	setActionElementContainer((ActionElementContainer)((OutlineEditPart)input).getModel());
        }
    }
 	
 	private void setActionElementContainer(ActionElementContainer newActionElementContainer) {
 		if (actionElementContainer == newActionElementContainer) return;
 		if (actionElementContainer != null) {
 			actionElementContainer.removePropertyChangeListener(this);
 		}
 		clearControls();
 		actionElementContainer = newActionElementContainer;
 		if (actionElementContainer != null) {
 			updateActionElementTable();
 			actionElementContainer.addPropertyChangeListener(this);
 		} 		
 	}
 	
	private void unSelectAll() {
		if (somethingSelected()) {
			selectActionElement(null);
		}
	}

	private boolean somethingSelected() {
		return getSelectedActionElement() != null;
	}

 	private void updateActionElementTable() {
 		ActionElement[] actionElements = actionElementContainer.getActionElements();
 		for (int i = 0; i < actionElements.length; i++) {
 			TableItem tableItem = new TableItem(actionElementTable, SWT.NULL);
 			tableItem.setText(getLabel(actionElements[i]));
 			tableItem.setData(actionElements[i]);
 			tableItem.setImage(SharedImages.INSTANCE.getImage(actionElements[i].getIconDescriptor()));
 		}
 	}
 	
 	private String getLabel(ActionElement actionElement) {
 		String result = actionElement.getName();
 		if (result != null && !"".equals(result)) return result;
 		if (actionElement instanceof Action) {
 			String refName = ((Action)actionElement).getRefName();
 			if (refName != null && !"".equals(refName)) {
 				return refName;
 			}
 		} 
 		result = actionElement.getLabel();
 		return result != null ? result.toLowerCase() : actionElement.getElementId();
 	}
 	
	public TabbedPropertySheetPage getTabbedPropertySheetPage() {
		return tabbedPropertySheetPage;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (actionElementTable.isDisposed()) return;
		if ("actionElementAdd".equals(evt.getPropertyName())) {
			TableItem tableItem = new TableItem(actionElementTable, SWT.NULL);
			ActionElement actionElement = (ActionElement)evt.getNewValue();
			tableItem.setText(getLabel(actionElement));
			tableItem.setData(actionElement);
			tableItem.setImage(SharedImages.INSTANCE.getImage(actionElement.getIconDescriptor()));
			actionElementTable.setSelection(tableItem);
			actionElementTable.notifyListeners(SWT.Selection, new org.eclipse.swt.widgets.Event());
		} else if ("actionElementRemove".equals(evt.getPropertyName())) {
			TableItem tableItem = getItemToRemove(evt.getOldValue());
			if (tableItem != null) {
				tableItem.dispose();
				actionElementTable.notifyListeners(SWT.Selection, new org.eclipse.swt.widgets.Event());
			}
		} else if ("name".equals(evt.getPropertyName()) || "refName".equals(evt.getPropertyName())) {
			if (actionElementTable.getSelectionCount() != 1) return;
			TableItem tableItem = actionElementTable.getSelection()[0];
			if (tableItem.getData() == evt.getSource()) {
				tableItem.setText(getLabel((ActionElement)tableItem.getData()));
			}
		}
	}
	
	private TableItem getItemToRemove(Object object) {
		for (int i = 0; i < actionElementTable.getItemCount(); i++) {
			if (actionElementTable.getItem(i).getData() == object)
				return actionElementTable.getItem(i);
		}
		return null;
	}
	
	public ActionElement getSelectedActionElement() {
		return selectedActionElement;
	}

	public void aboutToBeShown() {
		actionBarContributor.activateContributions();
	}
	
	public void aboutToBeHidden() {
		actionBarContributor.deactivateContributions();
	}
	
	public ActionElementContainer getActionElementContainer() {
		return actionElementContainer;
	}

}