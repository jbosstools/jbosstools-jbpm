package org.jboss.tools.flow.common.command;

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

import org.eclipse.gef.commands.Command;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;

/**
 * A command for splitting a connection.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class SplitConnectionCommand extends Command {

    private FlowWrapper parent;
    private NodeWrapper oldSource;
    private NodeWrapper oldTarget;
    private ConnectionWrapper oldConnection;
    private ConnectionWrapper secondConnection;    
    private NodeWrapper newElement;
    
    public void setNewSecondConnection(ConnectionWrapper connection) {
    	if (connection == null) {
    		throw new IllegalArgumentException("new second connection cannot be null");
    	}
    	this.secondConnection = connection;
    }
    
    public void execute() {
    	if (secondConnection == null) {
    		throw new IllegalStateException("new second connection is still null");
    	}
    	oldConnection.disconnect();
    	parent.addElement(newElement);
        newElement.setParent(parent);
    	oldConnection.connect(oldSource, newElement);
    	secondConnection.connect(newElement, oldTarget);
    }

    public void setParent(FlowWrapper flow) {
    	if (flow == null) {
    		throw new IllegalArgumentException("process is null");
    	}
        parent = flow;
    }

    public void setElementConnection(ConnectionWrapper connection) {
    	if (connection == null) {
    		throw new IllegalArgumentException("Element connection is null");
    	}
        this.oldConnection = connection;
        oldSource = connection.getSource();
        oldTarget = connection.getTarget();
    }

    public void setNewElement(NodeWrapper newElement) {
    	if (newElement == null) {
    		throw new IllegalArgumentException("NewElement is null");
    	}
        this.newElement = newElement;
        
    }

    public void undo() {
    	oldConnection.disconnect();
    	secondConnection.disconnect();
    	parent.removeElement(newElement);
    	newElement.setParent(null);
    	oldConnection.connect(oldSource, oldTarget);    	
    }
    
    public boolean canExecute() {
    	return parent.acceptsElement(newElement) && 
    		newElement.acceptsIncomingConnection(oldConnection, oldSource) &&
    		newElement.acceptsOutgoingConnection(secondConnection, oldTarget);
    }

}
