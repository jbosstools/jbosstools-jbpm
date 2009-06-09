package org.jbpm.gd.jpdl.properties;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.properties.AbstractPropertySection;
import org.jbpm.gd.jpdl.model.StartState;
import org.jbpm.gd.jpdl.model.Task;


public class StartStateTaskSection extends AbstractPropertySection {
	
	private TaskConfigurationComposite taskConfigurationComposite;
	private Composite taskInfoArea;
	private StartState startState;
	
	private Button configureTaskButton;
	
	public void createControls(Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite clientArea = getWidgetFactory().createFlatFormComposite(parent);
        configureTaskButton = getWidgetFactory().createButton(clientArea, "Configure Task", SWT.CHECK);
        configureTaskButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleConfigureTaskButtonSelected();
			}
        });
        taskInfoArea = getWidgetFactory().createComposite(clientArea);
        taskInfoArea.setLayout(new FormLayout());
        taskInfoArea.setVisible(false);
        taskConfigurationComposite = TaskConfigurationComposite.create(getWidgetFactory(), taskInfoArea);
        configureTaskButton.setLayoutData(createConfigureTaskButtonLayoutData());
        taskInfoArea.setLayoutData(createTaskInfoAreaLayoutData());
    }
	
	private void handleConfigureTaskButtonSelected() {
		if (startState != null) {
			if (configureTaskButton.getSelection()) {
				startState.setTask((Task)startState.getFactory().createById("org.jbpm.gd.jpdl.task"));
			} else {
				startState.setTask(null);
			}
			taskInfoArea.setVisible(configureTaskButton.getSelection());
		}
		refresh();
	}
	
	private FormData createConfigureTaskButtonLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.top = new FormAttachment(0, 0);
		return result;
	}
	
	private FormData createTaskInfoAreaLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.top = new FormAttachment(configureTaskButton, 0);
		result.right = new FormAttachment(100, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
	}
 
 	public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        if (!(selection instanceof IStructuredSelection)) return;
        Object input = ((IStructuredSelection)selection).getFirstElement();
        if (input instanceof NotationElementGraphicalEditPart) {
        	AbstractNotationElement notationElement = ((NotationElementGraphicalEditPart)input).getNotationElement();
        	input = notationElement.getSemanticElement();
        } else if (input instanceof OutlineEditPart) {
        	input = ((OutlineEditPart)input).getModel();
        }
        if (input instanceof StartState) {
        	startState = (StartState)input;
        	refresh();
        }
    }
 	
 	public void refresh() {
 		if (startState == null){
 			configureTaskButton.setSelection(false);
 			taskConfigurationComposite.setTask(null);
 		} else {
 			configureTaskButton.setSelection(startState.getTask() != null);
 			taskConfigurationComposite.setTask(startState.getTask());
 		}
 		taskInfoArea.setVisible(configureTaskButton.getSelection());
     }
 	
	public boolean shouldUseExtraSpace() {
		return true;
	}
		
}