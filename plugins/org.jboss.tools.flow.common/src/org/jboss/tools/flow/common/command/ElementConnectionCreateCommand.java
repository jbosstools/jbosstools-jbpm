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
 * A command for creating an element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ElementConnectionCreateCommand extends Command {

	private ConnectionWrapper connection;
    private NodeWrapper source;
    private NodeWrapper target;

    public boolean canExecute() {
        if (source.equals(target)) {
            return false;
        }
        // Check for existence of connection already
        for (ConnectionWrapper connection: source.getOutgoingConnections()) {
            if (connection.getTarget().equals(target)) {
            	return false;
            }
        }
        return source.acceptsOutgoingConnection(connection, target)
            && target != null && target.acceptsIncomingConnection(connection, source);
    }

    public void execute() {
        connection.connect(source, target);
    }

    public NodeWrapper getSource() {
        return source;
    }

    public NodeWrapper getTarget() {
        return target;
    }

    public void redo() {
    	connection.connect(source, target);
    }

    public void setSource(NodeWrapper source) {
    	this.source = source;
    }

    public void setConnection(ConnectionWrapper connection) {
        this.connection = connection;
    }

    public void setTarget(NodeWrapper target) {
    	this.target = target;
    }

    public void undo() {
    	connection.disconnect();
    }

}
