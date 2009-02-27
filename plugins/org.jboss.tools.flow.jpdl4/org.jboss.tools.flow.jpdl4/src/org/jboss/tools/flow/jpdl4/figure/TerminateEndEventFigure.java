package org.jboss.tools.flow.jpdl4.figure;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.jpdl4.Activator;

public class TerminateEndEventFigure extends EndEventFigure { 

	private static final Image icon = ImageDescriptor.createFromURL(
			Activator.getDefault().getBundle().getEntry(
					"icons/48/end_event_terminate.png")).createImage();

	protected void customizeFigure() {
		setIcon(icon);
	}

}
