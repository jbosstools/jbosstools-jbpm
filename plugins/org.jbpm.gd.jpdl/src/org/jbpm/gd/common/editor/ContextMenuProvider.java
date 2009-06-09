package org.jbpm.gd.common.editor;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

public class ContextMenuProvider extends org.eclipse.gef.ContextMenuProvider {

	private ActionRegistry actionRegistry;

	public ContextMenuProvider(EditPartViewer viewer,
			ActionRegistry registry) {
		super(viewer);
		this.actionRegistry = registry;
	}

	public void buildContextMenu(IMenuManager manager) {
		GEFActionConstants.addStandardActionGroups(manager);
		IAction action;
 		action = actionRegistry.getAction(ActionFactory.UNDO.getId());
		manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
		action = actionRegistry.getAction(ActionFactory.REDO.getId());
		manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

//		action = getActionRegistry().getAction(IWorkbenchActionConstants.PASTE);
//		if (action.isEnabled())
//			manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		action = actionRegistry
				.getAction(ActionFactory.DELETE.getId());
		if (action.isEnabled())
			manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

//		action = getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT);
//		if (action.isEnabled())
//			manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		action = actionRegistry.getAction(ActionFactory.SAVE.getId());
		manager.appendToGroup(GEFActionConstants.GROUP_SAVE, action);

	}

}
