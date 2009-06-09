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
import org.jbpm.gd.jpdl.model.ExceptionHandler;
import org.jbpm.gd.jpdl.model.ExceptionHandlerContainer;
import org.jbpm.gd.jpdl.model.Script;


public class ExceptionHandlerContainerSection extends AbstractPropertySection implements PropertyChangeListener {
	
    private TabbedPropertySheetPage tabbedPropertySheetPage;	
	private Tree exceptionHandlerTree;
	private ExceptionHandlerContainer exceptionHandlerContainer;
    private ExceptionHandlerContainerSectionActionBarContributor actionBarContributor;
    
    private ExceptionHandlerConfigurationComposite exceptionHandlerConfigurationComposite;
    private ActionConfigurationComposite actionConfigurationComposite;
    private ScriptConfigurationComposite scriptConfigurationComposite;
    
    private ExceptionHandler selectedExceptionHandler;
    private ActionElement selectedActionElement;
    
    private HashMap detailsPages = new HashMap();
	 	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);	
		actionBarContributor = new ExceptionHandlerContainerSectionActionBarContributor(this);
		tabbedPropertySheetPage = aTabbedPropertySheetPage;
		final Composite composite = getWidgetFactory().createFlatFormComposite(parent);		
		createMasterArea(composite);
		createDetailsArea(composite);
	}
	
	private void createMasterArea(Composite composite) {
		exceptionHandlerTree = getWidgetFactory().createTree(
				composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		exceptionHandlerTree.setLayoutData(createExceptionHandlerTreeLayoutData());
		exceptionHandlerTree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleEventTreeSelection();
			}			
		});
		actionBarContributor.createPopupMenu(exceptionHandlerTree);
	}
	
	private void handleEventTreeSelection() {
		TreeItem[] selectedItems = exceptionHandlerTree.getSelection();
		if (selectedItems.length == 0) {
			unSelectAll();
		} else {
			SemanticElement element = (SemanticElement)selectedItems[0].getData();
			if (element instanceof ExceptionHandler) {
				selectExceptionHandler((ExceptionHandler)element);
			} else if (element instanceof ActionElement) {
				selectActionElement((ActionElement)element);
			}
		}
		updateActionBarContributor();
		updateDetailsArea();
	}
	
	private void updateActionBarContributor() {
		boolean enabled = getSelectedExceptionHandler() != null;
		actionBarContributor.setAddActionElementEnabled(enabled);
		actionBarContributor.setRemoveEnabled(enabled);
	}
	
	private boolean somethingSelected() {
		return getSelectedExceptionHandler() != null  || getSelectedActionElement() != null;
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
		if (getSelectedActionElement() != null) {
			result = getSelectedActionElement().getElementId();
		} else if (getSelectedExceptionHandler() != null) {
			result = getSelectedExceptionHandler().getElementId();
		}
		return result;
	}
	
	private void unSelectAll() {
		if (somethingSelected()) {
			selectActionElement(null);
			selectExceptionHandler(null);
		}
		
	}
	
	private void selectExceptionHandler(ExceptionHandler exceptionHandler) {
		if (exceptionHandler == getSelectedExceptionHandler() && selectedActionElement == null) return;
		actionBarContributor.setAddActionElementEnabled(exceptionHandler != null);
		actionBarContributor.setRemoveEnabled(exceptionHandler != null);
		if (selectedActionElement != null) {
			selectedActionElement.removePropertyChangeListener(this);
		}
		if (selectedExceptionHandler != null) {
			selectedExceptionHandler.removePropertyChangeListener(this);
		}
		selectedActionElement = null;
		selectedExceptionHandler = exceptionHandler;
		exceptionHandlerConfigurationComposite.setExceptionHandler(exceptionHandler);
		if (selectedExceptionHandler != null) {
			selectedExceptionHandler.addPropertyChangeListener(this);
		}
	}
	
	private void selectActionElement(ActionElement actionElement) {
		if (actionElement == getSelectedActionElement()) return;
		ExceptionHandler exceptionHandler = null;
		if (actionElement != null) {
			exceptionHandler = (ExceptionHandler)exceptionHandlerTree.getSelection()[0].getParentItem().getData();
		}
		selectExceptionHandler(exceptionHandler);
		selectedActionElement = actionElement;
		updateActionElementDetails();
		if (selectedActionElement != null) {
			selectedActionElement.addPropertyChangeListener(this);
		}
	}
	
	private void updateActionElementDetails() {
		if (selectedActionElement instanceof Action) {
			actionConfigurationComposite.setAction((Action)selectedActionElement);
		} else if (selectedActionElement instanceof Script) {
			scriptConfigurationComposite.setScript((Script)selectedActionElement);
		}
	}
	
	private FormData createExceptionHandlerTreeLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(20, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	private void createDetailsArea(Composite composite) {
		createExceptionHandlerPage(composite);
		createActionPage(composite);
		createScriptPage(composite);
	}
	
	private void createActionPage(Composite composite) {
		Composite actionPage = getWidgetFactory().createComposite(composite);
		actionPage.setLayout(new FormLayout());
		actionPage.setLayoutData(createDetailsPageLayoutData());
		detailsPages.put("org.jbpm.gd.jpdl.action", actionPage);
		actionConfigurationComposite = ActionConfigurationComposite.create(getWidgetFactory(), actionPage);
	}
	
	private void createExceptionHandlerPage(Composite composite) {
		Composite exceptionHandlerPage = getWidgetFactory().createFlatFormComposite(composite);
		exceptionHandlerPage.setLayoutData(createDetailsPageLayoutData());
		detailsPages.put("org.jbpm.gd.jpdl.exceptionHandler", exceptionHandlerPage);
		exceptionHandlerConfigurationComposite = ExceptionHandlerConfigurationComposite.create(getWidgetFactory(), exceptionHandlerPage);
	}
	
	private void createScriptPage(Composite composite) {
		Composite scriptPage = getWidgetFactory().createComposite(composite);
		scriptPage.setLayout(new FormLayout());
		scriptPage.setLayoutData(createDetailsPageLayoutData());
		detailsPages.put("org.jbpm.gd.jpdl.script", scriptPage);
		scriptConfigurationComposite = ScriptConfigurationComposite.create(getWidgetFactory(), scriptPage);
	}
		
	private FormData createDetailsPageLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(exceptionHandlerTree, 0);
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
        	setExceptionHandlerContainer((ExceptionHandlerContainer)notationElement.getSemanticElement());
        } else if (input instanceof OutlineEditPart) {
        	setExceptionHandlerContainer((ExceptionHandlerContainer)((OutlineEditPart)input).getModel());
        }
    }
 	
 	private void clearControls() {
 		exceptionHandlerTree.removeAll();
 		Iterator iter = detailsPages.values().iterator();
 		while (iter.hasNext()) {
 			((Composite)iter.next()).setVisible(false);
 		}
 	}
 	
 	private void setExceptionHandlerContainer(ExceptionHandlerContainer newExceptionHandlerContainer) {
 		if (exceptionHandlerContainer == newExceptionHandlerContainer) return;
 		if (exceptionHandlerContainer != null) {
 			exceptionHandlerContainer.removePropertyChangeListener(this);
 		}
 		clearControls();
 		exceptionHandlerContainer = newExceptionHandlerContainer;
 		if (exceptionHandlerContainer != null) {
 			updateExceptionHandlerTree();
 			exceptionHandlerContainer.addPropertyChangeListener(this);
 		} 		
 	}
 	
 	private void updateExceptionHandlerTree() {
 		ExceptionHandler[] exceptionHandlers = exceptionHandlerContainer.getExceptionHandlers();
 		for (int i = 0; i < exceptionHandlers.length; i++) {
 			TreeItem exceptionHandlerItem = new TreeItem(exceptionHandlerTree, SWT.NULL);
 			exceptionHandlerItem.setText(getLabel(exceptionHandlers[i]));
 			exceptionHandlerItem.setData(exceptionHandlers[i]);
 			exceptionHandlerItem.setImage(SharedImages.INSTANCE.getImage(exceptionHandlers[i].getIconDescriptor()));
 			exceptionHandlerItem.setExpanded(true);
 			ActionElement[] actionElements = exceptionHandlers[i].getActionElements();
 			for (int j = 0; j < actionElements.length; j++) {
 				TreeItem actionElementItem = new TreeItem(exceptionHandlerItem, SWT.NULL);
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
		if (exceptionHandlerTree.isDisposed()) return;
		if ("exceptionHandlerAdd".equals(evt.getPropertyName())) {
			TreeItem treeItem = new TreeItem(exceptionHandlerTree, SWT.NULL);
			ExceptionHandler exceptionHandler = (ExceptionHandler)evt.getNewValue();
			treeItem.setText(getLabel(exceptionHandler));
			treeItem.setData(exceptionHandler);
			treeItem.setImage(SharedImages.INSTANCE.getImage(exceptionHandler.getIconDescriptor()));
			exceptionHandlerTree.setSelection(treeItem);
			exceptionHandlerTree.notifyListeners(SWT.Selection, new org.eclipse.swt.widgets.Event());
		} else if ("actionElementAdd".equals(evt.getPropertyName())) {
			TreeItem selection = exceptionHandlerTree.getSelection()[0];
			if (selection.getData() instanceof ActionElement) {
				selection = selection.getParentItem();
			}
			TreeItem treeItem = new TreeItem(selection, SWT.NULL);
			ActionElement actionElement = (ActionElement)evt.getNewValue();
			treeItem.setText(getLabel(actionElement));
			treeItem.setData(actionElement);
			treeItem.setImage(SharedImages.INSTANCE.getImage(actionElement.getIconDescriptor()));
			exceptionHandlerTree.setSelection(treeItem);
			exceptionHandlerTree.notifyListeners(SWT.Selection, new org.eclipse.swt.widgets.Event());
		} else if ("exceptionHandlerRemove".equals(evt.getPropertyName()) || 
				"actionElementRemove".equals(evt.getPropertyName())) {
			exceptionHandlerTree.getSelection()[0].dispose();
			exceptionHandlerTree.notifyListeners(SWT.Selection, new org.eclipse.swt.widgets.Event());
		} else if ("exceptionClass".equals(evt.getPropertyName())) {
			String newValue = (String)evt.getNewValue();
			if (exceptionHandlerTree.getSelectionCount() != 1) return;
			if (newValue == null || "".equals(newValue)) {
				newValue = "exception-handler";
			}
			TreeItem treeItem = exceptionHandlerTree.getSelection()[0];
			if (treeItem.getData() == evt.getSource()) {
				treeItem.setText(newValue);
			}
		} else if ("name".equals(evt.getPropertyName()) || "refName".equals(evt.getPropertyName())) {
			String newValue = (String)evt.getNewValue();
			if (exceptionHandlerTree.getSelectionCount() != 1) return;
			if (newValue == null || "".equals(newValue)) {
				newValue = "action";
			}
			TreeItem treeItem = exceptionHandlerTree.getSelection()[0];
			if (treeItem.getData() == evt.getSource()) {
				treeItem.setText(newValue);
			}
		}
	}
	
	private String getLabel(ExceptionHandler exceptionHandler) {
		String label = exceptionHandler.getExceptionClass();
		return label == null || "".equals(label) ? "exception" : label;
	}
	
	public boolean shouldUseExtraSpace() {
		return true;
	}
	
	public ExceptionHandlerContainer getExceptionHandlerContainer() {
		return exceptionHandlerContainer;
	}
	
	public ExceptionHandler getSelectedExceptionHandler() {
		return selectedExceptionHandler;
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