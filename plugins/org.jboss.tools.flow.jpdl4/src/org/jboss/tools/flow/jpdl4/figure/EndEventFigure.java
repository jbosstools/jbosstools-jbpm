package org.jboss.tools.flow.jpdl4.figure;

import org.jboss.tools.flow.common.figure.EllipseElementFigure;

public class EndEventFigure extends EllipseElementFigure {
	
    protected void customizeFigure() {
       super.customizeFigure();
       ellipse.setLineWidth(5);
    }
    
}
