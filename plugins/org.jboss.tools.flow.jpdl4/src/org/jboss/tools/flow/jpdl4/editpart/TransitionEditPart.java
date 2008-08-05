package org.jboss.tools.flow.jpdl4.editpart;

import org.jboss.tools.flow.common.editpart.ConnectionEditPart;
import org.jboss.tools.flow.jpdl4.wrapper.TransitionWrapper;

public class TransitionEditPart extends ConnectionEditPart {

	protected Class<?> getElementConnectionType() {
		return TransitionWrapper.class;
	}
	
}
