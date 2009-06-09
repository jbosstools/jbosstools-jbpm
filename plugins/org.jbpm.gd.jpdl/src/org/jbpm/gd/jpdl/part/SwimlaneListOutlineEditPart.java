package org.jbpm.gd.jpdl.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.model.ProcessDefinition;
import org.jbpm.gd.jpdl.model.Swimlane;

public class SwimlaneListOutlineEditPart extends OutlineEditPart {
	
	public SwimlaneListOutlineEditPart(ProcessDefinition model) {
		super(model);
	}
	
	protected Image getImage() {
		String iconPath = "icons/full/obj16/swimlanes_multiple.gif";
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(
				Platform.getBundle("org.jbpm.gd.jpdl").getEntry(iconPath));
		return SharedImages.INSTANCE.getImage(descriptor);
	}

	protected String getText() {
		return "Swimlanes";
	}
	
	protected List getModelChildren() {
		List result = new ArrayList();
		Swimlane[] swimlanes = ((ProcessDefinition)getModel()). getSwimlanes();
		for (int i = 0; i < swimlanes.length; i++) {
			result.add(swimlanes[i]);
		}
		return result;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("swimlaneAdd")) {
			handleChildAdd(evt.getNewValue());
		} else if (eventName.equals("swimlaneRemove")) {
			refreshChildren();
			getViewer().select(this);
		} else {
			super.propertyChange(evt);
		}
	}
}
