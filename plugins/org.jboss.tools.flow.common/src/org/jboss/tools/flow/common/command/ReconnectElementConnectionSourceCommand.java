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
import org.jboss.tools.flow.common.wrapper.NodeWrapper;

/**
 * A command for reconnecting the source of a connection.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ReconnectElementConnectionSourceCommand extends Command {

	private ConnectionWrapper connection;
	private NodeWrapper target;
	private NodeWrapper oldSource;
	private NodeWrapper newSource;
    
    public boolean canExecute() {
        if (connection.getTarget().equals(newSource)) {
            return false;
        }
        for (ConnectionWrapper connection: newSource.getOutgoingConnections()) {
            if (connection.getTarget().equals(target) && !connection.getSource().equals(oldSource)) {
                return false;
            }
        }
        return newSource.acceptsOutgoingConnection(connection, target); //XXX    
    }

    public void execute() {
        if (newSource != null) {
        	connection.disconnect();
        	connection.connect(newSource, target); 
        }
    }

    public void setSource(NodeWrapper source) {
    	this.newSource = source;
    }

    public void setConnection(ConnectionWrapper connection) {
        this.connection = connection;
        this.target = connection.getTarget();
        this.oldSource = connection.getSource();
    }

    public void undo() {
    	connection.disconnect();
    	connection.connect(oldSource, target);    	
    }
    
    public void redo() {
    	connection.disconnect();
    	connection.connect(newSource, target);
    }
}
