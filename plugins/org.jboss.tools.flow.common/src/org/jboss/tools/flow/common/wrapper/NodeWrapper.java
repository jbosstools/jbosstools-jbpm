package org.jboss.tools.flow.common.wrapper;

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

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Wrapper of a model element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public interface NodeWrapper extends Wrapper {
	
    String getId();
    String getName();
    void setName(String name);
    
    void setConstraint(Rectangle constraint);
    Rectangle getConstraint();
    
    void setParent(ContainerWrapper parent);
    ContainerWrapper getParent();
    
    void setLabel(LabelWrapper label);
    LabelWrapper getLabel();
    
    List<ConnectionWrapper> getOutgoingConnections();
    List<ConnectionWrapper> getIncomingConnections();
    void addIncomingConnection(ConnectionWrapper connection);
    void localAddIncomingConnection(ConnectionWrapper connection);
    void removeIncomingConnection(ConnectionWrapper connection);
    void addOutgoingConnection(ConnectionWrapper connection);
    void localAddOutgoingConnection(ConnectionWrapper connection);
    void removeOutgoingConnection(ConnectionWrapper connection);
    boolean acceptsIncomingConnection(ConnectionWrapper connection, NodeWrapper source);
    boolean acceptsOutgoingConnection(ConnectionWrapper connection, NodeWrapper target);
    
}
