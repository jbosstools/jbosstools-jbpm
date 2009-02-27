package org.jboss.tools.flow.jpdl4.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.jboss.tools.flow.common.policy.ElementContainerLayoutEditPolicy;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.jpdl4.command.AddProcessNodeCommand;

public class ProcessLayoutEditPolicy extends ElementContainerLayoutEditPolicy {

    protected Command getCreateCommand(CreateRequest request) {
        AddProcessNodeCommand command = new AddProcessNodeCommand();
        command.setParent((ContainerWrapper) getHost().getModel());
        NodeWrapper element = (NodeWrapper) request.getNewObject();
        element.setConstraint((Rectangle) getConstraintFor(request));
        command.setChild(element);
        return command;
    }

}
