package org.jboss.tools.flow.jpdl4.figure;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.common.figure.AbstractElementFigure;
import org.jboss.tools.flow.jpdl4.Activator;

public class ExclusiveGatewayFigure extends AbstractElementFigure { //DiamondElementFigure {

	private static final Image ICON = ImageDescriptor.createFromURL(
			Activator.getDefault().getBundle().getEntry(
					"icons/large/gateway_exclusive.png")).createImage();

	public ExclusiveGatewayFigure() {
		setSize(32, 32);
	}

	public void setText(String text) {
	}

	public void setBounds(Rectangle r) {
		r.setSize(32, 32);
		super.setBounds(r);
	}

	protected void customizeFigure() {
		setIcon(ICON);
	}
}
