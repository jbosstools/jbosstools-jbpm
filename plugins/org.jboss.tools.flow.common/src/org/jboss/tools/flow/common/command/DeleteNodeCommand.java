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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;

/**
 * A command for deleting an element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class DeleteNodeCommand extends Command {

    private NodeWrapper child;
    private ContainerWrapper parent;
    
    private List<DeleteNodeCommand> embeddedCommands;
    
    private List<NodeWrapper> incomingElementWrappers = new ArrayList<NodeWrapper>();
    private List<NodeWrapper> outgoingElementWrappers = new ArrayList<NodeWrapper>();
    private List<ConnectionWrapper> incomingConnections = new ArrayList<ConnectionWrapper>();
    private List<ConnectionWrapper> outgoingConnections = new ArrayList<ConnectionWrapper>();
    
    
    private void deleteConnections(NodeWrapper element) {
    	for (ConnectionWrapper connection: element.getIncomingConnections()) {
    		incomingElementWrappers.add(connection.getSource());
    		incomingConnections.add(connection);
    	}
    	for (ConnectionWrapper connection: element.getOutgoingConnections()) {
    		outgoingElementWrappers.add(connection.getTarget());
    		outgoingConnections.add(connection);
    	} 
    	for (ConnectionWrapper connection: incomingConnections) {
    		connection.disconnect();
    	}
    	for (ConnectionWrapper connection: outgoingConnections) {
    		connection.disconnect();
    	}
    }
    
    private void initializeEmbeddedCommands() {
    	embeddedCommands = new ArrayList<DeleteNodeCommand>();
    	ContainerWrapper container = (ContainerWrapper)child;
    	List<NodeWrapper> children = container.getNodeWrappers();
    	for (NodeWrapper w : children) {
    		DeleteNodeCommand c = new DeleteNodeCommand();
    		c.setParent(container);
    		c.setChild(w);
    		embeddedCommands.add(c);
    	}
    }
    
    private void executeEmbeddedCommands() {
    	if (embeddedCommands == null) {
    		initializeEmbeddedCommands();
    	}
    	for (DeleteNodeCommand c : embeddedCommands) {
    		c.execute();
    	}
    }

    public void execute() {
    	if (child instanceof ContainerWrapper) {
    		executeEmbeddedCommands();
    	}
        deleteConnections(child);
        parent.removeElement(child);
    }

    private void restoreConnections() {
    	int i = 0;
    	for (ConnectionWrapper connection: incomingConnections) {
    		connection.connect((NodeWrapper) incomingElementWrappers.get(i), child);
    		i++;
    	}
    	i = 0;
    	for (ConnectionWrapper connection: outgoingConnections) {
    		connection.connect(child, (NodeWrapper) outgoingElementWrappers.get(i));
    		i++;
    	}
    	incomingConnections.clear();
    	incomingElementWrappers.clear();
    	outgoingConnections.clear();
    	outgoingElementWrappers.clear();
    }
    
    public void setChild(NodeWrapper child) {
        this.child = child;
    }

    public void setParent(ContainerWrapper parent) {
        this.parent = parent;
    }
    
    private void undoEmbeddedCommands() {
        for (DeleteNodeCommand c : embeddedCommands) {
        	c.undo();
        }
    }

    public void undo() {
        parent.addElement(child);
        restoreConnections();
        if (child instanceof ContainerWrapper) {
        	undoEmbeddedCommands();
        }
    }

}
