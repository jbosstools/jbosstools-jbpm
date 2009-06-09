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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.properties.AbstractPropertySection;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.model.Task;
import org.jbpm.gd.jpdl.model.TaskContainer;

public class TaskContainerSection
	extends AbstractPropertySection implements PropertyChangeListener {
	
	private Composite detailsArea;	
	private Tree taskTree;	
	
	private TaskConfigurationComposite taskConfigurationComposite;
	
    private TaskContainer taskContainer;
    private Task task;
    private TabbedPropertySheetPage tabbedPropertySheetPage;    
    private TaskContainerSectionActionBarContributor actionBarContributor;
    
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);	
		actionBarContributor = new TaskContainerSectionActionBarContributor(this);
		tabbedPropertySheetPage = aTabbedPropertySheetPage;
		final Composite composite = getWidgetFactory().createFlatFormComposite(parent);		
		createMasterArea(composite);
		createDetailsArea(composite);
	}
	
	public TabbedPropertySheetPage getTabbedPropertySheetPage() {
		return tabbedPropertySheetPage;
	}

	private void createMasterArea(Composite composite) {
		taskTree = getWidgetFactory().createTree(
				composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		taskTree.setLayoutData(createTaskTreeLayoutData());
		taskTree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleTaskTreeSelected();
			}			
		});
		actionBarContributor.createPopupMenu(taskTree);
	}
	
	private void handleTaskTreeSelected() {
		if (taskContainer == null) return;
		if (taskTree.getSelectionCount() == 0) {
			setSelectedTask(null);
		} else {
			setSelectedTask((Task)taskTree.getSelection()[0].getData());
		}
	}

	private FormData createTaskTreeLayoutData() {
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
		taskConfigurationComposite = TaskConfigurationComposite.create(getWidgetFactory(), detailsArea);
	}
	
	private FormData createDetailsAreaLayoutData() {
		FormData data = new FormData();
		data.left = new FormAttachment(taskTree, 0);
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
        	setTaskContainer((TaskContainer)notationElement.getSemanticElement());
        } else if (input instanceof OutlineEditPart) {
        	setTaskContainer((TaskContainer)((OutlineEditPart)input).getModel());
        }
    }
 	
 	public void clearControls() {
 		setSelectedTask(null);
 		taskTree.removeAll();
  	}
 	
 	public void setTaskContainer(TaskContainer newTaskContainer) {
 		if (taskContainer == newTaskContainer) return;
 		if (taskContainer != null) {
 			taskContainer.removePropertyChangeListener(this);
 		}
 		clearControls();
 		taskContainer = newTaskContainer;
 		if (taskContainer != null) {
 			updateTaskTree();
 			taskContainer.addPropertyChangeListener(this);
 		}
 	}
 	
 	private void updateTaskTree() {
 		Task[] tasks = taskContainer.getTasks();
 		for (int i = 0; i < tasks.length; i++) {
 			TreeItem treeItem = new TreeItem(taskTree, SWT.NULL);
 			treeItem.setText(getTaskLabel(tasks[i]));
 			treeItem.setData(tasks[i]);
 			treeItem.setImage(SharedImages.INSTANCE.getImage(tasks[i].getIconDescriptor()));
 		}
 	}
 	
 	private String getTaskLabel(Task task) {
 		String name = task.getName();
 		return name == null || "".equals(name)? "task" : name;
 	}
 	
 	private void setSelectedTask(Task newTask) {
        if (task != null) {
        	task.removePropertyChangeListener(this);
        }
 		task = newTask;
 		taskConfigurationComposite.setTask(task);
 		if (task != null) {
 			task.addPropertyChangeListener(this);
 		}
 		actionBarContributor.setRemoveEnabled(task != null);
 		detailsArea.setVisible(task != null);
 	}
 	 	
 	public TaskContainer getTaskContainer() {
 		return taskContainer;
 	}

	public Task getSelectedTask() {
		return task;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (taskTree.isDisposed()) return;
		if ("taskAdd".equals(evt.getPropertyName())) {
			TreeItem treeItem = new TreeItem(taskTree, SWT.NULL);
			Task task = (Task)evt.getNewValue();
			treeItem.setText(getTaskLabel(task));
			treeItem.setData(task);
			treeItem.setImage(SharedImages.INSTANCE.getImage(task.getIconDescriptor()));
			taskTree.setSelection(treeItem);
			taskTree.notifyListeners(SWT.Selection, new Event());
		} else if ("taskRemove".equals(evt.getPropertyName())) {
			TreeItem treeItem = getItemToRemove(evt.getOldValue());
			if (treeItem != null) {
				treeItem.dispose();
			}
			taskTree.notifyListeners(SWT.Selection, new Event());
		} else if ("name".equals(evt.getPropertyName())) {
			if (taskTree.getSelectionCount() != 1) return;
			TreeItem treeItem = taskTree.getSelection()[0];
			if (treeItem.getData() == evt.getSource()) {
				treeItem.setText(getTaskLabel(task));
			}
		}
	}
	
	private TreeItem getItemToRemove(Object object) {
		for (int i = 0; i < taskTree.getItemCount(); i++) {
			if (taskTree.getItem(i).getData() == object)
				return taskTree.getItem(i);
		}
		return null;
	}
	
}
