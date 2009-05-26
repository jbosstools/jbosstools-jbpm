package org.jboss.tools.flow.jpdl4.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.flow.common.command.AddChildCommand;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class AddTimerHandler extends AbstractHandler implements IHandler {

	private Wrapper getParent(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindowChecked(event).getActivePage();
		if (page == null) return null;
		ISelection selection = page.getSelection();
		if (!(selection instanceof IStructuredSelection)) return null;
		IStructuredSelection structuredSelection = (IStructuredSelection)selection;
		Object first = structuredSelection.getFirstElement();
		if (!(first instanceof EditPart)) return null;
		EditPart editPart = (EditPart)first;
		while (editPart != null) {
			Object model = editPart.getModel();
			if (model != null && model instanceof Wrapper) {
				return (Wrapper)model;
			}
			editPart = editPart.getParent();
		}
		return null;
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Wrapper parent = getParent(event);
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		if (editorPart == null) return null;
		Object object = editorPart.getAdapter(CommandStack.class);
		if (object == null || !(object instanceof CommandStack)) return null;
		CommandStack commandStack = (CommandStack)object;
		AddChildCommand addChildCommand = new AddChildCommand();
		Wrapper child = ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.timer");
		addChildCommand.setChild(child);
		addChildCommand.setType("timer");
		addChildCommand.setParent(parent);
		commandStack.execute(addChildCommand);
		return null;	
	}

}
