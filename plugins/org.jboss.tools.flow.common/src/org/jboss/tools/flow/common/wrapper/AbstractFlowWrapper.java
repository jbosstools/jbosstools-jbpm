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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A wrapper for a process element.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public abstract class AbstractFlowWrapper extends AbstractWrapper implements FlowWrapper {

    private Map<String, NodeWrapper> nodeWrapperMap = new HashMap<String, NodeWrapper>();
    private List<NodeWrapper> nodeWrapperList = new ArrayList<NodeWrapper>();
    
    public abstract Integer getRouterLayout();
    
    public void setRouterLayout(Integer newLayout) {
    	Integer oldLayout = getRouterLayout();
    	internalSetRouterLayout(newLayout);
    	notifyListeners(CHANGE_VISUAL, "routerLayout", this, oldLayout, newLayout);
    }
    
    protected void internalSetRouterLayout(Integer routerLayout) {
    }

    public List<NodeWrapper> getNodeWrappers() {
        return Collections.unmodifiableList(
            new ArrayList<NodeWrapper>(nodeWrapperList));
    }
    
    public NodeWrapper getNodeWrapper(String id) {
        return (NodeWrapper) nodeWrapperMap.get(id);
    }
    
    public void addElement(NodeWrapper nodeWrapper) {
    	if (!acceptsElement(nodeWrapper)) return;
        internalAddElement(nodeWrapper);
		localAddElement(nodeWrapper);
		notifyListeners(ADD_ELEMENT, "node", this, null, nodeWrapper);
    }
    
    public void localAddElement(NodeWrapper element) {
        nodeWrapperMap.put(element.getId(), element);
        nodeWrapperList.add(element);
        element.setParent(this);
    }
    
    public boolean acceptsElement(NodeWrapper element) {
    	return true;
    }
    
    protected abstract void internalAddElement(NodeWrapper element);
    
    public void localRemoveElement(NodeWrapper element) {
        nodeWrapperMap.remove(element.getId());
        nodeWrapperList.remove(element);    	
    }
    
    public void removeElement(NodeWrapper element) {
    	localRemoveElement(element);
        internalRemoveElement(element);
        notifyListeners(REMOVE_ELEMENT, "node", this, element, null);
    }
    
    protected abstract void internalRemoveElement(NodeWrapper element);
    
    public FlowWrapper getFlowWrapper() {
        return this;
    }
    
}
