package org.jbpm.gd.jpdl.properties;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
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
import org.jbpm.gd.jpdl.command.TimerCreateCommand;
import org.jbpm.gd.jpdl.command.TimerDeleteCommand;
import org.jbpm.gd.jpdl.model.TimerContainer;


public class TimerContainerSectionActionBarContributor {
	
	private TimerContainerSection timersSection;
	
	private ActionContributionItem addTimerToolbarContributionItem;
	private ActionContributionItem removeToolbarContributionItem;
	private ActionContributionItem addTimerMenuContributionItem;
	private ActionContributionItem removeMenuContributionItem;
	private Separator toolbarSeparator;
	private Separator menuSeparator;
	
	private Menu root;
	
	public TimerContainerSectionActionBarContributor(TimerContainerSection timersSection) {
		setTimersSection(timersSection);
		createAddTimerContributions();
		createSeparatorContributions();
		createRemoveContributions();
	}
	
	private void createSeparatorContributions() {
		toolbarSeparator = new Separator();
		menuSeparator = new Separator();
	}
	
	private void setTimersSection(TimerContainerSection timersSection) {
		this.timersSection = timersSection;
	}
	
	public void activateContributions() {
		addAddTimerAction();
		addSeparators();
		addRemoveAction();
		getActionBars().updateActionBars();
	}
	
	private void addSeparators() {
		getActionBars().getToolBarManager().add(toolbarSeparator);
		getActionBars().getMenuManager().add(menuSeparator);
	}
	
	public void deactivateContributions() {
		removeAddTimerAction();
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

	private void addAddTimerAction() {
		getActionBars().getToolBarManager().add(addTimerToolbarContributionItem);
		getActionBars().getMenuManager().add(addTimerMenuContributionItem);
	}
	
	private void createAddTimerContributions() {
		addTimerToolbarContributionItem = new ActionContributionItem(createAddTimerAction());
		addTimerMenuContributionItem = new ActionContributionItem(createAddTimerAction());
	}
	
	private IAction createAddTimerAction() {
		IAction action = new Action() {
			public void run() {
				addNewTimer();
			}
		};
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_timer_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_timer_disabled.gif")));
		action.setToolTipText("Add a timer");	
		action.setText("New Timer");
		return action;
	}
	
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
	
	private void addNewTimer() {
		TimerContainer target = timersSection.getTimerContainer();
		TimerCreateCommand command = new TimerCreateCommand(target.getFactory());
		command.setTimerContainer(target);
		getCommandStack().execute(command);
	}
	
	private void removeSelectedObject() {
		TimerDeleteCommand command = new TimerDeleteCommand();
		command.setTimerContainer(timersSection.getTimerContainer());
		command.setTimer(timersSection.getSelectedTimer());
		getCommandStack().execute(command);
	}
	
	private CommandStack getCommandStack() {
		return timersSection.getCommandStack();
	}
	
	private void removeAddTimerAction() {
		getActionBars().getToolBarManager().remove(addTimerToolbarContributionItem);
		getActionBars().getMenuManager().remove(addTimerMenuContributionItem);
	}

	private void removeRemoveAction() {
		getActionBars().getToolBarManager().remove(removeToolbarContributionItem);
		getActionBars().getMenuManager().remove(removeMenuContributionItem);
	}

	private IActionBars getActionBars() {
		return timersSection.getTabbedPropertySheetPage().getSite().getActionBars();
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
		createAddTimerMenuItem();
		new MenuItem(root, SWT.SEPARATOR);
		createRemoveMenuItem();
	}

	private String getDeleteImagePath() {
		String imagePath;
		if (timersSection.getSelectedTimer() != null) { 
			imagePath = "/icons/full/obj16/delete_enabled.gif";
		} else {
			imagePath = "/icons/full/obj16/delete_disabled.gif";
		}
		return imagePath;
	}

	private void createAddTimerMenuItem() {
		MenuItem addTimerItem = new MenuItem(root, SWT.PUSH);
		addTimerItem.setText("New Timer");
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/new_timer_enabled.gif"));
		addTimerItem.setImage(SharedImages.INSTANCE.getImage(descriptor));
		addTimerItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addNewTimer();
			}			
		});
	}
	
	private void createRemoveMenuItem() {
		MenuItem removeItem = new MenuItem(root, SWT.PUSH);
		removeItem.setText("Delete");
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry(getDeleteImagePath()));
		removeItem.setImage(SharedImages.INSTANCE.getImage(descriptor));
		removeItem.setEnabled(timersSection.getSelectedTimer() != null);
		removeItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeSelectedObject();
			}
		});
	}

}
