package org.jboss.tools.flow.common.wrapper;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Container;
import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.properties.DefaultContainerWrapperPropertySource;
import org.jboss.tools.flow.common.strategy.AcceptsElementStrategy;
import org.jboss.tools.flow.common.strategy.AcceptsIncomingConnectionStrategy;
import org.jboss.tools.flow.common.strategy.AcceptsOutgoingConnectionStrategy;

public class DefaultContainerWrapper extends AbstractContainerWrapper {
	
	private AcceptsElementStrategy acceptsElementStrategy;
	private AcceptsIncomingConnectionStrategy incomingConnectionStrategy;
	private AcceptsOutgoingConnectionStrategy outgoingConnectionStrategy;
	private DefaultContainerWrapperPropertySource propertySource;

	protected void internalSetName(String name) {
		getNode().setName(name);
	}

	protected void internalAddElement(NodeWrapper element) {
        Node node = (Node)element.getElement();
        List<Node> nodes = ((Flow)getFlowWrapper().getElement()).getNodes();
        long id = 0;
        for (Node n: nodes) {
            if (n.getId() > id) {
                id = n.getId();
            }
        }
        node.setId(++id);
        ((Container)getParent().getElement()).addNode(node); 
	}


	protected void internalRemoveElement(NodeWrapper element) {
		((Container)getParent().getElement()).removeNode((Node)element.getElement()); 
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


	public String getId() {
		long id = getNode().getId();
		return id == -1 ? null : getNode().getId() + "";
	}

	public String getName() {
		return getNode().getName();
	}

	public Node getNode() {
		return (Node)getElement();
	}
	
    public boolean acceptsElement(NodeWrapper wrapper) {
    	if (wrapper == null) {
    		return false;
    	} else if (acceptsElementStrategy != null) {
    		return acceptsElementStrategy.acceptsElement(wrapper.getElement());
    	} else {
    		return getParent().acceptsElement(wrapper);
    	}
    }
    
    public void setAcceptsElementStrategy(AcceptsElementStrategy strategy) {
    	this.acceptsElementStrategy = strategy;
    }
    
	public void setAcceptsIncomingConnectionStrategy(AcceptsIncomingConnectionStrategy strategy) {
		this.incomingConnectionStrategy = strategy;
	}

	public void setAcceptsOutgoingConnectionStrategy(AcceptsOutgoingConnectionStrategy strategy) {
		this.outgoingConnectionStrategy = strategy;
	}

	public boolean acceptsIncomingConnection(
			ConnectionWrapper connectionWrapper, NodeWrapper sourceWrapper) {
		if (connectionWrapper == null || sourceWrapper == null) {
			return false;
		} else if (incomingConnectionStrategy != null) {
			return incomingConnectionStrategy.acceptsIncomingConnection(
					(Connection)connectionWrapper.getElement(), 
					(Node)sourceWrapper.getElement());
		} else {
			return super.acceptsIncomingConnection(connectionWrapper, sourceWrapper);
		}
	}

	public boolean acceptsOutgoingConnection(
			ConnectionWrapper connectionWrapper, NodeWrapper targetWrapper) {
		if (connectionWrapper == null || targetWrapper == null) {
			return false;
		} else if (outgoingConnectionStrategy != null) {
			return outgoingConnectionStrategy.acceptsOutgoingConnection(
					(Connection)connectionWrapper.getElement(), 
					(Node)targetWrapper.getElement());
		} else {
			return super.acceptsOutgoingConnection(connectionWrapper, targetWrapper);
		}
	}

    protected IPropertySource getPropertySource() {
    	if (propertySource == null) {
    		propertySource = new DefaultContainerWrapperPropertySource(this);
    	}
    	return propertySource;
    }
    
    @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
    	if (adapter == IPropertySource.class) {
    		return this;
    	}
    	return super.getAdapter(adapter);
    }
    
}
