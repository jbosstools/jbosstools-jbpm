package org.jboss.tools.flow.jpdl4.graph.editpart;

import org.jboss.tools.flow.editor.core.ConnectionFactory;
import org.jboss.tools.flow.editor.editpart.ConnectionEditPart;
import org.jboss.tools.flow.jpdl4.graph.wrapper.TransitionWrapperFactory;

public class TransitionEditPart extends ConnectionEditPart {

	protected ConnectionFactory getElementConnectionFactory() {
    	return new TransitionWrapperFactory();
    }
	
}
