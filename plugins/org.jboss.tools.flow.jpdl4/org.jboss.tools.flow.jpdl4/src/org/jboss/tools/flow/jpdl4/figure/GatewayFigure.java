package org.jboss.tools.flow.jpdl4.figure;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.figure.AbstractElementFigure;
import org.jboss.tools.flow.common.figure.DiamondAnchor;

public abstract class GatewayFigure extends AbstractElementFigure { //DiamondElementFigure {
	
	public GatewayFigure() {
		setSize(48, 48);
	}

	public void setText(String text) {
	}

	public void setBounds(Rectangle r) {
		r.setSize(48, 48);
		super.setBounds(r);
	}

    public ConnectionAnchor getSourceConnectionAnchor() {
        return new DiamondAnchor(this);
    }

    public ConnectionAnchor getTargetConnectionAnchor() {
        return new DiamondAnchor(this);
    }

}
