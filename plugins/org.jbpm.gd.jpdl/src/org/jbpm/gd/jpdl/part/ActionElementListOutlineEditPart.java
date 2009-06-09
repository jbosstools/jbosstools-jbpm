package org.jbpm.gd.jpdl.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.model.ActionElement;
import org.jbpm.gd.jpdl.model.ActionElementContainer;
import org.jbpm.gd.jpdl.model.ProcessDefinition;

public class ActionElementListOutlineEditPart extends OutlineEditPart {
	
	public ActionElementListOutlineEditPart(ProcessDefinition model) {
		super(model);
	}

	protected Image getImage() {
		String iconPath = "icons/full/obj16/action_elements_multiple.gif";
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(
				Platform.getBundle("org.jbpm.gd.jpdl").getEntry(iconPath));
		return SharedImages.INSTANCE.getImage(descriptor);
	}

	protected String getText() {
		return "Action Elements";
	}
	
	protected List getModelChildren() {
		List result = new ArrayList();
		ActionElement[] actionElements = ((ActionElementContainer)getModel()).getActionElements();
		for (int i = 0; i < actionElements.length; i++) {
			result.add(actionElements[i]);
		}
		return result;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("actionElementAdd")) {
			handleChildAdd(evt.getNewValue());
		} else if (eventName.equals("actionElementRemove")) {
			refreshChildren();
			getViewer().select(this);
		} else {
			super.propertyChange(evt);
		}
	}
}
