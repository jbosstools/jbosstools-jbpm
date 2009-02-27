package org.jboss.tools.flow.jpdl4.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.graphics.Color;
import org.jboss.tools.flow.common.figure.RoundedRectangleElementFigure;

public class TaskFigure extends RoundedRectangleElementFigure {
	
	private static final Color veryLightBlue = new Color(null, 246, 247, 255);
	private static final Color lightBlue = new Color(null, 3, 104, 154);
	
	protected void paintChildren(Graphics graphics) {
		Color foregroundColor = graphics.getForegroundColor();
		Color backgroundColor = graphics.getBackgroundColor();
		graphics.setBackgroundColor(veryLightBlue);
		graphics.setForegroundColor(lightBlue);
		super.paintChildren(graphics);
		graphics.setBackgroundColor(backgroundColor);
		graphics.setForegroundColor(foregroundColor);
	}
	
	protected void customizeFigure() {
		super.customizeFigure();
		getLabel().setForegroundColor(ColorConstants.darkGray);
		rectangle.setLineWidth(2);
	}
	
}
