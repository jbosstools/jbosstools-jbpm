package org.jboss.tools.flow.common.editpart;

import org.eclipse.draw2d.IFigure;
import org.jboss.tools.flow.common.figure.IFigureFactory;

public class NodeEditPart extends ElementEditPart {

	private IFigureFactory figureFactory;
	
	protected IFigure createFigure() {
		if (figureFactory != null) {
			return figureFactory.createFigure();
		}
		return null;
	}
	
	public void setFigureFactory(IFigureFactory figureFactory) {
		this.figureFactory = figureFactory;
	}

}
