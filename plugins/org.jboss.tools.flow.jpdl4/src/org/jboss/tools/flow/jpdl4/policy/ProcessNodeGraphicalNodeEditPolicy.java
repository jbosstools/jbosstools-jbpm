package org.jboss.tools.flow.jpdl4.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.policy.ElementNodeEditPolicy;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.jpdl4.model.SequenceFlow;

public class ProcessNodeGraphicalNodeEditPolicy extends ElementNodeEditPolicy {

   protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
    	if (request.getNewObject() instanceof ConnectionWrapper) {
    		initializeConnectionName((ConnectionWrapper)request.getNewObject());
    	}
    	return super.getConnectionCompleteCommand(request);
    }
    
    private void initializeConnectionName(ConnectionWrapper connectionWrapper) {
    	Element element = connectionWrapper.getElement();
    	if (!(element instanceof SequenceFlow)) return;
    	SequenceFlow sequenceFlow = (SequenceFlow)element;
    	sequenceFlow.setName("to " + getElement().getName());
    }
    
}
