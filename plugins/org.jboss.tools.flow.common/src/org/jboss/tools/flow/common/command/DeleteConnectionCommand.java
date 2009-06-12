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
 * A command for deleting a connection.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class DeleteConnectionCommand extends Command {

    private NodeWrapper source;
    private NodeWrapper target;
    private ConnectionWrapper connection;

    public void execute() {
    	connection.disconnect();
    }

    public void setSource(NodeWrapper action) {
        source = action;
    }

    public void setTarget(NodeWrapper action) {
        target = action;
    }

    public void setAntecedentTaskConnection(ConnectionWrapper connection) {
        this.connection = connection;
    }

    public void undo() {
    	connection.connect(source, target);
    }
}
