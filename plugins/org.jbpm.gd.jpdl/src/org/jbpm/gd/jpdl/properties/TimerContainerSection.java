package org.jbpm.gd.jpdl.properties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
import org.jbpm.gd.jpdl.model.Timer;
import org.jbpm.gd.jpdl.model.TimerContainer;


public class TimerContainerSection extends AbstractPropertySection implements PropertyChangeListener {
	
    private TabbedPropertySheetPage tabbedPropertySheetPage;	
    private TimerContainer timerContainer;
    private Timer selectedTimer;
    private TimerContainerSectionActionBarContributor actionBarContributor;
    
    private Table timerTable;
    private Composite detailsArea;
    private TimerConfigurationComposite timerConfigurationComposite;
	 	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);	
		actionBarContributor = new TimerContainerSectionActionBarContributor(this);
		tabbedPropertySheetPage = aTabbedPropertySheetPage;
		final Composite composite = getWidgetFactory().createFlatFormComposite(parent);		
		createMasterArea(composite);
		createDetailsArea(composite);
	}
	
	private void createMasterArea(Composite composite) {
		timerTable = getWidgetFactory().createTable(
				composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		timerTable.setLayoutData(createTimerTableLayoutData());
		timerTable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleTimerTableSelected();
			}			
		});
		actionBarContributor.createPopupMenu(timerTable);
	}
	
	private void createDetailsArea(Composite composite) {
		detailsArea = getWidgetFactory().createComposite(composite);
		detailsArea.setLayout(new FormLayout());
		detailsArea.setLayoutData(createDetailsAreaLayoutData());
		detailsArea.setVisible(false);
		timerConfigurationComposite = TimerConfigurationComposite.create(getWidgetFactory(), detailsArea);
	}
	
	private void handleTimerTableSelected() {
		TableItem[] selectedItems = timerTable.getSelection();
		if (selectedItems.length == 0) {
			unSelectAll();
		} else {
			selectTimer((Timer)selectedItems[0].getData());
		}
		detailsArea.setVisible(selectedItems.length != 0);
	}
	
 	private void clearControls() {
		timerTable.removeAll();
		timerConfigurationComposite.setTimer(null);
		detailsArea.setVisible(false);
 	}
 	
	private void selectTimer(Timer timer) {
		if (timer == selectedTimer) return;
		actionBarContributor.setRemoveEnabled(timer != null);
		if (selectedTimer != null) {
			selectedTimer.removePropertyChangeListener(this);
		}
		selectedTimer = timer;
		updateTimerDetails();
		if (selectedTimer != null) {
			selectedTimer.addPropertyChangeListener(this);
		}
	}
	
	private void updateTimerDetails() {
		timerConfigurationComposite.setTimer(selectedTimer);
	}
	
	private FormData createTimerTableLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(20, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		return data;
	}
	
	private FormData createDetailsAreaLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(timerTable, 0);
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
        	setTimerContainer((TimerContainer)notationElement.getSemanticElement());
        } else if (input instanceof OutlineEditPart) {
        	setTimerContainer((TimerContainer)((OutlineEditPart)input).getModel());
        }
    }
 	
 	private void setTimerContainer(TimerContainer newTimerContainer) {
 		if (timerContainer == newTimerContainer) return;
 		if (timerContainer != null) {
 			timerContainer.removePropertyChangeListener(this);
 		}
 		clearControls();
 		timerContainer = newTimerContainer;
 		if (timerContainer != null) {
 			updateTimerTable();
 			timerContainer.addPropertyChangeListener(this);
 		} 		
 	}
 	
	private void unSelectAll() {
		if (selectedTimer != null) {
			selectTimer(null);
		}
	}

 	private void updateTimerTable() {
 		Timer[] timers = timerContainer.getTimers();
 		for (int i = 0; i < timers.length; i++) {
 			TableItem tableItem = new TableItem(timerTable, SWT.NULL);
 			tableItem.setText(getLabel(timers[i]));
 			tableItem.setData(timers[i]);
 			tableItem.setImage(SharedImages.INSTANCE.getImage(timers[i].getIconDescriptor()));
 		}
 	}
 	
 	private String getLabel(Timer timer) {
 		String result = timer.getName();
 		if (result != null && !"".equals(result)) return result;
 		result = timer.getLabel();
 		return result != null ? result.toLowerCase() : timer.getElementId();
 	}
 	
	public TabbedPropertySheetPage getTabbedPropertySheetPage() {
		return tabbedPropertySheetPage;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (timerTable.isDisposed()) return;
		if ("timerAdd".equals(evt.getPropertyName())) {
			TableItem tableItem = new TableItem(timerTable, SWT.NULL);
			Timer timer = (Timer)evt.getNewValue();
			tableItem.setText(getLabel(timer));
			tableItem.setData(timer);
			tableItem.setImage(SharedImages.INSTANCE.getImage(timer.getIconDescriptor()));
			timerTable.setSelection(tableItem);
			timerTable.notifyListeners(SWT.Selection, new org.eclipse.swt.widgets.Event());
		} else if ("actionElementRemove".equals(evt.getPropertyName())) {
			TableItem tableItem = getItemToRemove(evt.getOldValue());
			if (tableItem != null) {
				tableItem.dispose();
				timerTable.notifyListeners(SWT.Selection, new org.eclipse.swt.widgets.Event());
			}
		} else if ("name".equals(evt.getPropertyName()) || "refName".equals(evt.getPropertyName())) {
			if (timerTable.getSelectionCount() != 1) return;
			TableItem tableItem = timerTable.getSelection()[0];
			if (tableItem.getData() == evt.getSource()) {
				tableItem.setText(getLabel((Timer)tableItem.getData()));
			}
		}
	}
	
	private TableItem getItemToRemove(Object object) {
		for (int i = 0; i < timerTable.getItemCount(); i++) {
			if (timerTable.getItem(i).getData() == object)
				return timerTable.getItem(i);
		}
		return null;
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

	public TimerContainer getTimerContainer() {
		return timerContainer;
	}
	
	public Timer getSelectedTimer() {
		return selectedTimer;
	}
	
}