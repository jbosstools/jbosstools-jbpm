package org.jboss.tools.flow.common.wrapper;

import org.eclipse.draw2d.geometry.Point;

public abstract class AbstractLabelWrapper extends AbstractWrapper implements
		LabelWrapper {

    private Point location = null;

    public Point getLocation() {
		return location;
	}

	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLocation(Point newLocation) {
		Point oldLocation = this.location;
		this.location = newLocation;
		notifyListeners(CHANGE_VISUAL, "location", this, oldLocation, newLocation);
	}

	public void setText(String text) {
		// TODO Auto-generated method stub

	}

}
