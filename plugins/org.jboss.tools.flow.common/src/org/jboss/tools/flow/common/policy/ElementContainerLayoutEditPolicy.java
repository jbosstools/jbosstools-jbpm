package org.jboss.tools.flow.common.policy;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.jboss.tools.flow.common.command.AddNodeCommand;
import org.jboss.tools.flow.common.command.ChangeConstraintCommand;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;

/**
 * Policy for performing layout of a process.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ElementContainerLayoutEditPolicy extends XYLayoutEditPolicy {
    
    protected Command getCreateCommand(CreateRequest request) {
        AddNodeCommand command = new AddNodeCommand();
        command.setParent((ContainerWrapper) getHost().getModel());
        NodeWrapper element = (NodeWrapper) request.getNewObject();
        element.setConstraint((Rectangle) getConstraintFor(request));
        command.setChild(element);
        return command;
    }

    protected Command getDeleteDependantCommand(Request request) {
        return null;
    }

    protected Command createAddCommand(EditPart child, Object constraint) {
        // TODO this is needed to allow dragging of elements from one container to another
        return null;
    }

    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
        ChangeConstraintCommand command = new ChangeConstraintCommand();
        command.setElement((NodeWrapper) child.getModel());
        command.setConstraint((Rectangle)constraint);
        return command;
    }
}
