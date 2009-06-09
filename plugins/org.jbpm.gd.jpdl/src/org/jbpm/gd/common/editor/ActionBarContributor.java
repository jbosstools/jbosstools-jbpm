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

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

public class ActionBarContributor extends
		MultiPageEditorActionBarContributor {
	
	ActionRegistry actionRegistry = new ActionRegistry();
	
    private static final String[] WORKBENCH_ACTION_IDS = {
    	ActionFactory.PRINT.getId(),
        ActionFactory.DELETE.getId(),
        ActionFactory.SAVE.getId(),
        ActionFactory.UNDO.getId(),
        ActionFactory.REDO.getId(),
        ActionFactory.CUT.getId(),
        ActionFactory.COPY.getId(),
        ActionFactory.PASTE.getId(),
        ActionFactory.SELECT_ALL.getId(),
        ActionFactory.FIND.getId(),
        IDEActionFactory.BOOKMARK.getId() 
    };
    private static final String[] TEXTEDITOR_ACTION_IDS = {
    	ITextEditorActionConstants.PRINT,
        ITextEditorActionConstants.DELETE,
        ITextEditorActionConstants.SAVE,
        ITextEditorActionConstants.UNDO,
        ITextEditorActionConstants.REDO,
        ITextEditorActionConstants.CUT,
        ITextEditorActionConstants.COPY,
        ITextEditorActionConstants.PASTE,
        ITextEditorActionConstants.SELECT_ALL,
        ITextEditorActionConstants.FIND,
        IDEActionFactory.BOOKMARK.getId()
    };
    
 	public void setActivePage(IEditorPart activeEditor) {
		IActionBars actionBars = getActionBars();
		if (actionBars == null) return;
		if (activeEditor instanceof GraphPage) {
			hookGlobalGraphicalEditorActions((GraphPage)activeEditor, actionBars);
		} else if (activeEditor instanceof StructuredTextEditor) {
			hookGlobalXmlEditorActions((StructuredTextEditor)activeEditor, actionBars);
		} else {
			actionBars.setGlobalActionHandler( GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY, null);			
		}
		actionBars.updateActionBars();
	}
	
	private void hookGlobalXmlEditorActions(
			StructuredTextEditor part, IActionBars actionBars) {
		for (int i = 0;i < WORKBENCH_ACTION_IDS.length; i++) {
			actionBars.setGlobalActionHandler(
					WORKBENCH_ACTION_IDS[i],
					part.getAction(TEXTEDITOR_ACTION_IDS[i]));
		}
		actionBars.setGlobalActionHandler( GEFActionConstants.TOGGLE_GRID_VISIBILITY, null);
	}

	private void hookGlobalGraphicalEditorActions(
			GraphPage part, IActionBars actionBars) {
		ActionRegistry registry = part.getEditor().getActionRegistry();
		for (int i = 0; i < WORKBENCH_ACTION_IDS.length; i++) {
			IAction action = registry.getAction(WORKBENCH_ACTION_IDS[i]);
			actionBars.setGlobalActionHandler(
					WORKBENCH_ACTION_IDS[i],
					action);
		}
		IAction action = registry.getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY);
		actionBars.setGlobalActionHandler( GEFActionConstants.TOGGLE_GRID_VISIBILITY, action);
	}

	protected void addRetargetAction(RetargetAction action) {
		actionRegistry.registerAction(action);
		getPage().addPartListener(action);
	}
	
	public void init(IActionBars bars) {
		buildActions();
		super.init(bars);
	}
	
	public void dispose() {
		RetargetAction action = (RetargetAction)actionRegistry.getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY);
		getPage().removePartListener(action);
		action.dispose();
		actionRegistry.dispose();
		super.dispose();
	}

	protected void buildActions() {
		addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY, 
				"Show Grid", IAction.AS_CHECK_BOX));
	}

	public void contributeToMenu(IMenuManager menubar) {
		super.contributeToMenu(menubar);
		MenuManager viewMenu = new MenuManager("View");
		viewMenu.add(actionRegistry.getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
		menubar.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
	}
	
}
