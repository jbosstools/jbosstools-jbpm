/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jbpm.gd.common.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.StackAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;


public class ActionRegistry extends org.eclipse.gef.ui.actions.ActionRegistry {
	
	private IEditorPart editorPart;
	
	private List editPartActionIds;
	private List stackActionIds;
	private List editorActionIds;
	
	public ActionRegistry(IEditorPart editorPart) {
		initEditorPart(editorPart);
		initActionLists();
		initActions();
	}
	
	private void initEditorPart(IEditorPart part) {
		this.editorPart = part;
	}
	
	private void initActionLists() {
		editPartActionIds = new ArrayList();
		stackActionIds = new ArrayList();
		editorActionIds = new ArrayList();		
	}

	private void initActions() {
		addStackAction(new UndoAction(editorPart));
		addStackAction(new RedoAction(editorPart));	
		addEditPartAction(new DeleteAction((IWorkbenchPart)editorPart));
		addEditPartAction(new SaveAction(editorPart));
		registerAction(new PrintAction(editorPart));
	}
		
	private void addEditPartAction(WorkbenchPartAction action) {
		registerAction(action);
		editPartActionIds.add(action.getId());
	}
	
	private void addStackAction(StackAction action) {
		registerAction(action);
		
		stackActionIds.add(action.getId());
	}
	
	private void updateActions(List actionIds) {
		for(Iterator ids = actionIds.iterator(); ids.hasNext();) {
			IAction action = getAction(ids.next());
			if (null != action && action instanceof UpdateAction)
				((UpdateAction)action).update();
		}
	}
	
	public void updateStackActions() {
		updateActions(stackActionIds);
	}
	
	public void updateEditPartActions() {
		updateActions(editPartActionIds);
	}
	
	public void updateEditorActions() {
		updateActions(editorActionIds);
	}
	
}
