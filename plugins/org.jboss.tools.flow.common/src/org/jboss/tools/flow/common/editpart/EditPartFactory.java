package org.jboss.tools.flow.common.editpart;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gef.EditPart;
import org.jboss.tools.flow.common.figure.IFigureFactory;
import org.jboss.tools.flow.common.figure.NodeFigureFactory;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.LabelWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class EditPartFactory implements org.eclipse.gef.EditPartFactory {

    public EditPart createEditPart(EditPart context, Object model) {
        EditPart result = null;
        if (!(model instanceof Wrapper)) return result;
        result = createEditPart((Wrapper)model);
        if (result != null) {
        	result.setModel(model);
        }
        return result;
    }
    
    protected EditPart createEditPart(Wrapper wrapper) {
    	EditPart result = null;
    	if (wrapper instanceof FlowWrapper) {
    		result = createFlowEditPart((FlowWrapper)wrapper);
    	} else if (wrapper instanceof ContainerWrapper) {
    		result = createContainerEditPart((ContainerWrapper)wrapper);
    	} else if (wrapper instanceof NodeWrapper) {
    		result = createNodeEditPart((NodeWrapper)wrapper);
    	} else if (wrapper instanceof ConnectionWrapper) {
    		result = createConnectionEditPart((ConnectionWrapper)wrapper);
    	} else if (wrapper instanceof LabelWrapper) {
    		result = createLabelEditPart((LabelWrapper)wrapper);
    	}
    	return result;
    }
    
    protected EditPart createFlowEditPart(FlowWrapper wrapper) {
    	return createFlowEditPart(wrapper.getElement());
    }
    
    protected EditPart createFlowEditPart(Element element) {
    	return new RootEditPart();
    }
    
    protected EditPart createContainerEditPart(ContainerWrapper wrapper) {
    	Element element = wrapper.getElement();
    	if (element == null) {
    		return null;
    	} else {
    		return createContainerEditPart(element);
    	}
    }
    
    protected EditPart createContainerEditPart(Element element) {
    	return new ContainerEditPart();
    }
    
    protected EditPart createNodeEditPart(NodeWrapper wrapper) {
    	Element element = wrapper.getElement();
    	if (element == null) {
    		return null;
    	} else {
    		return createNodeEditPart(element);
    	}
    }
    
    protected EditPart createNodeEditPart(Element element) {
		IConfigurationElement configurationElement = 
			(IConfigurationElement)element.getMetaData("configurationElement");
		if (configurationElement == null) return null;
    	NodeEditPart result = new NodeEditPart();
    	IFigureFactory figureFactory = new NodeFigureFactory(configurationElement);
    	result.setFigureFactory(figureFactory);
    	return result;
    }
    
    protected EditPart createConnectionEditPart(ConnectionWrapper wrapper) {
    	Element element = wrapper.getElement();
    	if (element == null) {
    		return null;
    	} else {
    		return createConnectionEditPart(element);
    	}
    }
    
    protected EditPart createConnectionEditPart(Element element) {
    	return new ConnectionEditPart();
    }
    
    protected EditPart createLabelEditPart(LabelWrapper wrapper) {
    	return new LabelEditPart();
    }
    
	protected EditPart createEditPart(Element element) {
		IConfigurationElement configurationElement = 
			(IConfigurationElement)element.getMetaData("configurationElement");
		if (configurationElement == null) return null;
		IConfigurationElement[] children = configurationElement.getChildren();
		if (children.length != 1) return null;
		String type = children[0].getName();
		if ("flow".equals(type)) {
			return createFlowEditPart(element);
		} else if ("container".equals(type)) {
			return createContainerEditPart(element);
		} else if ("node".equals(type)) {
			return createNodeEditPart(element);
		} else if ("connection".equals(type)) {
			return createConnectionEditPart(element);
		} else {
			return null;
		}
	}
	
}
