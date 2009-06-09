package org.jbpm.gd.jpdl.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.model.NodeElement;
import org.jbpm.gd.jpdl.model.NodeElementContainer;

public class NodeElementListOutlineEditPart extends OutlineEditPart {
	
	public NodeElementListOutlineEditPart(NodeElementContainer model) {
		super(model);
	}

	protected Image getImage() {
		String iconPath = "icons/full/obj16/node_elements_multiple.gif";
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(
				Platform.getBundle("org.jbpm.gd.jpdl").getEntry(iconPath));
		return SharedImages.INSTANCE.getImage(descriptor);
	}

	protected String getText() {
		return "Nodes";
	}
	
	protected List getModelChildren() {
		List result = new ArrayList();
		NodeElement[] nodes = ((NodeElementContainer)getModel()).getNodeElements();
		for (int i = 0; i < nodes.length; i++) {
			result.add(nodes[i]);
		}
		return result;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("nodeElementAdd") || 
				eventName.equals("startStateAdd")) {
			handleChildAdd(evt.getNewValue());
		} else if (eventName.equals("nodeElementRemove") || 
				eventName.equals("startStateRemove")) {
			refreshChildren();
			getViewer().select(this);
		} else {
			super.propertyChange(evt);
		}
	}
}
