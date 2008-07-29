package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.jboss.tools.flow.common.core.Node;
import org.jboss.tools.flow.editor.core.AbstractRootWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;
import org.jboss.tools.flow.jpdl4.core.Process;
import org.jboss.tools.flow.jpdl4.core.StartState;

public class ProcessWrapper extends AbstractRootWrapper {

    public ProcessWrapper() {
        setElement(new Process());
    }

    public Process getProcess() {
        return (Process) getElement();
    }
    
    public String getName() {
        return getProcess().getName();
    }
    
    public void setName(String name) {
        getProcess().setName(name);
    }
    
    public Integer getRouterLayout() {
        Integer routerLayout = (Integer) getProcess().getMetaData("routerLayout");
        if (routerLayout == null) {
            return ROUTER_LAYOUT_MANUAL;
        }
        return routerLayout;
    }
    
    public void internalSetRouterLayout(Integer routerLayout) {
        getProcess().setMetaData("routerLayout", routerLayout);
    }
    
    protected void internalAddElement(NodeWrapper element) {
        Node node = ((BaseNodeWrapper) element).getNode();
        long id = 0;
        for (Node n: getProcess().getNodes()) {
            if (n.getId() > id) {
                id = n.getId();
            }
        }
        node.setId(++id);
        getProcess().addNode(node); 
    }

    protected void internalRemoveElement(NodeWrapper element) {
        getProcess().removeNode(((BaseNodeWrapper) element).getNode()); 
    }
    
    public boolean acceptsElement(NodeWrapper element) {
    	if (element.getElement() instanceof StartState) {
    		return getProcess().getStartState() == null;
    	}
    	return super.acceptsElement(element); 
    }
    
}
