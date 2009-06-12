package org.jboss.tools.flow.common.wrapper;

import org.eclipse.draw2d.geometry.Point;

public interface LabelWrapper extends Wrapper {
	
	void setText(String text);
	String getText();
	
	void setLocation(Point location);
	Point getLocation();

}
