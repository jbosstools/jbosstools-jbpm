package org.jbpm.gd.jpdl.properties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.properties.AbstractPropertySection;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.model.ProcessDefinition;
import org.jbpm.gd.jpdl.model.Swimlane;

public class SwimlaneContainerSection
	extends AbstractPropertySection implements PropertyChangeListener, SelectionListener, FocusListener {
	
	private Composite detailsArea;	
	private Table swimlaneTable;	
    private ProcessDefinition processDefinition;
    private Swimlane selectedSwimlane;
    private SwimlaneConfigurationComposite swimlaneConfigurationComposite;
    private TabbedPropertySheetPage tabbedPropertySheetPage;    
    private SwimlaneContainerSectionActionBarContributor actionBarContributor;
    
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);	
		actionBarContributor = new SwimlaneContainerSectionActionBarContributor(this);
		tabbedPropertySheetPage = aTabbedPropertySheetPage;
		final Composite composite = getWidgetFactory().createFlatFormComposite(parent);		
		createMasterArea(composite);
		createDetailsArea(composite);
	}
	
	public TabbedPropertySheetPage getTabbedPropertySheetPage() {
		return tabbedPropertySheetPage;
	}

	private void createMasterArea(Composite composite) {
		swimlaneTable = getWidgetFactory().createTable(
				composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		swimlaneTable.setLayoutData(createSwimlaneListLayoutData());
		swimlaneTable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleSwimlaneListSelection();
			}			
		});
		actionBarContributor.createPopupMenu(swimlaneTable);
	}
	
	private void handleSwimlaneListSelection() {
		if (processDefinition == null) return;
		int i = swimlaneTable.getSelectionIndex();
		if (i == -1) {
			setSelectedSwimlane(null);
		} else {
			setSelectedSwimlane((Swimlane)swimlaneTable.getSelection()[0].getData());
		}
	}

	private FormData createSwimlaneListLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(20, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	private void createDetailsArea(Composite composite) {
		detailsArea = getWidgetFactory().createComposite(composite);
		detailsArea.setLayout(new FormLayout());
		detailsArea.setLayoutData(createDetailsAreaLayoutData());		
		createSwimlaneConfigurationComposite();
	}
	
	private void createSwimlaneConfigurationComposite() {
		swimlaneConfigurationComposite = SwimlaneConfigurationComposite.create(getWidgetFactory(), detailsArea);
	}

	private FormData createDetailsAreaLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(swimlaneTable, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	public boolean shouldUseExtraSpace() {
		return true;
	}
	
	public void aboutToBeShown() {
		actionBarContributor.activateContributions();
	}
	
	public void aboutToBeHidden() {
		actionBarContributor.deactivateContributions();
	}
	
 	public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        if (!(selection instanceof IStructuredSelection)) return;
        Object input = ((IStructuredSelection)selection).getFirstElement();
        if (input instanceof NotationElementGraphicalEditPart) {
        	AbstractNotationElement notationElement = ((NotationElementGraphicalEditPart)input).getNotationElement();
        	setProcessDefinition((ProcessDefinition)notationElement.getSemanticElement());
        } else if (input instanceof OutlineEditPart) {
        	setProcessDefinition((ProcessDefinition)((OutlineEditPart)input).getModel());
        }
    }
 	
 	public void clearControls() {
 		setSelectedSwimlane(null);
 		swimlaneTable.removeAll();
 		clearSwimlaneInfo();
 	}
 	
 	private void clearSwimlaneInfo() {
 		detailsArea.setVisible(false);
 	}
 	
 	public void setProcessDefinition(ProcessDefinition newProcessDefinition) {
 		if (processDefinition == newProcessDefinition) return;
 		if (processDefinition != null) {
 			processDefinition.removePropertyChangeListener(this);
 		}
 		clearControls();
 		processDefinition = newProcessDefinition;
 		if (processDefinition != null) {
 			updateControls();
	 		processDefinition.addPropertyChangeListener(this);
 		}
 	}
 	
 	private void updateControls() {
 		Swimlane[] swimlanes = processDefinition.getSwimlanes();
 		for (int i = 0; i < swimlanes.length; i++) {
 			TableItem tableItem = new TableItem(swimlaneTable, SWT.NULL);
 			tableItem.setText(swimlanes[i].getName());
 			tableItem.setData(swimlanes[i]);
 			tableItem.setImage(SharedImages.INSTANCE.getImage(swimlanes[i].getIconDescriptor()));
 		}
 		detailsArea.setVisible(selectedSwimlane != null);
 	}
 	
 	public ProcessDefinition getProcessDefinition() {
 		return processDefinition;
 	}
 	
 	private void hookListeners() {
 	}
 	
 	private void unhookListeners() {
 	}
 	
 	private void setSelectedSwimlane(Swimlane swimlane) {
 		unhookListeners();
        if (selectedSwimlane != null) {
        	selectedSwimlane.removePropertyChangeListener(this);
        }
 		selectedSwimlane = swimlane;
 		updateSwimlaneConfigurationComposite();
 		if (selectedSwimlane != null) {
 			selectedSwimlane.addPropertyChangeListener(this);
 	 		hookListeners();
 		}
 		actionBarContributor.setRemoveSwimlaneEnabled(selectedSwimlane != null);
 		detailsArea.setVisible(selectedSwimlane != null);
 	}
 	
 	private void updateSwimlaneConfigurationComposite() { 	
 		swimlaneConfigurationComposite.setSwimlane(selectedSwimlane);
 	}

 	public Swimlane getSelectedSwimlane() {
 		return selectedSwimlane;
 	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (swimlaneTable.isDisposed()) return;
		if ("swimlaneAdd".equals(evt.getPropertyName())) {
			TableItem tableItem = new TableItem(swimlaneTable, SWT.NULL);
			Swimlane swimlane = (Swimlane)evt.getNewValue();
			tableItem.setText(swimlane.getName());
			tableItem.setData(swimlane);
			tableItem.setImage(SharedImages.INSTANCE.getImage(swimlane.getIconDescriptor()));
			swimlaneTable.setSelection(tableItem);
			swimlaneTable.notifyListeners(SWT.Selection, new Event());
		} else if ("swimlaneRemove".equals(evt.getPropertyName())) {
			TableItem tableItem = getItemToRemove(evt.getOldValue());
			if (tableItem != null) {
				tableItem.dispose();
				swimlaneTable.notifyListeners(SWT.Selection, new Event());
			}
		} else if ("name".equals(evt.getPropertyName())) {
			if (swimlaneTable.getSelectionCount() != 1) return;
			TableItem tableItem = swimlaneTable.getItem(swimlaneTable.getSelectionIndex());
			if (tableItem.getData() == evt.getSource()) {
				tableItem.setText((String)evt.getNewValue());
			}
		}
	}
	
	private TableItem getItemToRemove(Object object) {
		for (int i = 0; i < swimlaneTable.getItemCount(); i++) {
			if (swimlaneTable.getItem(i).getData() == object)
				return swimlaneTable.getItem(i);
		}
		return null;
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
