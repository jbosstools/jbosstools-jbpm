package org.jboss.tools.process.jpdl4.graph.wrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.flow.editor.core.AbstractConnectionWrapper;
import org.jboss.tools.flow.editor.core.NodeWrapper;
import org.jboss.tools.flow.jpdl4.core.Node;
import org.jboss.tools.flow.jpdl4.core.Transition;

public class TransitionWrapper extends AbstractConnectionWrapper {
		
	public TransitionWrapper() {
	}
	
	public Transition getTransition() {
	    return (Transition)getElement();
	}
	
	public void localSetTransition(Transition transition) {
	    setElement(transition);
	}
	
	public void connect(NodeWrapper source, NodeWrapper target) {
		super.connect(source, target);
		Node from = ((BaseNodeWrapper) getSource()).getNode();
		Node to = ((BaseNodeWrapper) getTarget()).getNode();
		setElement(new Transition(from, to));		
	}

    protected List<Point> internalGetBendpoints() {
        return (List<Point>) stringToBendpoints((String) getTransition().getMetaData("bendpoints"));
    }
    
    protected void internalSetBendpoints(List<Point> bendpoints) {
        getTransition().setMetaData("bendpoints", bendpointsToString(bendpoints));
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
	
}
