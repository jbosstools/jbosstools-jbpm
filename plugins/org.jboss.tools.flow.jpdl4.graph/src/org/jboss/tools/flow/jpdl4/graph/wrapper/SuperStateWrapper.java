package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.core.Container;
import org.jboss.tools.flow.common.core.Node;
import org.jboss.tools.flow.editor.core.AbstractContainerWrapper;
import org.jboss.tools.flow.editor.core.ContainerWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;
import org.jboss.tools.flow.jpdl4.core.Process;
import org.jboss.tools.flow.jpdl4.core.StartState;
import org.jboss.tools.flow.jpdl4.core.SuperState;

public class SuperStateWrapper extends AbstractContainerWrapper {
	
    public SuperStateWrapper() {
        setElement(new SuperState());
    }
    
	public Process getProcess() {
		ContainerWrapper parent = getParent();
		while (parent instanceof NodeWrapper && ((NodeWrapper)parent).getParent() != null) {
			parent = ((NodeWrapper)parent).getParent();
		}
		if (parent instanceof ProcessWrapper) {
			return ((ProcessWrapper)parent).getProcess();
		}
		return null;
	}
	
	public Node getNode() {
		return (Node)getElement();
	}
	
	protected void internalAddElement(NodeWrapper element) {
        Node node = (Node)element.getElement();
        long id = 0;
        for (Node n: getProcess().getNodes()) {
            if (n.getId() > id) {
                id = n.getId();
            }
        }
        node.setId(++id);
        ((Container)getParent().getElement()).addNode(node); 
	}

	protected void internalRemoveElement(NodeWrapper element) {
        getProcess().removeNode((Node)element.getElement()); 
	}

	protected Rectangle internalGetConstraint() {
		Node node = getNode();
		Integer x = (Integer) node.getMetaData("x");
		Integer y = (Integer) node.getMetaData("y");
		Integer width = (Integer) node.getMetaData("width");
		Integer height = (Integer) node.getMetaData("height");
		return new Rectangle(x == null ? 0 : x, y == null ? 0 : y,
				width == null ? -1 : width, height == null ? -1 : height);
	}

	protected void internalSetConstraint(Rectangle constraint) {
		Node node = getNode();
		node.setMetaData("x", constraint.x);
		node.setMetaData("y", constraint.y);
		node.setMetaData("width", constraint.width);
		node.setMetaData("height", constraint.height);
	}

	public boolean acceptsElement(NodeWrapper element) {
    	if (element.getElement() instanceof StartState) {
    		return getProcess().getStartState() == null;
    	}
    	return super.acceptsElement(element); 
	}

	public String getId() {
		long id = getNode().getId();
		return id == -1 ? null : getNode().getId() + "";
	}

	public String getName() {
		return getNode().getName();
	}

}
