package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.InputParameter;
import org.jboss.tools.flow.jpdl4.model.Parameter;
import org.jboss.tools.flow.jpdl4.util.SharedImages;

public class ParameterTreeEditPart extends JpdlTreeEditPart implements ElementTreeEditPart {
	
	public ParameterTreeEditPart(Wrapper wrapper) {
		super(wrapper);
	}
	
	protected Image getImage() {
		Element element = ((Wrapper)getModel()).getElement();
		String iconPath = element instanceof InputParameter ? "icons/16/input.gif" : "icons/16/output.gif";
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(
				Platform.getBundle("org.jboss.tools.flow.jpdl4").getEntry(iconPath));
		return SharedImages.INSTANCE.getImage(descriptor);
	}
	
	protected String getText() {
		Element element = ((Wrapper)getModel()).getElement();
		String property = element instanceof InputParameter ? Parameter.VAR : Parameter.SUBVAR;
		String variable = (String)((Wrapper)getModel()).getPropertyValue(property);
		return variable == null || "".equals(variable) ? "variable" : variable;
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
