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
import org.jboss.tools.flow.common.command.AddChildCommand;
import org.jboss.tools.flow.common.wrapper.DefaultWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.EventListener;
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;

public class AddEventListenerHandler extends AbstractHandler implements IHandler {

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
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		if (editorPart == null) return null;
		Object object = editorPart.getAdapter(CommandStack.class);
		if (object == null || !(object instanceof CommandStack)) return null;
		CommandStack commandStack = (CommandStack)object;
		Wrapper parent = (Wrapper)model;
		Wrapper child = new DefaultWrapper();
		AddChildCommand addChildCommand = new AddChildCommand();
		if (editPart.getParent() != null && editPart.getParent() instanceof RootEditPart) {
			Wrapper wrapper = new DefaultWrapper();
			wrapper.setElement(new EventListener());
			addChildCommand.setType("eventListener");
			child.setElement(new EventListenerContainer());
			child.addChild("listener", wrapper);
		} else {
			addChildCommand.setType("listener");
			child.setElement(new EventListener());
		}
		addChildCommand.setChild(child);
		addChildCommand.setParent(parent);
		commandStack.execute(addChildCommand);
		return null;	
	}

}
