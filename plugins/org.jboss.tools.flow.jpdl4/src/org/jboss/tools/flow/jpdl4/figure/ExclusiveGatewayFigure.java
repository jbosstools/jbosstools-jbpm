package org.jboss.tools.flow.jpdl4.figure;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.jpdl4.Activator;

public class ExclusiveGatewayFigure extends GatewayFigure { //DiamondElementFigure {

	private static final Image ICON = ImageDescriptor.createFromURL(
			Activator.getDefault().getBundle().getEntry(
					"icons/48/gateway_exclusive.png")).createImage();

	protected void customizeFigure() {
		setIcon(ICON);
	}
}
