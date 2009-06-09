package org.jbpm.gd.jpdl.properties;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IActionBars;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.Plugin;
import org.jbpm.gd.jpdl.command.ActionElementCreateCommand;
import org.jbpm.gd.jpdl.command.ActionElementDeleteCommand;
import org.jbpm.gd.jpdl.command.ExceptionHandlerCreateCommand;
import org.jbpm.gd.jpdl.command.ExceptionHandlerDeleteCommand;
import org.jbpm.gd.jpdl.model.ActionElementContainer;
import org.jbpm.gd.jpdl.model.ExceptionHandlerContainer;


public class ExceptionHandlerContainerSectionActionBarContributor implements IMenuCreator {
	
	private static final String ACTION_ID = "org.jbpm.gd.jpdl.action";
	private static final String SCRIPT_ID = "org.jbpm.gd.jpdl.script";
	
	private String elementToCreate = ACTION_ID;
	
	private ExceptionHandlerContainerSection exceptionHandlerContainerSection;
	
	private ActionContributionItem addExceptionHandlerToolbarContributionItem;
	private ActionContributionItem addActionElementToolbarContributionItem;
	private ActionContributionItem removeToolbarContributionItem;
	private ActionContributionItem addExceptionHandlerMenuContributionItem;
	private ActionContributionItem addActionMenuContributionItem;
	private ActionContributionItem addScriptMenuContributionItem;
	private ActionContributionItem removeMenuContributionItem;
	private Separator toolbarSeparator;
	private Separator firstMenuSeparator;
	private Separator secondMenuSeparator;
	private Menu addActionElementMenu;
	private Menu popupMenu;
	
	public ExceptionHandlerContainerSectionActionBarContributor(ExceptionHandlerContainerSection section) {
		this.exceptionHandlerContainerSection = section;
		addExceptionHandlerToolbarContributionItem = new ActionContributionItem(createAddExceptionHandlerAction());
		addActionElementToolbarContributionItem = new ActionContributionItem(createAddActionElementAction());
		removeToolbarContributionItem = new ActionContributionItem(createRemoveAction());
		addExceptionHandlerMenuContributionItem = new ActionContributionItem(createAddExceptionHandlerAction());
		addActionMenuContributionItem = new ActionContributionItem(createAddActionAction());
		addScriptMenuContributionItem = new ActionContributionItem(createAddScriptAction());
		removeMenuContributionItem = new ActionContributionItem(createRemoveAction());
		toolbarSeparator = new Separator();
		firstMenuSeparator = new Separator();
		secondMenuSeparator = new Separator();
	}
	
	private IAction createAddExceptionHandlerAction() {
		IAction action = new Action() {
			public void run() {
				addNewExceptionHandler();
			}
		};
		setExceptionHandlerInfo(action);
		return action;
	}
	
	private IAction createAddActionAction() {
		IAction action = new Action() {
			public void run() {
				addNewActionElement(ACTION_ID);
			}
		};
		setActionInfo(action);
		return action;
	}
	
	private IAction createAddScriptAction() {
		IAction action = new Action() {
			public void run() {
				addNewActionElement(SCRIPT_ID);
			}
		};
		setScriptInfo(action);
		return action;
	}
	
	private IAction createAddActionElementAction() {
		IAction action = new Action() {
			public void run() {
				addNewActionElement(elementToCreate);
			}
		};
		action.setMenuCreator(this);
		action.setEnabled(false);
		setActionInfo(action);
		return action;
	}
	
	private void setExceptionHandlerInfo(IAction action) {
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_exception_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_exception_disabled.gif")));
		action.setToolTipText("Add an exception handler");	
		action.setText("New Exception Handler");
	}

	private void setActionInfo(IAction action) {
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_action_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_action_disabled.gif")));
		action.setToolTipText("Add an action");	
		action.setText("New Action");
	}
	
	private void setScriptInfo(IAction action) {
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_script_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_script_disabled.gif")));
		action.setToolTipText("Add a script action");	
		action.setText("New Script");
	}
	
	private IAction createRemoveAction() {
		IAction action = new Action() {
			public void run() {
				removeSelectedObject();
			}
		};
		setRemoveInfo(action);
		action.setEnabled(false);
		return action;
	}

	private void setRemoveInfo(IAction action) {
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/delete_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/delete_disabled.gif")));
		action.setToolTipText("Remove the selected object");
		action.setText("Delete");
	}
	
	private void createActionElementMenu(Control parent) {
		boolean enabled = exceptionHandlerContainerSection.getSelectedExceptionHandler() != null;
		addActionElementMenu = new Menu(parent);
		addActionElementMenu.setEnabled(enabled);
		createAddActionElementMenuItems(addActionElementMenu, enabled);
	}

	private void createAddActionElementMenuItems(Menu menu, boolean enabled) {
		createAddActionMenuItem(menu, enabled);
		createAddScriptMenuItem(menu, enabled);
	}

	private void createAddActionMenuItem(Menu menu, boolean enabled) {
		MenuItem addActionMenuItem = new MenuItem(menu, SWT.PUSH);
		addActionMenuItem.setText("New Action");
		ImageDescriptor enabledDescriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/new_action_enabled.gif"));
		addActionMenuItem.setImage(SharedImages.INSTANCE.getImage(enabledDescriptor));
		addActionMenuItem.setEnabled(enabled);
		if (enabled) {
			addActionMenuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					addNewActionElement(ACTION_ID);
					if (((MenuItem)e.widget).getParent() != popupMenu) {
						elementToCreate = ACTION_ID;
						setActionInfo(addActionElementToolbarContributionItem.getAction());
					}
				}			
			});
		}
	}
	
	private void createAddScriptMenuItem(Menu menu, boolean enabled) {
		MenuItem addScriptMenuItem = new MenuItem(menu, SWT.PUSH);
		addScriptMenuItem.setText("New Script");
		ImageDescriptor enabledDescriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/new_script_enabled.gif"));
		addScriptMenuItem.setImage(SharedImages.INSTANCE.getImage(enabledDescriptor));
		addScriptMenuItem.setEnabled(enabled);
		if (enabled) {
			addScriptMenuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					addNewActionElement(SCRIPT_ID);
					if (((MenuItem)e.widget).getParent() != popupMenu) {
						elementToCreate = SCRIPT_ID;
						setScriptInfo(addActionElementToolbarContributionItem.getAction());
					}
				}			
			});
		}		
	}
	
	private void addNewActionElement(String elementToCreate) {
		ActionElementContainer target = exceptionHandlerContainerSection.getSelectedExceptionHandler();
		ActionElementCreateCommand command = new ActionElementCreateCommand(target.getFactory());
		command.setActionElementContainer(target);
		command.setActionId(elementToCreate);
		getCommandStack().execute(command);
	}
	
	private void addNewExceptionHandler() {
		ExceptionHandlerContainer target = exceptionHandlerContainerSection.getExceptionHandlerContainer();
		ExceptionHandlerCreateCommand command = new ExceptionHandlerCreateCommand(target.getFactory());
		command.setExceptionHandlerContainer(target);
		getCommandStack().execute(command);
	}
	
	private void removeSelectedObject() {
		if (exceptionHandlerContainerSection.getSelectedActionElement() != null) {
			ActionElementDeleteCommand command = new ActionElementDeleteCommand();
			command.setActionElement(exceptionHandlerContainerSection.getSelectedActionElement());
			command.setActionElementContainer(exceptionHandlerContainerSection.getSelectedExceptionHandler());
			getCommandStack().execute(command);
		} else {
			ExceptionHandlerDeleteCommand command = new ExceptionHandlerDeleteCommand();
			command.setExceptionHandlerContainer(exceptionHandlerContainerSection.getExceptionHandlerContainer());
			command.setExceptionHandler(exceptionHandlerContainerSection.getSelectedExceptionHandler());
			getCommandStack().execute(command);
		}
	}
	
	private CommandStack getCommandStack() {
		return exceptionHandlerContainerSection.getCommandStack();
	}
	
	private IActionBars getActionBars() {
		return exceptionHandlerContainerSection.getTabbedPropertySheetPage().getSite().getActionBars();
	}
	
	public void setRemoveEnabled(boolean enabled) {
		removeToolbarContributionItem.getAction().setEnabled(enabled);
		removeMenuContributionItem.getAction().setEnabled(enabled);
	}
	
	public void setAddActionElementEnabled(boolean enabled) {
		addActionElementToolbarContributionItem.getAction().setEnabled(enabled);
		addActionMenuContributionItem.getAction().setEnabled(enabled);
		addScriptMenuContributionItem.getAction().setEnabled(enabled);
	}

	public void activateContributions() {
		getActionBars().getToolBarManager().add(addExceptionHandlerToolbarContributionItem);
		getActionBars().getToolBarManager().add(addActionElementToolbarContributionItem);
		getActionBars().getToolBarManager().add(toolbarSeparator);
		getActionBars().getToolBarManager().add(removeToolbarContributionItem);
		getActionBars().getMenuManager().add(addExceptionHandlerMenuContributionItem);
		getActionBars().getMenuManager().add(firstMenuSeparator);
		getActionBars().getMenuManager().add(addActionMenuContributionItem);
		getActionBars().getMenuManager().add(addScriptMenuContributionItem);
		getActionBars().getMenuManager().add(secondMenuSeparator);
		getActionBars().getMenuManager().add(removeMenuContributionItem);
		getActionBars().updateActionBars();
	}

	public void deactivateContributions() {
		getActionBars().getToolBarManager().remove(addExceptionHandlerToolbarContributionItem);
		getActionBars().getToolBarManager().remove(addActionElementToolbarContributionItem);
		getActionBars().getToolBarManager().remove(toolbarSeparator);
		getActionBars().getToolBarManager().remove(removeToolbarContributionItem);
		getActionBars().getMenuManager().remove(addExceptionHandlerMenuContributionItem);
		getActionBars().getMenuManager().remove(firstMenuSeparator);
		getActionBars().getMenuManager().remove(addActionMenuContributionItem);
		getActionBars().getMenuManager().remove(addScriptMenuContributionItem);
		getActionBars().getMenuManager().remove(secondMenuSeparator);
		getActionBars().getMenuManager().remove(removeMenuContributionItem);
		getActionBars().updateActionBars();
	}

	public void createPopupMenu(Composite composite) {
		popupMenu = new Menu(composite);
		composite.setMenu(popupMenu);
		popupMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				disposePopupMenu();
				createPopupMenu();
			}			
		});
	}
	
	private void disposePopupMenu() {
		MenuItem[] menuItems = popupMenu.getItems();
		for (int i = 0; i < menuItems.length; i++) {
			menuItems[i].dispose();
		}
	}
	
	private void createPopupMenu() {
		createAddExceptionHandlerMenuItem();
		new MenuItem(popupMenu, SWT.SEPARATOR);
		boolean enabled = exceptionHandlerContainerSection.getSelectedExceptionHandler() != null;
		createAddActionElementMenuItems(popupMenu, enabled);
		new MenuItem(popupMenu, SWT.SEPARATOR);
		createRemoveMenuItem();
	}
	
	private void createAddExceptionHandlerMenuItem() {		
		MenuItem addEventItem = new MenuItem(popupMenu, SWT.PUSH);
		addEventItem.setText("New Exception Handler");
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/new_exception_enabled.gif"));
		addEventItem.setImage(SharedImages.INSTANCE.getImage(descriptor));
		addEventItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addNewExceptionHandler();
			}			
		});
	}
	
	private String getDeleteImagePath() {
		String imagePath;
		if (hasSelection()) { 
			imagePath = "/icons/full/obj16/delete_enabled.gif";
		} else {
			imagePath = "/icons/full/obj16/delete_disabled.gif";
		}
		return imagePath;
	}

	private void createRemoveMenuItem() {
		MenuItem removeItem = new MenuItem(popupMenu, SWT.PUSH);
		removeItem.setText("Delete");
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry(getDeleteImagePath()));
		removeItem.setImage(SharedImages.INSTANCE.getImage(descriptor));
		removeItem.setEnabled(hasSelection());
		removeItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeSelectedObject();
			}
		});
	}
	
	private boolean hasSelection() {
		return exceptionHandlerContainerSection.getSelectedActionElement() != null || exceptionHandlerContainerSection.getSelectedExceptionHandler() != null;
	}

	public void dispose() {
		if (addActionElementMenu != null) {
			MenuItem[] items = addActionElementMenu.getItems();
			for (int i = 0; i < items.length; i++) {
				items[i].dispose();
			}
		}
	}

	public Menu getMenu(Control parent) {
		if (addActionElementMenu == null) {
			createActionElementMenu(parent);
		}
		return addActionElementMenu;
	}

	public Menu getMenu(Menu parent) {
		return null;
	}
	
	
}
