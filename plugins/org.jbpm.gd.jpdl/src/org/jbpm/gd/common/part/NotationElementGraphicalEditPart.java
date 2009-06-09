package org.jbpm.gd.common.part;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.IActionFilter;
import org.jbpm.gd.common.notation.AbstractNotationElement;

public interface NotationElementGraphicalEditPart  
extends EditPart, PropertyChangeListener, IActionFilter {
	
	public AbstractNotationElement getNotationElement();

}
