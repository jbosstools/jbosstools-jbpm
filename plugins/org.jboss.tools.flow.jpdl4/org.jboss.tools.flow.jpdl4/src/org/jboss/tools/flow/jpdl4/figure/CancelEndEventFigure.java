package org.jboss.tools.flow.jpdl4.figure;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.jpdl4.Activator;

public class CancelEndEventFigure extends EndEventFigure { 

	private static final Image icon = ImageDescriptor.createFromURL(
			Activator.getDefault().getBundle().getEntry(
					"icons/48/end_event_cancel.png")).createImage();

	protected void customizeFigure() {
		setIcon(icon);
	}

}
