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
 * A command for reconnecting the target of a connection.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ReconnectElementConnectionTargetCommand extends Command {

    private ConnectionWrapper connection;
    private NodeWrapper oldTarget;
    private NodeWrapper newTarget;
    private NodeWrapper source;
    
    
    public boolean canExecute() {
        if (connection.getSource().equals(newTarget)) {
            return false;
        }
        for (ConnectionWrapper connection: newTarget.getIncomingConnections()) {
            if (connection.getSource().equals(source) && !connection.getTarget().equals(oldTarget)) {
                return false;
            }
        }   
        return newTarget.acceptsIncomingConnection(connection, source);    
    }

    public void execute() {
        if (newTarget != null) {
        	connection.disconnect();
        	connection.connect(source, newTarget);    	
        }
    }

    public void setTarget(NodeWrapper target) {
        this.newTarget = target;
    }

    public void setConnection(ConnectionWrapper connection) {
        this.connection = connection;
        this.source = connection.getSource();
        this.oldTarget = connection.getTarget();
    }

    public void undo() {
    	connection.disconnect();
    	connection.connect(source, oldTarget);
    }
    
    public void redo() {
    	connection.disconnect();
    	connection.connect(source, newTarget);
    }
}
