package org.jboss.tools.flow.jpdl4.editpart;

import org.jboss.tools.flow.common.editpart.ConnectionEditPart;
import org.jboss.tools.flow.common.wrapper.DefaultConnectionWrapper;

public class TransitionEditPart extends ConnectionEditPart {

	protected Class<?> getElementConnectionType() {
		return DefaultConnectionWrapper.class;
	}
	
}
