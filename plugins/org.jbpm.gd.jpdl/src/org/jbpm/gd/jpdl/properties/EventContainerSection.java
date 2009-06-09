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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.properties.AbstractPropertySection;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.ActionElement;
import org.jbpm.gd.jpdl.model.CancelTimer;
import org.jbpm.gd.jpdl.model.CreateTimer;
import org.jbpm.gd.jpdl.model.EsbElement;
import org.jbpm.gd.jpdl.model.EsbNotifier;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.EventContainer;
import org.jbpm.gd.jpdl.model.MailAction;
import org.jbpm.gd.jpdl.model.Script;


public class EventContainerSection extends AbstractPropertySection implements PropertyChangeListener {
	
    private TabbedPropertySheetPage tabbedPropertySheetPage;	
	private Tree eventTree;
	private EventContainer eventContainer;
    private EventContainerSectionActionBarContributor actionBarContributor;
    private HashMap pagesMap = new HashMap();
    
    private EventConfigurationComposite eventConfigurationComposite;
    private ActionConfigurationComposite actionConfigurationComposite;
    private ScriptConfigurationComposite scriptConfigurationComposite;
    private TimerConfigurationComposite createTimerConfigurationComposite;
    private CancelTimerConfigurationComposite cancelTimerConfigurationComposite;
    private MailActionConfigurationComposite mailConfigurationComposite;
    private EsbConfigurationComposite esbConfigurationComposite;
    
    private Event selectedEvent;
    private ActionElement selectedActionElement;
	 	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);	
		actionBarContributor = new EventContainerSectionActionBarContributor(this);
		tabbedPropertySheetPage = aTabbedPropertySheetPage;
		final Composite composite = getWidgetFactory().createFlatFormComposite(parent);		
		createMasterArea(composite);
		createDetailsArea(composite);
	}
	
	private void createMasterArea(Composite composite) {
		eventTree = getWidgetFactory().createTree(
				composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		eventTree.setLayoutData(createEventTreeLayoutData());
		eventTree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleEventTreeSelection();
			}			
		});
		actionBarContributor.createPopupMenu(eventTree);
	}
	
	private void handleEventTreeSelection() {
		TreeItem[] selectedItems = eventTree.getSelection();
		if (selectedItems.length == 0) {
			unSelectAll();
		} else {
			SemanticElement element = (SemanticElement)selectedItems[0].getData();
			if (element instanceof Event) {
				selectEvent((Event)element);
			} else if (element instanceof ActionElement) {
				selectActionElement((ActionElement)element);
			}
		}
		updateDetailsArea();
	}
	
	private boolean somethingSelected() {
		return getSelectedEvent() != null  || getSelectedActionElement() != null;
	}
	
	private void updateDetailsArea() {
		String id = null;
		if (getSelectedActionElement() != null) {
			id = getSelectedActionElement().getElementId();
		} else if (getSelectedEvent() != null) {
			id = getSelectedEvent().getElementId();
		}
		Iterator iter = pagesMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String)iter.next();
			((Composite)pagesMap.get(key)).setVisible(key.equals(id));
		}
	}
	
	private void unSelectAll() {
		if (somethingSelected()) {
			selectActionElement(null);
		}
	}
	
	private void selectEvent(Event event) {
		if (event == getSelectedEvent() && selectedActionElement == null) return;
		actionBarContributor.setAddActionElementEnabled(event != null);
		actionBarContributor.setRemoveEnabled(event != null);
		if (selectedActionElement != null) {
			selectedActionElement.removePropertyChangeListener(this);
		}
		if (selectedEvent != null) {
			selectedEvent.removePropertyChangeListener(this);
		}
		selectedActionElement = null;
		selectedEvent = event;
		eventConfigurationComposite.setEvent(event);
		if (selectedEvent != null) {
			selectedEvent.addPropertyChangeListener(this);
		}
	}
	
	private void selectActionElement(ActionElement actionElement) {
		if (actionElement == getSelectedActionElement()) return;
		Event event = null;
		if (actionElement != null) {
			event = (Event)eventTree.getSelection()[0].getParentItem().getData();
		}
		selectEvent(event);
		selectedActionElement = actionElement;
		updateActionElementDetails();
		if (selectedActionElement != null) {
			selectedActionElement.addPropertyChangeListener(this);
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
			mailConfigurationComposite.setMailAction((MailAction)selectedActionElement);
		}
	}
	
	private FormData createEventTreeLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(20, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	private void createDetailsArea(Composite composite) {
		createEventPage(composite);
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
		actionPage.setVisible(false);
		actionPage.setLayoutData(createDetailsAreaLayoutData());
		pagesMap.put("org.jbpm.gd.jpdl.action", actionPage);
		actionConfigurationComposite = ActionConfigurationComposite.create(getWidgetFactory(), actionPage);
	}
	
	private void createEventPage(Composite composite) {
		Composite eventPage = getWidgetFactory().createFlatFormComposite(composite);
		eventPage.setVisible(false);
		eventPage.setLayoutData(createDetailsAreaLayoutData());
		pagesMap.put("org.jbpm.gd.jpdl.event", eventPage);
		eventConfigurationComposite = EventConfigurationComposite.create(getWidgetFactory(), eventPage);
	}
	
	private void createScriptPage(Composite composite) {
		Composite scriptPage = getWidgetFactory().createComposite(composite);
		scriptPage.setLayout(new FormLayout());
		scriptPage.setVisible(false);
		scriptPage.setLayoutData(createDetailsAreaLayoutData());
		pagesMap.put("org.jbpm.gd.jpdl.script", scriptPage);
		scriptConfigurationComposite = ScriptConfigurationComposite.create(getWidgetFactory(), scriptPage);
	}
		
	private void createCreateTimerPage(Composite composite) {
		Composite createTimerPage = getWidgetFactory().createComposite(composite);
		createTimerPage.setLayout(new FormLayout());
		createTimerPage.setVisible(false);
		createTimerPage.setLayoutData(createDetailsAreaLayoutData());
		pagesMap.put("org.jbpm.gd.jpdl.createTimer", createTimerPage);
		createTimerConfigurationComposite = TimerConfigurationComposite.create(getWidgetFactory(), createTimerPage);
	}
	
	private void createCancelTimerPage(Composite composite) {
		Composite cancelTimerPage = getWidgetFactory().createComposite(composite);
		cancelTimerPage.setLayout(new FormLayout());
		cancelTimerPage.setVisible(false);
		cancelTimerPage.setLayoutData(createDetailsAreaLayoutData());
		pagesMap.put("org.jbpm.gd.jpdl.cancelTimer", cancelTimerPage);
		cancelTimerConfigurationComposite = CancelTimerConfigurationComposite.create(getWidgetFactory(), cancelTimerPage);
	}
	
	private void createMailPage(Composite composite) {
		Composite mailPage = getWidgetFactory().createComposite(composite);
		mailPage.setLayout(new FormLayout());
		mailPage.setVisible(false);
		mailPage.setLayoutData(createDetailsAreaLayoutData());
		pagesMap.put("org.jbpm.gd.jpdl.mail", mailPage);
		mailConfigurationComposite = MailActionConfigurationComposite.create(getWidgetFactory(), mailPage);
	}
	
	private void createEsbPage(Composite composite) {
		Composite esbPage = getWidgetFactory().createComposite(composite);
		esbPage.setLayout(new FormLayout());
		esbPage.setVisible(false);
		esbPage.setLayoutData(createDetailsAreaLayoutData());
		pagesMap.put("org.jbpm.gd.jpdl.esbNotifier", esbPage);
		esbConfigurationComposite = EsbConfigurationComposite.create(getWidgetFactory(), esbPage);
	}
	
	private FormData createDetailsAreaLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(eventTree, 0);
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
        	setEventContainer((EventContainer)notationElement.getSemanticElement());
        } else if (input instanceof OutlineEditPart) {
        	setEventContainer((EventContainer)((OutlineEditPart)input).getModel());
        }
    }
 	
 	private void clearControls() {
 		eventTree.removeAll();
 		Iterator iter = pagesMap.keySet().iterator();
 		while (iter.hasNext()) {
 			((Composite)pagesMap.get(iter.next())).setVisible(false);
 		}
 	}
 	
 	private void setEventContainer(EventContainer newEventContainer) {
 		if (eventContainer == newEventContainer) return;
 		if (eventContainer != null) {
 			eventContainer.removePropertyChangeListener(this);
 		}
 		clearControls();
 		eventContainer = newEventContainer;
 		if (eventContainer != null) {
 			updateEventTree();
 			eventContainer.addPropertyChangeListener(this);
 		} 		
 	}
 	
 	private void updateEventTree() {
 		Event[] events = eventContainer.getEvents();
 		for (int i = 0; i < events.length; i++) {
 			TreeItem eventItem = new TreeItem(eventTree, SWT.NULL);
 			eventItem.setText(getLabel(events[i]));
 			eventItem.setData(events[i]);
 			eventItem.setImage(SharedImages.INSTANCE.getImage(events[i].getIconDescriptor()));
 			eventItem.setExpanded(true);
 			ActionElement[] actionElements = events[i].getActionElements();
 			for (int j = 0; j < actionElements.length; j++) {
 				TreeItem actionElementItem = new TreeItem(eventItem, SWT.NULL);
 				actionElementItem.setText(getLabel(actionElements[j]));
 				actionElementItem.setData(actionElements[j]);
 				actionElementItem.setImage(SharedImages.INSTANCE.getImage(actionElements[j].getIconDescriptor()));
 			}
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
		if (eventTree.isDisposed()) return;
		if ("eventAdd".equals(evt.getPropertyName())) {
			TreeItem treeItem = new TreeItem(eventTree, SWT.NULL);
			Event event = (Event)evt.getNewValue();
			treeItem.setText(getLabel(event));
			treeItem.setData(event);
			treeItem.setImage(SharedImages.INSTANCE.getImage(event.getIconDescriptor()));
			eventTree.setSelection(treeItem);
			eventTree.notifyListeners(SWT.Selection, new org.eclipse.swt.widgets.Event());
		} else if ("actionElementAdd".equals(evt.getPropertyName())) {
			TreeItem selection = eventTree.getSelection()[0];
			if (selection.getData() instanceof ActionElement) {
				selection = selection.getParentItem();
			}
			TreeItem treeItem = new TreeItem(selection, SWT.NULL);
			ActionElement actionElement = (ActionElement)evt.getNewValue();
			treeItem.setText(getLabel(actionElement));
			treeItem.setData(actionElement);
			treeItem.setImage(SharedImages.INSTANCE.getImage(actionElement.getIconDescriptor()));
			eventTree.setSelection(treeItem);
			eventTree.notifyListeners(SWT.Selection, new org.eclipse.swt.widgets.Event());
		} else if ("eventRemove".equals(evt.getPropertyName()) || 
				"actionElementRemove".equals(evt.getPropertyName())) {
			if (eventTree.getSelectionCount() != 1) return;
			eventTree.getSelection()[0].dispose();
			eventTree.notifyListeners(SWT.Selection, new org.eclipse.swt.widgets.Event());
		} else if ("type".equals(evt.getPropertyName())) {
			if (eventTree.getSelectionCount() != 1) return;
			String newValue = (String)evt.getNewValue();
			if (newValue == null || "".equals(newValue)) {
				newValue = "event";
			}
			TreeItem treeItem = eventTree.getSelection()[0];
			if (treeItem.getData() == evt.getSource()) {
				treeItem.setText(newValue);
			}
		} else if ("name".equals(evt.getPropertyName()) || "refName".equals(evt.getPropertyName())) {
			if (eventTree.getSelectionCount() != 1) return;
			String newValue = (String)evt.getNewValue();
			if (newValue == null || "".equals(newValue)) {
				newValue = "action";
			}
			TreeItem treeItem = eventTree.getSelection()[0];
			if (treeItem.getData() == evt.getSource()) {
				treeItem.setText(newValue);
			}
		}
	}
	
	private String getLabel(Event event) {
		String label = event.getType();
		return label == null || "".equals(label) ? "event" : label;
	}
	
	public boolean shouldUseExtraSpace() {
		return true;
	}
	
	public EventContainer getEventContainer() {
		return eventContainer;
	}
	
	public Event getSelectedEvent() {
		return selectedEvent;
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
	
}