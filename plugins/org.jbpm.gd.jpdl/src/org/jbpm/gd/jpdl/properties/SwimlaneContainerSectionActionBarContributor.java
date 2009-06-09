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
import org.jbpm.gd.jpdl.command.SwimlaneCreateCommand;
import org.jbpm.gd.jpdl.command.SwimlaneDeleteCommand;
import org.jbpm.gd.jpdl.model.ProcessDefinition;


public class SwimlaneContainerSectionActionBarContributor {
	
	private SwimlaneContainerSection swimlaneSection;
	
	private ActionContributionItem addSwimlaneToolbarContributionItem;
	private ActionContributionItem removeSwimlaneToolbarContributionItem;
	private ActionContributionItem addSwimlaneMenuContributionItem;
	private ActionContributionItem removeSwimlaneMenuContributionItem;
	private Separator toolbarSeparator;
	private Separator menuSeparator;
	
	private Menu root;
	
	public SwimlaneContainerSectionActionBarContributor(SwimlaneContainerSection swimlaneSection) {
		setSwimlaneSection(swimlaneSection);
		createAddSwimlaneContributions();
		createSeparatorContributions();
		createRemoveSwimlaneContributions();
	}
	
	private void createSeparatorContributions() {
		toolbarSeparator = new Separator();
		menuSeparator = new Separator();
	}
	
	private void setSwimlaneSection(SwimlaneContainerSection swimlaneSection) {
		this.swimlaneSection = swimlaneSection;
	}
	
	public void activateContributions() {
		addAddSwimlaneAction();
		addSeparators();
		addRemoveSwimlaneAction();
		getActionBars().updateActionBars();
	}
	
	private void addSeparators() {
		getActionBars().getToolBarManager().add(toolbarSeparator);
		getActionBars().getMenuManager().add(menuSeparator);
	}
	
	public void deactivateContributions() {
		removeAddSwimlaneAction();
		removeSeparators();
		removeRemoveSwimlaneAction();
		getActionBars().updateActionBars();
	}
	
	private void removeSeparators() {
		getActionBars().getToolBarManager().remove(toolbarSeparator);
		getActionBars().getMenuManager().remove(menuSeparator);
	}
	
	public void setRemoveSwimlaneEnabled(boolean enabled) {
		removeSwimlaneMenuContributionItem.getAction().setEnabled(enabled);
		removeSwimlaneToolbarContributionItem.getAction().setEnabled(enabled);
	}

	private void addAddSwimlaneAction() {
		getActionBars().getToolBarManager().add(addSwimlaneToolbarContributionItem);
		getActionBars().getMenuManager().add(addSwimlaneMenuContributionItem);
	}
	
	private void createAddSwimlaneContributions() {
		addSwimlaneToolbarContributionItem = new ActionContributionItem(createAddSwimlaneAction());
		addSwimlaneMenuContributionItem = new ActionContributionItem(createAddSwimlaneAction());
	}
	
	private IAction createAddSwimlaneAction() {
		IAction action = new Action() {
			public void run() {
				addNewSwimlane();
			}
		};
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_swimlane_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/new_swimlane_disabled.gif")));
		action.setToolTipText("Add a swimlane");	
		action.setText("New Swimlane");
		return action;
	}
	
	IContributionItem item;
	
	private void addRemoveSwimlaneAction() {
		getActionBars().getToolBarManager().add(removeSwimlaneToolbarContributionItem);
		getActionBars().getMenuManager().add(removeSwimlaneMenuContributionItem);
	}

	private void createRemoveSwimlaneContributions() {
		createRemoveSwimlaneAction();
		removeSwimlaneToolbarContributionItem = new ActionContributionItem(createRemoveSwimlaneAction());
		removeSwimlaneMenuContributionItem = new ActionContributionItem(createRemoveSwimlaneAction());
	}
	
	private IAction createRemoveSwimlaneAction() {
		IAction action = new Action() {
			public void run() {
				removeSelectedSwimlane();
			}
		};
		action.setImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/delete_enabled.gif")));
		action.setDisabledImageDescriptor(
				ImageDescriptor.createFromURL(Plugin.getDefault()
						.getBundle().getEntry("/icons/full/obj16/delete_disabled.gif")));
		action.setToolTipText("Remove the selected swimlane");
		action.setText("Delete");
		action.setEnabled(false);
		return action;
	}
	
	private void addNewSwimlane() {
		ProcessDefinition processDefinition = swimlaneSection.getProcessDefinition();
		SwimlaneCreateCommand command = new SwimlaneCreateCommand(processDefinition.getFactory());
		command.setProcessDefinition(processDefinition);
		getCommandStack().execute(command);
	}
	
	private void removeSelectedSwimlane() {
		SwimlaneDeleteCommand command = new SwimlaneDeleteCommand();
		command.setProcessDefinition(swimlaneSection.getProcessDefinition());
		command.setSwimlane(swimlaneSection.getSelectedSwimlane());
		getCommandStack().execute(command);
	}
	
	private CommandStack getCommandStack() {
		return swimlaneSection.getCommandStack();
	}
	
	private void removeAddSwimlaneAction() {
		getActionBars().getToolBarManager().remove(addSwimlaneToolbarContributionItem);
		getActionBars().getMenuManager().remove(addSwimlaneMenuContributionItem);
	}

	private void removeRemoveSwimlaneAction() {
		getActionBars().getToolBarManager().remove(removeSwimlaneToolbarContributionItem);
		getActionBars().getMenuManager().remove(removeSwimlaneMenuContributionItem);
	}

	private IActionBars getActionBars() {
		return swimlaneSection.getTabbedPropertySheetPage().getSite().getActionBars();
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
		createAddSwimlaneMenuItem();
		new MenuItem(root, SWT.SEPARATOR);
		createRemoveSwimlaneMenuItem();
	}

	private String getDeleteImagePath() {
		String imagePath;
		if (swimlaneSection.getSelectedSwimlane() != null) { 
			imagePath = "/icons/full/obj16/delete_enabled.gif";
		} else {
			imagePath = "/icons/full/obj16/delete_disabled.gif";
		}
		return imagePath;
	}

	private void createAddSwimlaneMenuItem() {
		MenuItem addSwimlaneItem = new MenuItem(root, SWT.PUSH);
		addSwimlaneItem.setText("New Swimlane");
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/new_swimlane_enabled.gif"));
		addSwimlaneItem.setImage(SharedImages.INSTANCE.getImage(descriptor));
		addSwimlaneItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addNewSwimlane();
			}			
		});
	}
	
	private void createRemoveSwimlaneMenuItem() {
		MenuItem removeSwimlaneItem = new MenuItem(root, SWT.PUSH);
		removeSwimlaneItem.setText("Delete");
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry(getDeleteImagePath()));
		removeSwimlaneItem.setImage(SharedImages.INSTANCE.getImage(descriptor));
		removeSwimlaneItem.setEnabled(swimlaneSection.getSelectedSwimlane() != null);
		removeSwimlaneItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeSelectedSwimlane();
			}
		});
	}

}
