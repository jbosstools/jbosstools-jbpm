package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.jpdl4.model.Process;
import org.jboss.tools.flow.jpdl4.util.SharedImages;

public class EventListenerContainerListTreeEditPart extends JpdlTreeEditPart implements ListTreeEditPart {
	
	public EventListenerContainerListTreeEditPart(Process process) {
		super(process);
	}
	
	protected Image getImage() {
		String iconPath = "icons/16/events_multiple.gif";
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(
				Platform.getBundle("org.jboss.tools.flow.jpdl4").getEntry(iconPath));
		return SharedImages.INSTANCE.getImage(descriptor);
	}
	
	protected String getText() {
		return "Event Listeners";
	}

}
