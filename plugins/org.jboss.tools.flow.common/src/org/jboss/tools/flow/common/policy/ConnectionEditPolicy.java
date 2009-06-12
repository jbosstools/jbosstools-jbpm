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

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.GroupRequest;
import org.jboss.tools.flow.common.command.DeleteConnectionCommand;
import org.jboss.tools.flow.common.command.SplitConnectionCommand;
import org.jboss.tools.flow.common.editpart.ConnectionEditPart;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;

/**
 * Policy for editing connections.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ConnectionEditPolicy extends org.eclipse.gef.editpolicies.ConnectionEditPolicy {

	private CreationFactory elementConnectionFactory;
	
	public void setElementConnectionFactory(CreationFactory elementConnectionFactory) {
		this.elementConnectionFactory = elementConnectionFactory;
	}
	
    public Command getCommand(Request request) {
        if (REQ_CREATE.equals(request.getType())) {
            return getSplitConnectionCommand(request);
        }
        return super.getCommand(request);
    }

    private PolylineConnection getConnectionFigure() {
        return ((PolylineConnection) ((ConnectionEditPart) getHost()).getFigure());
    }

    protected Command getDeleteCommand(GroupRequest request) {
        DeleteConnectionCommand cmd = new DeleteConnectionCommand();
        ConnectionWrapper connection = (ConnectionWrapper) getHost().getModel();
        cmd.setAntecedentTaskConnection(connection);
        cmd.setSource(connection.getSource());
        cmd.setTarget(connection.getTarget());
        return cmd;
    }

    protected Command getSplitConnectionCommand(Request request) {
    	if (elementConnectionFactory == null) {
    		throw new IllegalStateException("DefaultElementConnectionFactory is null");
    	}
        SplitConnectionCommand cmd = new SplitConnectionCommand();
        ConnectionWrapper elementConnection = (ConnectionWrapper)getHost().getModel();
        ConnectionWrapper newSecondConnection = (ConnectionWrapper)elementConnectionFactory.getNewObject();
        // Copy the configurationElement from the first connection as it is empty
        ((Element)newSecondConnection.getElement()).setMetaData(
        		"configurationElement", 
        		((Element)elementConnection.getElement()).getMetaData("configurationElement"));
        cmd.setElementConnection(elementConnection);
        cmd.setNewSecondConnection(newSecondConnection);
        cmd.setParent(((FlowWrapper) ((ConnectionEditPart) getHost())
            .getSource().getParent().getModel()));
        cmd.setNewElement(((NodeWrapper) ((CreateRequest) request).getNewObject()));
        return cmd;
    }

    public EditPart getTargetEditPart(Request request) {
        if (REQ_CREATE.equals(request.getType())) {
            return getHost();
        }
        return null;
    }

    public void eraseTargetFeedback(Request request) {
        if (REQ_CREATE.equals(request.getType())) {
            getConnectionFigure().setLineWidth(1);
        }
    }

    public void showTargetFeedback(Request request) {
        if (REQ_CREATE.equals(request.getType())) {
            getConnectionFigure().setLineWidth(2);
        }
    }

}
