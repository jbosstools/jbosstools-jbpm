package org.jboss.tools.flow.jpdl4.editpart;

import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.jpdl4.util.SharedImages;

public class ArgumentListTreeEditPart extends JpdlTreeEditPart {
	
	public ArgumentListTreeEditPart(List<Element> arguments) {
		super(arguments);
	}
	
	protected Image getImage() {
		String iconPath = "icons/16/argument_multiple.gif";
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(
				Platform.getBundle("org.jboss.tools.flow.jpdl4").getEntry(iconPath));
		return SharedImages.INSTANCE.getImage(descriptor);
	}
	
	protected String getText() {
		return "Arguments";
	}
	
	@SuppressWarnings("unchecked")
	protected List<Object> getModelChildren() {
		return (List<Object>)getModel();
	}
	

}
