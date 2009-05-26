package org.jboss.tools.flow.jpdl4.editpart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;
import org.jboss.tools.flow.jpdl4.util.SharedImages;

public class EventListenerListTreeEditPart extends JpdlTreeEditPart implements ElementTreeEditPart {
	
	public EventListenerListTreeEditPart(Wrapper wrapper) {
		super(wrapper);
	}
	
	protected Image getImage() {
		String iconPath = "icons/16/events_multiple.gif";
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(
				Platform.getBundle("org.jboss.tools.flow.jpdl4").getEntry(iconPath));
		return SharedImages.INSTANCE.getImage(descriptor);
	}
	
	protected String getText() {
		String eventType = (String)((Wrapper)getModel()).getPropertyValue(EventListenerContainer.EVENT_TYPE);
		if (eventType != null && !("".equals(eventType))) return eventType;
		return "event type";
	}
	
	protected List<Object> getModelChildren() {
		List<Object> result = new ArrayList<Object>();
		Wrapper wrapper = (Wrapper)getModel();
		if (wrapper.getChildren(EventListenerContainer.LISTENERS) != null) {
			result.addAll(wrapper.getChildren(EventListenerContainer.LISTENERS));
		}
		return result;
	}
	

    public void activate() {
        super.activate();
        ((Wrapper)getModel()).addListener(this);
    }

    public void deactivate() {
    	((Wrapper)getModel()).removeListener(this);
        super.deactivate();
    }

    public void modelChanged(ModelEvent event) {
    	if (event.getChangeType() == Wrapper.REMOVE_ELEMENT) {
	    	EditPart parent = getParent();
	    	if (parent instanceof JpdlTreeEditPart) {
	    		ModelEvent modelEvent = new ModelEvent(Wrapper.REMOVE_ELEMENT, "eventListener", getModel(), null, null);
	    		((JpdlTreeEditPart)parent).modelChanged(modelEvent);
	    	}
	    	refreshChildren();
    	} else if (event.getChangeType() == Wrapper.ADD_ELEMENT) {
    		refreshChildren();
    		Object object = event.getNewValue();
    		EditPart editPart = (EditPart)getViewer().getEditPartRegistry().get(object);
    		if (editPart != null) {
    			getViewer().select(editPart);
    		}
    	}
    	else if (event.getChangeType() == Wrapper.CHANGE_PROPERTY) {
    		refreshVisuals();
    	}
    }
    
}
