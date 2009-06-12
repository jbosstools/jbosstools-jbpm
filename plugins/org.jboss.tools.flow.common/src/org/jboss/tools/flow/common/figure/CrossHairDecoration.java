package org.jboss.tools.flow.common.figure;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;

public class CrossHairDecoration extends PolygonDecoration {

	public static final PointList CROSSHAIR_TIP = new PointList();

	static {
		CROSSHAIR_TIP.addPoint(0, 0);
		CROSSHAIR_TIP.addPoint(-1, 0);
		CROSSHAIR_TIP.addPoint(-1, -1);
		CROSSHAIR_TIP.addPoint(-1, 0);
		CROSSHAIR_TIP.addPoint(-2, 0);
		CROSSHAIR_TIP.addPoint(-1, 0);
		CROSSHAIR_TIP.addPoint(-1, 1);
		CROSSHAIR_TIP.addPoint(-1, 0);
	}

	public CrossHairDecoration() {
		setTemplate(CROSSHAIR_TIP);
		setScale(4, 4);
		setFill(false);
	}
	
}
