package org.jbpm.gd.common.figure;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jbpm.gd.jpdl.Constants;

public class NodeContainerBorder extends AbstractBorder {

	protected static Insets insets = new Insets(8, 8, 8, 8);

	public Insets getInsets(IFigure figure) {
		return insets;
	}

	public void paint(IFigure figure, Graphics g, Insets in) {
		Rectangle r = figure.getBounds().getCropped(in);
		g.setForegroundColor(Constants.veryLightGray);
		g.setLineWidth(8);
		g.drawLine(r.x, r.y + 4, r.right(), r.y + 4);
		g.drawLine(r.x, r.bottom() - 4, r.right(), r.bottom() - 4);
		g.drawLine(r.x + 4, r.y + 4, r.x + 4, r.bottom() - 4);
		g.drawLine(r.right() - 4, r.bottom() - 4, r.right() - 4, r.y + 4);
		g.setForegroundColor(ColorConstants.lightGray);
		g.setLineWidth(1);
		g.drawLine(r.x, r.y, r.right() - 2, r.y);
		g.drawLine(r.right() - 2, r.y, r.right() - 2, r.bottom() - 2);
		g.drawLine(r.right() - 2, r.bottom() - 2, r.x, r.bottom() - 2);
		g.drawLine(r.x, r.bottom() - 2, r.x, r.y);
	}

}
