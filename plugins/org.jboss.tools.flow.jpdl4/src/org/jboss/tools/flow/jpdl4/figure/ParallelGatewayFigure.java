package org.jboss.tools.flow.jpdl4.figure;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.jpdl4.Activator;

public class ParallelGatewayFigure extends GatewayFigure { //DiamondElementFigure {

	private static final Image ICON = ImageDescriptor.createFromURL(
			Activator.getDefault().getBundle().getEntry(
					"icons/48/gateway_parallel.png")).createImage();

	protected void customizeFigure() {
		setIcon(ICON);
	}
	
}
