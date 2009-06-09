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
import org.jbpm.gd.jpdl.model.ActionElementContainer;


public class ActionElementContainerSectionActionBarContributor implements IMenuCreator {
	
	private static final String ACTION_ID = "org.jbpm.gd.jpdl.action";
	private static final String CANCEL_TIMER_ID = "org.jbpm.gd.jpdl.cancelTimer";
	private static final String CREATE_TIMER_ID = "org.jbpm.gd.jpdl.createTimer";
	private static final String MAIL_ID = "org.jbpm.gd.jpdl.mail";
	private static final String SCRIPT_ID = "org.jbpm.gd.jpdl.script";
	private static final String ESB_NOTIFIER_ID = "org.jbpm.gd.jpdl.esbNotifier";
	
	private String elementToCreate = ACTION_ID;
	
	private ActionElementContainerSection actionElementsSection;
	
	private ActionContributionItem addActionElementToolbarContributionItem;
	private ActionContributionItem removeToolbarContributionItem;
	private ActionContributionItem addActionMenuContributionItem;
	private ActionContributionItem addCreateTimerMenuContributionItem;
	private ActionContributionItem addCancelTimerMenuContributionItem;
	private ActionContributionItem addScriptMenuContributionItem;
	private ActionContributionItem addMailMenuContributionItem;
	private ActionContributionItem addEsbNotifierMenuContributionItem;
	private ActionContributionItem removeMenuContributionItem;
	private Separator toolbarSeparator;
	private Separator menuSeparator;
	private Menu addActionElementMenu;
	private Menu popupMenu;
	
	public ActionElementContainerSectionActionBarContributor(ActionElementContainerSection actionElementsSection) {
		this.actionElementsSection = actionElementsSection;
		addActionElementToolbarContributionItem = new ActionContributionItem(createAddActionElementAction());
		removeToolbarContributionItem = new ActionContributionItem(createRemoveAction());
		addActionMenuContributionItem = new ActionContributionItem(createAddActionAction());
		addCreateTimerMenuContributionItem = new ActionContributionItem(createAddCreateTimerAction());
		addCancelTimerMenuContributionItem = new ActionContributionItem(createAddCancelTimerAction());
		addScriptMenuContributionItem = new ActionContributionItem(createAddScriptAction());
		addMailMenuContributionItem = new ActionContributionItem(createAddMailAction());
		addEsbNotifierMenuContributionItem = new ActionContributionItem(createAddEsbNotifierAction());
		removeMenuContributionItem = new ActionContributionItem(createRemoveAction());
		toolbarSeparator = new Separator();
		menuSeparator = new Separator();
	}
	
	private IAction createAddCancelTimerAction() {
		IAction action = new Action() {
			public void run() {
				addNewActionElement(CANCEL_TIMER_ID);
			}
		};
		setCancelTimerInfo(action);
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
	
	private IAction createAddCreateTimerAction() {
		IAction action = new Action() {
			public void run() {
				addNewActionElement(CREATE_TIMER_ID);
			}
		};
		setCreateTimerInfo(action);
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
	
	private IAction createAddMailAction() {
		IAction action = new Action() {
			public void run() {
				addNewActionElement(MAIL_ID);
			}
		};
		setMailInfo(action);
		return action;
	}
	
	private IAction createAddEsbNotifierAction() {
		IAction action = new Action() {
			public void run() {
				addNewActionElement(ESB_NOTIFIER_ID);
			}
		};
		setEsbNotifierInfo(action);
		return action;
	}
	
	private IAction createAddActionElementAction() {
		IAction action = new Action() {
			public void run() {
				addNewActionElement(elementToCreate);
			}
		};
		action.setMenuCreator(this);
		setActionInfo(action);
		return action;
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
	
	private void setCancelTimerInfo(IAction action) {
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_cancel_timer_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_cancel_timer_disabled.gif")));
		action.setToolTipText("Add a cancel timer action");	
		action.setText("New Cancel Timer");
	}
	
	private void setCreateTimerInfo(IAction action) {
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_create_timer_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_create_timer_disabled.gif")));
		action.setToolTipText("Add a create timer action");	
		action.setText("New Create Timer");
	}
	
	private void setMailInfo(IAction action) {
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_mail_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_mail_disabled.gif")));
		action.setToolTipText("Add a mail action");	
		action.setText("New Mail");
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
	
	private void setEsbNotifierInfo(IAction action) {
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/esb_enabled.gif")));
		action.setToolTipText("Add a esb Notifier");	
		action.setText("New ESB Notifier");
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
		addActionElementMenu = new Menu(parent);
		createAddActionElementMenuItems(addActionElementMenu);
	}

	private void createAddActionElementMenuItems(Menu menu) {
		createAddActionMenuItem(menu);
		createAddScriptMenuItem(menu);
		createAddCreateTimerMenuItem(menu);
		createAddCancelTimerMenuItem(menu);
		createAddMailMenuItem(menu);
		createAddEsbNotifierMenuItem(menu);
	}

	private void createAddActionMenuItem(Menu menu) {
		MenuItem addActionMenuItem = new MenuItem(menu, SWT.PUSH);
		addActionMenuItem.setText("New Action");
		ImageDescriptor enabledDescriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/new_action_enabled.gif"));
		addActionMenuItem.setImage(SharedImages.INSTANCE.getImage(enabledDescriptor));
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
	
	private void createAddCancelTimerMenuItem(Menu menu) {
		MenuItem addCancelTimerMenuItem = new MenuItem(menu, SWT.PUSH);
		addCancelTimerMenuItem.setText("New Cancel Timer");
		ImageDescriptor enabledDescriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/new_cancel_timer_enabled.gif"));
		addCancelTimerMenuItem.setImage(SharedImages.INSTANCE.getImage(enabledDescriptor));
		addCancelTimerMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addNewActionElement(CANCEL_TIMER_ID);
				if (((MenuItem)e.widget).getParent() != popupMenu) {
					elementToCreate = CANCEL_TIMER_ID;
					setCancelTimerInfo(addActionElementToolbarContributionItem.getAction());
				}
			}			
		});
	}		
	
	private void createAddCreateTimerMenuItem(Menu menu) {
		MenuItem addCreateTimerMenuItem = new MenuItem(menu, SWT.PUSH);
		addCreateTimerMenuItem.setText("New Create Timer");
		ImageDescriptor enabledDescriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/new_create_timer_enabled.gif"));
		addCreateTimerMenuItem.setImage(SharedImages.INSTANCE.getImage(enabledDescriptor));
		addCreateTimerMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addNewActionElement(CREATE_TIMER_ID);
				if (((MenuItem)e.widget).getParent() != popupMenu) {
					elementToCreate = CREATE_TIMER_ID;
					setCreateTimerInfo(addActionElementToolbarContributionItem.getAction());
				}
			}			
		});
	}		
	
	private void createAddMailMenuItem(Menu menu) {
		MenuItem addMailMenuItem = new MenuItem(menu, SWT.PUSH);
		addMailMenuItem.setText("New Mail");
		ImageDescriptor enabledDescriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/new_mail_enabled.gif"));
		addMailMenuItem.setImage(SharedImages.INSTANCE.getImage(enabledDescriptor));
		addMailMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addNewActionElement(MAIL_ID);
				if (((MenuItem)e.widget).getParent() != popupMenu) {
					elementToCreate = MAIL_ID;
					setMailInfo(addActionElementToolbarContributionItem.getAction());
				}
			}			
		});
	}		
	
	private void createAddScriptMenuItem(Menu menu) {
		MenuItem addScriptMenuItem = new MenuItem(menu, SWT.PUSH);
		addScriptMenuItem.setText("New Script");
		ImageDescriptor enabledDescriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/new_script_enabled.gif"));
		addScriptMenuItem.setImage(SharedImages.INSTANCE.getImage(enabledDescriptor));
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
	
	private void createAddEsbNotifierMenuItem(Menu menu) {
		MenuItem addEsbNotifierMenuItem = new MenuItem(menu, SWT.PUSH);
		addEsbNotifierMenuItem.setText("New ESB Notifier");
		ImageDescriptor enabledDescriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/esb_enabled.gif"));
		addEsbNotifierMenuItem.setImage(SharedImages.INSTANCE.getImage(enabledDescriptor));
		addEsbNotifierMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addNewActionElement(ESB_NOTIFIER_ID);
				if (((MenuItem)e.widget).getParent() != popupMenu) {
					elementToCreate = ESB_NOTIFIER_ID;
					setEsbNotifierInfo(addActionElementToolbarContributionItem.getAction());
				}
			}			
		});
	}		
	
	private void addNewActionElement(String elementToCreate) {
		ActionElementContainer target = actionElementsSection.getActionElementContainer();
		ActionElementCreateCommand command = new ActionElementCreateCommand(target.getFactory());
		command.setActionElementContainer(actionElementsSection.getActionElementContainer());
		command.setActionId(elementToCreate);
		getCommandStack().execute(command);
	}
	
	private void removeSelectedObject() {
		if (actionElementsSection.getSelectedActionElement() != null) {
			ActionElementDeleteCommand command = new ActionElementDeleteCommand();
			command.setActionElement(actionElementsSection.getSelectedActionElement());
			command.setActionElementContainer(actionElementsSection.getActionElementContainer());
			getCommandStack().execute(command);
		}
	}
	
	private CommandStack getCommandStack() {
		return actionElementsSection.getCommandStack();
	}
	
	private IActionBars getActionBars() {
		return actionElementsSection.getTabbedPropertySheetPage().getSite().getActionBars();
	}
	
	public void setRemoveEnabled(boolean enabled) {
		removeToolbarContributionItem.getAction().setEnabled(enabled);
		removeMenuContributionItem.getAction().setEnabled(enabled);
	}
	
	public void activateContributions() {
		getActionBars().getToolBarManager().add(addActionElementToolbarContributionItem);
		getActionBars().getToolBarManager().add(toolbarSeparator);
		getActionBars().getToolBarManager().add(removeToolbarContributionItem);
		getActionBars().getMenuManager().add(addActionMenuContributionItem);
		getActionBars().getMenuManager().add(addScriptMenuContributionItem);
		getActionBars().getMenuManager().add(addCreateTimerMenuContributionItem);
		getActionBars().getMenuManager().add(addCancelTimerMenuContributionItem);
		getActionBars().getMenuManager().add(addMailMenuContributionItem);
		getActionBars().getMenuManager().add(addEsbNotifierMenuContributionItem);
		getActionBars().getMenuManager().add(menuSeparator);
		getActionBars().getMenuManager().add(removeMenuContributionItem);
		getActionBars().updateActionBars();
	}

	public void deactivateContributions() {
		getActionBars().getToolBarManager().remove(addActionElementToolbarContributionItem);
		getActionBars().getToolBarManager().remove(toolbarSeparator);
		getActionBars().getToolBarManager().remove(removeToolbarContributionItem);
		getActionBars().getMenuManager().remove(addActionMenuContributionItem);
		getActionBars().getMenuManager().remove(addCreateTimerMenuContributionItem);
		getActionBars().getMenuManager().remove(addCancelTimerMenuContributionItem);
		getActionBars().getMenuManager().remove(addScriptMenuContributionItem);
		getActionBars().getMenuManager().remove(addMailMenuContributionItem);
		getActionBars().getMenuManager().remove(addEsbNotifierMenuContributionItem);
		getActionBars().getMenuManager().remove(menuSeparator);
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
		createAddActionElementMenuItems(popupMenu);
		new MenuItem(popupMenu, SWT.SEPARATOR);
		createRemoveMenuItem();
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
		return actionElementsSection.getSelectedActionElement() != null;
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
