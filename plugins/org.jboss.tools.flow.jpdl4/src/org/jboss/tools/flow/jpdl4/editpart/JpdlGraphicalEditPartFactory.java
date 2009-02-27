package org.jboss.tools.flow.jpdl4.editpart;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gef.EditPart;
import org.jboss.tools.flow.common.editpart.EditPartFactory;
import org.jboss.tools.flow.common.figure.IFigureFactory;
import org.jboss.tools.flow.common.figure.NodeFigureFactory;
import org.jboss.tools.flow.common.model.Element;

public class JpdlGraphicalEditPartFactory extends EditPartFactory {

    protected EditPart createNodeEditPart(Element element) {
		IConfigurationElement configurationElement = 
			(IConfigurationElement)element.getMetaData("configurationElement");
		if (configurationElement == null) return null;
    	ProcessNodeGraphicalEditPart result = new ProcessNodeGraphicalEditPart();
    	IFigureFactory figureFactory = new NodeFigureFactory(configurationElement);
    	result.setFigureFactory(figureFactory);
    	return result;
    }
    
    protected EditPart createFlowEditPart(Element element) {
    	return new ProcessGraphicalEditPart();
    }
    
    protected EditPart createConnectionEditPart(Element element) {
    	return new SequenceFlowGraphicalEditPart();
    }
    
    
}
