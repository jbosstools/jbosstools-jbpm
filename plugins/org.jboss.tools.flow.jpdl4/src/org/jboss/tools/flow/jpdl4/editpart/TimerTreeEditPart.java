package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Timer;
import org.jboss.tools.flow.jpdl4.util.SharedImages;

public class TimerTreeEditPart extends JpdlTreeEditPart implements ElementTreeEditPart {
	
	public TimerTreeEditPart(Wrapper wrapper) {
		super(wrapper);
	}
	
	protected Image getImage() {
		String iconPath = "icons/16/timer.gif";
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(
				Platform.getBundle("org.jboss.tools.flow.jpdl4").getEntry(iconPath));
		return SharedImages.INSTANCE.getImage(descriptor);
	}
	
	protected String getText() {
		String name = (String)((Wrapper)getModel()).getPropertyValue(Timer.DUE_DATE);
		if (name != null && !("".equals(name))) return name;
		name = (String)((Wrapper)getModel()).getPropertyValue(Timer.DUE_DATETIME);
		if (name != null && !("".equals(name))) return name;
		name = (String)((Wrapper)getModel()).getPropertyValue(Timer.REPEAT);
		if (name != null && !("".equals(name))) return name;
		return "timer";
	}
	
    public void modelChanged(ModelEvent event) {
    	if (event.getChangeType() == Wrapper.CHANGE_PROPERTY) {
    		refreshVisuals();
    	}
    }
    
    public void activate() {
        super.activate();
        ((Wrapper)getModel()).addListener(this);
    }

    public void deactivate() {
    	((Wrapper)getModel()).removeListener(this);
        super.deactivate();
    }
}
