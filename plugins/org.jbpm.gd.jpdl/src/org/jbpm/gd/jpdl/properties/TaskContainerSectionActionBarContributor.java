package org.jbpm.gd.jpdl.properties;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.Plugin;
import org.jbpm.gd.jpdl.command.TaskCreateCommand;
import org.jbpm.gd.jpdl.command.TaskDeleteCommand;
import org.jbpm.gd.jpdl.model.TaskContainer;


public class TaskContainerSectionActionBarContributor {
	
	private TaskContainerSection tasksSection;
	
	private ActionContributionItem addTaskToolbarContributionItem;
	private ActionContributionItem removeToolbarContributionItem;
	private ActionContributionItem addTaskMenuContributionItem;
	private ActionContributionItem removeMenuContributionItem;
	private Separator toolbarSeparator;
	private Separator menuSeparator;
	
	private Menu root;
	
	public TaskContainerSectionActionBarContributor(TaskContainerSection tasksSection) {
		setTasksSection(tasksSection);
		createAddTaskContributions();
		createSeparatorContributions();
		createRemoveContributions();
	}
	
	private void createSeparatorContributions() {
		toolbarSeparator = new Separator();
		menuSeparator = new Separator();
	}
	
	private void setTasksSection(TaskContainerSection tasksSection) {
		this.tasksSection = tasksSection;
	}
	
	public void activateContributions() {
		addAddTaskAction();
		addSeparators();
		addRemoveAction();
		getActionBars().updateActionBars();
	}
	
	private void addSeparators() {
		getActionBars().getToolBarManager().add(toolbarSeparator);
		getActionBars().getMenuManager().add(menuSeparator);
	}
	
	public void deactivateContributions() {
		removeAddTaskAction();
		removeSeparators();
		removeRemoveAction();
		getActionBars().updateActionBars();
	}
	
	private void removeSeparators() {
		getActionBars().getToolBarManager().remove(toolbarSeparator);
		getActionBars().getMenuManager().remove(menuSeparator);
	}
	
	public void setRemoveEnabled(boolean enabled) {
		removeMenuContributionItem.getAction().setEnabled(enabled);
		removeToolbarContributionItem.getAction().setEnabled(enabled);
	}

	private void addAddTaskAction() {
		getActionBars().getToolBarManager().add(addTaskToolbarContributionItem);
		getActionBars().getMenuManager().add(addTaskMenuContributionItem);
	}
	
	private void createAddTaskContributions() {
		addTaskToolbarContributionItem = new ActionContributionItem(createAddTaskAction());
		addTaskMenuContributionItem = new ActionContributionItem(createAddTaskAction());
	}
	
	private IAction createAddTaskAction() {
		IAction action = new Action() {
			public void run() {
				addNewTask();
			}
		};
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_task_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_task_disabled.gif")));
		action.setToolTipText("Add a task");	
		action.setText("New Task");
		return action;
	}
	
	IContributionItem item;
	
	private void addRemoveAction() {
		getActionBars().getToolBarManager().add(removeToolbarContributionItem);
		getActionBars().getMenuManager().add(removeMenuContributionItem);
	}

	private void createRemoveContributions() {
		removeToolbarContributionItem = new ActionContributionItem(createRemoveAction());
		removeMenuContributionItem = new ActionContributionItem(createRemoveAction());
	}
	
	private IAction createRemoveAction() {
		IAction action = new Action() {
			public void run() {
				removeSelectedObject();
			}
		};
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/delete_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/delete_disabled.gif")));
		action.setToolTipText("Remove the selection");
		action.setText("Delete");
		action.setEnabled(false);
		return action;
	}
	
	private void addNewTask() {
		TaskContainer target = tasksSection.getTaskContainer();
		TaskCreateCommand command = new TaskCreateCommand(target.getFactory());
		command.setTaskContainer(target);
		getCommandStack().execute(command);
	}
	
	private void removeSelectedObject() {
		TaskDeleteCommand command = new TaskDeleteCommand();
		command.setTaskContainer(tasksSection.getTaskContainer());
		command.setTask(tasksSection.getSelectedTask());
		getCommandStack().execute(command);
	}
	
	private CommandStack getCommandStack() {
		return tasksSection.getCommandStack();
	}
	
	private void removeAddTaskAction() {
		getActionBars().getToolBarManager().remove(addTaskToolbarContributionItem);
		getActionBars().getMenuManager().remove(addTaskMenuContributionItem);
	}

	private void removeRemoveAction() {
		getActionBars().getToolBarManager().remove(removeToolbarContributionItem);
		getActionBars().getMenuManager().remove(removeMenuContributionItem);
	}

	private IActionBars getActionBars() {
		return tasksSection.getTabbedPropertySheetPage().getSite().getActionBars();
	}
	
	public void createPopupMenu(Composite composite) {
		root = new Menu(composite);
		composite.setMenu(root);
		root.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				disposePopupMenu();
				createPopupMenu();
			}			
		});
	}
	
	private void disposePopupMenu() {
		MenuItem[] menuItems = root.getItems();
		for (int i = 0; i < menuItems.length; i++) {
			menuItems[i].dispose();
		}
	}
	
	private void createPopupMenu() {
		createAddTaskMenuItem();
		new MenuItem(root, SWT.SEPARATOR);
		createRemoveMenuItem();
	}

	private String getDeleteImagePath() {
		String imagePath;
		if (tasksSection.getSelectedTask() != null) { 
			imagePath = "/icons/full/obj16/delete_enabled.gif";
		} else {
			imagePath = "/icons/full/obj16/delete_disabled.gif";
		}
		return imagePath;
	}

	private void createAddTaskMenuItem() {
		MenuItem addTaskItem = new MenuItem(root, SWT.PUSH);
		addTaskItem.setText("New Task");
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/new_task_enabled.gif"));
		addTaskItem.setImage(SharedImages.INSTANCE.getImage(descriptor));
		addTaskItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addNewTask();
			}			
		});
	}
	
	private void createRemoveMenuItem() {
		MenuItem removeItem = new MenuItem(root, SWT.PUSH);
		removeItem.setText("Delete");
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry(getDeleteImagePath()));
		removeItem.setImage(SharedImages.INSTANCE.getImage(descriptor));
		removeItem.setEnabled(tasksSection.getSelectedTask() != null);
		removeItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeSelectedObject();
			}
		});
	}

}
