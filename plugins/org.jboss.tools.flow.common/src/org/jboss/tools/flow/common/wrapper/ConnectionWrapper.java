package org.jboss.tools.flow.common.wrapper;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;

public interface ConnectionWrapper extends Wrapper {

	int CHANGE_BENDPOINTS = 1;
	
	NodeWrapper getSource();
	NodeWrapper getTarget();
	
	void disconnect();
	void connect(NodeWrapper source, NodeWrapper target);
	
	void addBendpoint(int index, Point location);
	void replaceBendpoint(int index, Point newLocation);
	void removeBendpoint(int index);
	List<Point> getBendpoints();
	
	void setLabel(LabelWrapper label);
	LabelWrapper getLabel();
	
}
