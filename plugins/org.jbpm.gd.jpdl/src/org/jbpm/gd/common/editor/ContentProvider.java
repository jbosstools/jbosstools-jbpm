package org.jbpm.gd.common.editor;

import org.eclipse.ui.IEditorInput;
import org.jbpm.gd.common.notation.RootContainer;

public interface ContentProvider {
	
	boolean saveToInput(IEditorInput input, RootContainer rootContainer);
	void addNotationInfo(RootContainer rootContainer, IEditorInput input);
	
	String getNotationInfoFileName(String semanticInfoFileName);
	String getDiagramImageFileName(String semanticInfoFileName);
	
}
