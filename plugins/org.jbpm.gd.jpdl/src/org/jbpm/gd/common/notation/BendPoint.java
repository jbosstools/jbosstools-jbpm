package org.jbpm.gd.common.notation;

import org.eclipse.draw2d.geometry.Dimension;

public class BendPoint {

	private Dimension firstDimension;
	private Dimension secondDimension;
	
	public Dimension getFirstRelativeDimension() {
		return firstDimension;
	}
	
	public Dimension getSecondRelativeDimension() {
		return secondDimension;
	}
	
	public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
		firstDimension = dim1;
		secondDimension = dim2;
	}

}
