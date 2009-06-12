package org.jboss.tools.flow.common.wrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.DefaultConnection;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.properties.WrapperPropertySource;


public class DefaultConnectionWrapper extends AbstractConnectionWrapper {
	
	private IPropertySource propertySource;

	public Connection getConnection() {
	    return (Connection)getElement();
	}
	
    protected List<Point> internalGetBendpoints() {
        return (List<Point>) stringToBendpoints((String) getConnection().getMetaData("bendpoints"));
    }
    
    protected void internalSetBendpoints(List<Point> bendpoints) {
        getConnection().setMetaData("bendpoints", bendpointsToString(bendpoints));
    }
    
    private String bendpointsToString(List<Point> bendpoints) {
        if (bendpoints == null) {
            return null;
        }
        String result = "[";
        for (Iterator<Point> iterator = bendpoints.iterator(); iterator.hasNext(); ) {
            Point point = iterator.next();
            result += point.x + "," + point.y + (iterator.hasNext() ? ";" : "");
        }
        result += "]";
        return result;
    }
    
    private List<Point> stringToBendpoints(String s) {
        List<Point> result = new ArrayList<Point>();
        if (s == null) {
            return result;
        }
        s = s.substring(1, s.length() - 1);
        String[] bendpoints = s.split(";");
        for (String bendpoint: bendpoints) {
            String[] xy = bendpoint.split(",");
            result.add(new Point(new Integer(xy[0]), new Integer(xy[1])));
        }
        return result;
    }
	
	
	public void connect(NodeWrapper source, NodeWrapper target) {
		Node from = (Node)source.getElement();
		Node to = (Node)target.getElement();
		Connection connection = getConnection();
		if (connection != null) {
			connection.setTo(to);
			connection.setFrom(from);
		} else {
			setElement(createConnection(from, to));		
		}
		super.connect(source, target);
	}

	protected Connection createConnection(Node from, Node to) {
		Connection result = new DefaultConnection(from, to);
		if (getConnection() != null) {
			result.setMetaData(
					"configurationElement", 
					getConnection().getMetaData("configurationElement"));
		}
		return result;
	}
	
    protected IPropertySource getPropertySource() {
    	if (propertySource == null) {
    		propertySource = new WrapperPropertySource(this);
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
