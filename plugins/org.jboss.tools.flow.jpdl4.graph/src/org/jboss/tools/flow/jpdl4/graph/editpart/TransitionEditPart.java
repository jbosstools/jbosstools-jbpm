package org.jboss.tools.flow.jpdl4.graph.editpart;

import org.jboss.tools.flow.editor.editpart.ConnectionEditPart;
import org.jboss.tools.flow.jpdl4.graph.wrapper.TransitionWrapper;

public class TransitionEditPart extends ConnectionEditPart {

	protected Class<?> getElementConnectionType() {
		return TransitionWrapper.class;
	}
	
}
