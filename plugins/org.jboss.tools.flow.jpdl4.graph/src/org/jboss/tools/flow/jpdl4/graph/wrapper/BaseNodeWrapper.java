package org.jboss.tools.flow.jpdl4.graph.wrapper;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.editor.core.AbstractConnectionWrapper;
import org.jboss.tools.flow.editor.core.AbstractNodeWrapper;
import org.jboss.tools.flow.jpdl4.core.Node;

public abstract class BaseNodeWrapper extends AbstractNodeWrapper {
	
    public void setNode(Node node) {
        setElement(node);
    }
    
    public Node getNode() {
        return (Node) getElement();
    }
    
    public String getId() {
        long id = getNode().getId();
        return id == -1 ? null : getNode().getId() + "";
    }

    public String getName() {
        return getNode().getName();
    }

    public void internalSetName(String name) {
        getNode().setName(name);    
        notifyListeners(CHANGE_VISUAL);
    }
    
    protected void internalSetConstraint(Rectangle constraint) {
        Node node = getNode();
        node.setMetaData("x", constraint.x);
        node.setMetaData("y", constraint.y);
        node.setMetaData("width", constraint.width);
        node.setMetaData("height", constraint.height);
    }
    
    public Rectangle internalGetConstraint() {
        Node node = getNode();
        Integer x = (Integer) node.getMetaData("x");
        Integer y = (Integer) node.getMetaData("y");
        Integer width = (Integer) node.getMetaData("width");
        Integer height = (Integer) node.getMetaData("height");
        return new Rectangle(
            x == null ? 0 : x,
            y == null ? 0 : y,
            width == null ? -1 : width,
            height == null ? -1 : height);
    }

    public boolean acceptsIncomingConnection(AbstractConnectionWrapper connection, BaseNodeWrapper source) {
        return source == null
    		|| ((BaseNodeWrapper) source).getNode().getNodeContainer() == getNode().getNodeContainer();
    }

    public boolean acceptsOutgoingConnection(AbstractConnectionWrapper connection, BaseNodeWrapper target) {
        return target == null
    		|| ((BaseNodeWrapper) target).getNode().getNodeContainer() == getNode().getNodeContainer();
    }
    
}
