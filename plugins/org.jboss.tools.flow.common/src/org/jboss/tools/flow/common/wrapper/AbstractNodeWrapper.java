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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * Default wrapper of a model element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public abstract class AbstractNodeWrapper extends AbstractWrapper implements NodeWrapper {

    protected static IPropertyDescriptor[] descriptors;

    public static final String NAME = "Name";
    static {
        descriptors = new IPropertyDescriptor[] {
            new TextPropertyDescriptor(NAME, "Name"),
        };
    }
    
    private ContainerWrapper parent;
    private LabelWrapper label;
    private transient Rectangle constraint;
    private List<ConnectionWrapper> incomingConnections = new ArrayList<ConnectionWrapper>();
    private List<ConnectionWrapper> outgoingConnections = new ArrayList<ConnectionWrapper>();
   
	public void setConstraint(Rectangle constraint) {
		Rectangle oldConstraint = this.constraint;
	    this.constraint = constraint;
		internalSetConstraint(constraint);
		notifyListeners(CHANGE_VISUAL, "constraint", this, oldConstraint, constraint);
	}
	
	protected abstract void internalSetConstraint(Rectangle constraint);
	
	public Rectangle getConstraint() {
	    if (constraint == null) {
	        constraint = internalGetConstraint();
	    }
	    return constraint;
	}
	
	protected abstract Rectangle internalGetConstraint();

	public void setParent(ContainerWrapper parent) {
		this.parent = parent;
	}

	public ContainerWrapper getParent() {
		return parent;
	}
	
	public void setLabel(LabelWrapper label) {
		this.label = label;
	}
	
	public LabelWrapper getLabel() {
		return label;
	}

	public List<ConnectionWrapper> getOutgoingConnections() {
		return Collections.unmodifiableList(outgoingConnections);
	}

	public List<ConnectionWrapper> getIncomingConnections() {
		return Collections.unmodifiableList(incomingConnections);
	}

	public void addIncomingConnection(ConnectionWrapper connection) {
	    localAddIncomingConnection(connection);
		internalAddIncomingConnection(connection);
		notifyListeners(ADD_ELEMENT, "incomingConnection", this, null, connection);
	}
	
	public void localAddIncomingConnection(ConnectionWrapper connection) {
	    incomingConnections.add(connection);
	}

	protected void internalAddIncomingConnection(ConnectionWrapper connection) {
	}

	public void removeIncomingConnection(ConnectionWrapper connection) {
		incomingConnections.remove(connection);
		internalRemoveIncomingConnection(connection);
		notifyListeners(REMOVE_ELEMENT, "incomingConnection", this, connection, null);
	}

	protected void internalRemoveIncomingConnection(ConnectionWrapper connection) {
	}

	public void addOutgoingConnection(ConnectionWrapper connection) {
	    localAddOutgoingConnection(connection);
		internalAddOutgoingConnection(connection);
		notifyListeners(ADD_ELEMENT, "outgoingConnection", this, null, connection);
	}

    public void localAddOutgoingConnection(ConnectionWrapper connection) {
        outgoingConnections.add(connection);
    }

	protected void internalAddOutgoingConnection(ConnectionWrapper connection) {
	}

	public void removeOutgoingConnection(ConnectionWrapper connection) {
		outgoingConnections.remove(connection);
		internalRemoveOutgoingConnection(connection);
		notifyListeners(REMOVE_ELEMENT, "outgoingConnection", this, connection, null);
	}

	protected void internalRemoveOutgoingConnection(ConnectionWrapper connection) {
	}

	public void setName(String newLocation) {
		String oldName = getName();
		internalSetName(newLocation);
		notifyListeners(CHANGE_VISUAL, "name", this, oldName, newLocation);
	}

	protected void internalSetName(String name) {
	}

}
