package org.jboss.tools.flow.common.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;

public class DiamondDecoration extends PolygonDecoration {

	public static final PointList DIAMOND_TIP = new PointList();

	static {
		DIAMOND_TIP.addPoint(0, 0);
		DIAMOND_TIP.addPoint(-2, 2);
		DIAMOND_TIP.addPoint(-4, 0);
		DIAMOND_TIP.addPoint(-2, -2);
	}
	
	public DiamondDecoration() {
		setTemplate(DIAMOND_TIP);
		setScale(3.5, 2);
		setFill(true);
		setBackgroundColor(ColorConstants.white);
		setOpaque(true);
	}
	
}
