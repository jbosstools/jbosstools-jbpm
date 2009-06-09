package org.jbpm.gd.jpdl.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.model.Event;
import org.jbpm.gd.jpdl.model.EventContainer;

public class EventListOutlineEditPart extends OutlineEditPart {
	
	public EventListOutlineEditPart(EventContainer model) {
		super(model);
	}

	protected Image getImage() {
		String iconPath = "icons/full/obj16/events_multiple.gif";
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(
				Platform.getBundle("org.jbpm.gd.jpdl").getEntry(iconPath));
		return SharedImages.INSTANCE.getImage(descriptor);
	}

	protected String getText() {
		return "Events";
	}
	
	protected List getModelChildren() {
		List result = new ArrayList();
		Event[] events = ((EventContainer)getModel()). getEvents();
		for (int i = 0; i < events.length; i++) {
			result.add(events[i]);
		}
		return result;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("eventAdd")) {
			handleChildAdd(evt.getNewValue());
		} else if (eventName.equals("eventRemove")) {
			refreshChildren();
			getViewer().select(this);
		} else {
			super.propertyChange(evt);
		}
	}
}
