package org.jboss.tools.flow.jpdl4.figure;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.jpdl4.Activator;


public class StartEventFigure extends EventFigure { 
    
    private static final Image icon = ImageDescriptor.createFromURL(
		Activator.getDefault().getBundle().getEntry("icons/48/start_event_empty.png")).createImage();;
    
    protected void customizeFigure() {
        setIcon(icon);
    }
    
}
    
