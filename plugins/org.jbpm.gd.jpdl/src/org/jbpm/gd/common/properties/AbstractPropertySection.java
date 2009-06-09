package org.jbpm.gd.common.properties;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.jbpm.gd.common.editor.Editor;
import org.jbpm.gd.common.editor.OutlineViewer;


public class AbstractPropertySection extends org.eclipse.ui.views.properties.tabbed.AbstractPropertySection {
	
	protected Editor editor;
	
 	public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        if (part instanceof Editor) {
        	editor = ((Editor)part);
         }
        if (part instanceof ContentOutline) {
        	IPage page = ((ContentOutline)part).getCurrentPage();
        	if (page instanceof OutlineViewer) {
        		editor = ((OutlineViewer)page).getEditor();
        	}
        }
    }
 	
 	public CommandStack getCommandStack() {
 		return editor == null ? null : editor.getCommandStack();
 	}
 	
}