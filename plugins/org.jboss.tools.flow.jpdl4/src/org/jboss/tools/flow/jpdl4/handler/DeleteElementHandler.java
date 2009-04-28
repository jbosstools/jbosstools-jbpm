package org.jboss.tools.flow.jpdl4.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.flow.common.command.DeleteChildCommand;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.jboss.tools.flow.jpdl4.model.EventListener;
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;
import org.jboss.tools.flow.jpdl4.model.Swimlane;
import org.jboss.tools.flow.jpdl4.model.Timer;

public class DeleteElementHandler extends AbstractHandler implements IHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindowChecked(event).getActivePage();
		if (page == null) return null;
		ISelection selection = page.getSelection();
		if (!(selection instanceof IStructuredSelection)) return null;
		IStructuredSelection structuredSelection = (IStructuredSelection)selection;
		Object first = structuredSelection.getFirstElement();
		if (!(first instanceof EditPart)) return null;
		EditPart editPart = (EditPart)first;
		Object model = editPart.getModel();
		if (model == null || !(model instanceof Wrapper)) return null;
		Wrapper child = (Wrapper)model;
		EditPart parentEditPart = getParentEditPart(child.getElement(), editPart);
		if (parentEditPart == null) return null;
		model = parentEditPart.getModel();
		if (model == null || !(model instanceof Wrapper)) return null;
		Wrapper parent = (Wrapper)model;
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		if (editorPart == null) return null;
		Object object = editorPart.getAdapter(CommandStack.class);
		if (object == null || !(object instanceof CommandStack)) return null;
		CommandStack commandStack = (CommandStack)object;
		DeleteChildCommand deleteChildCommand = new DeleteChildCommand();
		deleteChildCommand.setChild(child);
		if (child.getElement() instanceof Swimlane) {
			deleteChildCommand.setType("swimlane");
		} else if (child.getElement() instanceof Timer) {
			deleteChildCommand.setType("timer");
		} else if (child.getElement() instanceof EventListener) {
			deleteChildCommand.setType("listener");
		} else if (child.getElement() instanceof EventListenerContainer) {
			deleteChildCommand.setType("eventListener");
		}
		deleteChildCommand.setParent(parent);
		if (deleteChildCommand.canExecute()) {
			commandStack.execute(deleteChildCommand);
		} else {
			Logger.logInfo("Could not execute delete element command: " + deleteChildCommand);
		}
		return null;	
	}
	
	private EditPart getParentEditPart(Element element, EditPart editPart) {
		if (element instanceof Swimlane || element instanceof Timer) {
			return getRootEditPart(editPart);
		} else {
			return editPart.getParent();
		}
	}
	
	private EditPart getRootEditPart(EditPart editPart) {
		EditPart result = editPart;
		while (result.getParent() != null && !(result.getParent() instanceof RootEditPart)) {
			result = result.getParent();
		}
		return result;
	}

}
