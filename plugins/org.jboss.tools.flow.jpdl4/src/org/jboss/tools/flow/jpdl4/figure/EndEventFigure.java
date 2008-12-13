package org.jboss.tools.flow.jpdl4.figure;

import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.common.figure.AbstractElementFigure;
import org.jboss.tools.flow.jpdl4.Activator;

public class EndEventFigure extends AbstractElementFigure { //EventFigure {

	private static final Image icon = ImageDescriptor.createFromURL(
			Activator.getDefault().getBundle().getEntry(
					"icons/large/end_event_terminate.png")).createImage();

	public EndEventFigure() {
		setSize(32, 32);
	}

	public void setText(String text) {
	}

	public void setBounds(Rectangle r) {
		r.setSize(32, 32);
		super.setBounds(r);
	}

	protected void customizeFigure() {
		setIcon(icon);
	}

	public void setSelected(boolean b) {
		super.setSelected(b);
		((LineBorder) getBorder()).setWidth(b ? 3 : 0);
		repaint();
	}
	// protected void customizeFigure() {
	// super.customizeFigure();
	// ellipse.setLineWidth(5);
	// }

}
