package org.jbpm.gd.jpdl.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.model.EventContainer;
import org.jbpm.gd.jpdl.model.NodeElement;
import org.jbpm.gd.jpdl.model.Transition;

public class TransitionListOutlineEditPart extends OutlineEditPart {
	
	public TransitionListOutlineEditPart(EventContainer model) {
		super(model);
	}

	protected Image getImage() {
		String iconPath = "icons/full/obj16/transitions_multiple.gif";
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(
				Platform.getBundle("org.jbpm.gd.jpdl").getEntry(iconPath));
		return SharedImages.INSTANCE.getImage(descriptor);
	}

	protected String getText() {
		return "Transitions";
	}
	
	protected List getModelChildren() {
		List result = new ArrayList();
		Transition[] transitions = ((NodeElement)getModel()). getTransitions();
		for (int i = 0; i < transitions.length; i++) {
			result.add(transitions[i]);
		}
		return result;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("transitionAdd")) {
			handleChildAdd(evt.getNewValue());
		} else if (eventName.equals("transitionRemove")) {
			refreshChildren();
			getViewer().select(this);
		} else {
			super.propertyChange(evt);
		}
	}
}
