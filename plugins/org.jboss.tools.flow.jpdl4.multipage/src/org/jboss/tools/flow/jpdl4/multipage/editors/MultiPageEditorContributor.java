/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.flow.jpdl4.multipage.editors;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.jboss.tools.flow.common.Activator;
import org.jboss.tools.flow.common.action.HorizontalAutoLayoutAction;
import org.jboss.tools.flow.common.action.VerticalAutoLayoutAction;
import org.jboss.tools.flow.common.editor.DropDownMenuWithDefaultAction;

/**
 * The contributor is for build actions for two editors of the multipage editor
 * 
 * @author Grid Qian
 */
public class MultiPageEditorContributor extends
		MultiPageEditorActionBarContributor {

	// zoom combo button for jpdl editor
	private ZoomComboContributionItem zoomCombo = null;
	// a map to contain the id of a retarget action to the retarget action
	// instance
	private Map<String, RetargetAction> actionMap = new HashMap<String, RetargetAction>();

	private static final String[] WORKBENCH_ACTION_IDS = {
			ActionFactory.PRINT.getId(), ActionFactory.DELETE.getId(),
			ActionFactory.SAVE.getId(), ActionFactory.UNDO.getId(),
			ActionFactory.REDO.getId(), ActionFactory.CUT.getId(),
			ActionFactory.COPY.getId(), ActionFactory.PASTE.getId(),
			ActionFactory.SELECT_ALL.getId(), ActionFactory.FIND.getId(),
			IDEActionFactory.BOOKMARK.getId() };
	private static final String[] TEXTEDITOR_ACTION_IDS = {
			ITextEditorActionConstants.PRINT,
			ITextEditorActionConstants.DELETE, ITextEditorActionConstants.SAVE,
			ITextEditorActionConstants.UNDO, ITextEditorActionConstants.REDO,
			ITextEditorActionConstants.CUT, ITextEditorActionConstants.COPY,
			ITextEditorActionConstants.PASTE,
			ITextEditorActionConstants.SELECT_ALL,
			ITextEditorActionConstants.FIND, IDEActionFactory.BOOKMARK.getId() };

	/*
	 * Creates a multi-page contributor.
	 */
	public MultiPageEditorContributor() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorActionBarContributor#init(org.eclipse.ui.
	 * IActionBars)
	 */
	public void init(IActionBars bars) {
		buildActions(bars);
		contributeJpdlEditorToolBarAction(bars.getToolBarManager());
		super.init(bars);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.MultiPageEditorActionBarContributor#setActivePage
	 * (org.eclipse.ui.IEditorPart)
	 */
	public void setActivePage(IEditorPart activeEditor) {
		IActionBars actionBars = getActionBars();
		if (actionBars == null)
			return;
		actionBars.clearGlobalActionHandlers();
		if (activeEditor instanceof InnerJpdlEditor) {
			addJpdlEditorActions((InnerJpdlEditor) activeEditor, actionBars);
		} else if (activeEditor instanceof StructuredTextEditor) {
			addXmlEditorActions((StructuredTextEditor) activeEditor, actionBars);
		}
		actionBars.updateActionBars();
	}

	/*
	 * set the global actions for jpdl editor
	 */
	private void addJpdlEditorActions(InnerJpdlEditor activeEditor,
			IActionBars actionBars) {
		// get the registry of actions from jpdl editor
		ActionRegistry registry = (ActionRegistry) activeEditor
				.getAdapter(ActionRegistry.class);
		
		// set jpdl global action to workbench global action
		for (int i = 0; i < WORKBENCH_ACTION_IDS.length; i++) {
			actionBars.setGlobalActionHandler(WORKBENCH_ACTION_IDS[i], registry
					.getAction(WORKBENCH_ACTION_IDS[i]));
		}
		
		// set the retarget action of jpdl editor to jpdl golbal action
		String[] keys = actionMap.keySet().toArray(
				new String[actionMap.keySet().size()]);
		for (int i = 0; i < keys.length; i++) {
			actionBars.setGlobalActionHandler(keys[i], registry
					.getAction(keys[i]));
		}
		
		// make zoomCombo visible
		zoomCombo.setVisible(true);
		actionBars.getToolBarManager().update(true);
	
	}

	/*
	 * set the global actions for xml editor
	 */
	private void addXmlEditorActions(StructuredTextEditor activeEditor,
			IActionBars actionBars) {	
		// set the global actions of jpdl editor are unable.
		String[] keys = actionMap.keySet().toArray(
				new String[actionMap.keySet().size()]);
		for (int i = 0; i < keys.length; i++) {
			actionBars.setGlobalActionHandler(keys[i], null);
		}
		
		//set the global actions to xml editor global action
		for (int i = 0; i < WORKBENCH_ACTION_IDS.length; i++) {
			actionBars.setGlobalActionHandler(WORKBENCH_ACTION_IDS[i],
					activeEditor.getAction(TEXTEDITOR_ACTION_IDS[i]));
		}
		zoomCombo.setVisible(false);
	}

	/*
	 * create retarget actions for jpdl editor
	 */
	private void buildActions(IActionBars actionBars) {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE));
		addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));
		addRetargetAction(new RetargetAction(
				GEFActionConstants.TOGGLE_GRID_VISIBILITY, "Grid"));
		RetargetAction verticalAutoLayoutAction = new RetargetAction(
				VerticalAutoLayoutAction.ID, null);
		verticalAutoLayoutAction.setImageDescriptor(ImageDescriptor
				.createFromURL(Activator.getDefault().getBundle().getEntry(
						"icons/layoutV.gif")));
		addRetargetAction(verticalAutoLayoutAction);
		RetargetAction horizontalAutoLayoutAction = new RetargetAction(
				HorizontalAutoLayoutAction.ID, null);
		horizontalAutoLayoutAction.setImageDescriptor(ImageDescriptor
				.createFromURL(Activator.getDefault().getBundle().getEntry(
						"icons/layoutH.gif")));
		addRetargetAction(horizontalAutoLayoutAction);
	}

	/*
	 * add the retargetaction to the map
	 */
	protected void addRetargetAction(RetargetAction action) {
		actionMap.put(action.getId(), action);
		getPage().addPartListener(action);
	}

	/*
	 * add the retarget actions to jpdl tool bar
	 */
	public void contributeJpdlEditorToolBarAction(IToolBarManager toolBarManager) {
		toolBarManager.add(actionMap.get(ActionFactory.UNDO.getId()));
		toolBarManager.add(actionMap.get(ActionFactory.REDO.getId()));
		toolBarManager.add(new Separator());
		zoomCombo = new ZoomComboContributionItem(getPage());
		toolBarManager.add(zoomCombo);
		toolBarManager.add(new Separator());

		DropDownMenuWithDefaultAction alignMenu = new DropDownMenuWithDefaultAction(
				actionMap.get(GEFActionConstants.ALIGN_LEFT));
		alignMenu.add(actionMap.get(GEFActionConstants.ALIGN_LEFT));
		alignMenu.add(actionMap.get(GEFActionConstants.ALIGN_CENTER));
		alignMenu.add(actionMap.get(GEFActionConstants.ALIGN_RIGHT));
		alignMenu.add(new Separator());
		alignMenu.add(actionMap.get(GEFActionConstants.ALIGN_TOP));
		alignMenu.add(actionMap.get(GEFActionConstants.ALIGN_MIDDLE));
		alignMenu.add(actionMap.get(GEFActionConstants.ALIGN_BOTTOM));
		toolBarManager.add(alignMenu);

		toolBarManager.add(new Separator());
		toolBarManager.add(actionMap
				.get(GEFActionConstants.TOGGLE_GRID_VISIBILITY));

		toolBarManager.add(new Separator());
		toolBarManager.add(actionMap.get(VerticalAutoLayoutAction.ID));
		toolBarManager.add(actionMap.get(HorizontalAutoLayoutAction.ID));
	}

	public void dispose() {
		String[] keys = actionMap.keySet().toArray(
				new String[actionMap.keySet().size()]);
		for (int i = 0; i < keys.length; i++) {
			RetargetAction action = (RetargetAction) actionMap.get(keys[i]);
			getPage().removePartListener(action);
			action.dispose();
		}
		actionMap.clear();
		actionMap = null;

		zoomCombo.dispose();
	}
}
