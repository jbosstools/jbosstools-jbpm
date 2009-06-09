/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jbpm.gd.common.figure;

import java.util.ArrayList;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

public class EdgeFigure extends PolylineConnection {
	
	private static final Font FONT = new Font(null, "Arial", 9, SWT.NORMAL);
	private Label label;
	
	public EdgeFigure() {
		setTargetDecoration();
//		setConnectionRouter(new ManhattanConnectionRouter());
		setConnectionRouter(new BendpointConnectionRouter());
		setRoutingConstraint(new ArrayList());
		addLabel();
	}

	private void setTargetDecoration() {
		PolygonDecoration arrow = new PolygonDecoration();
		arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
		arrow.setBackgroundColor(ColorConstants.white);
		arrow.setForegroundColor(ColorConstants.lightGray);
		arrow.setOpaque(true);
		arrow.setScale(10, 5);
		setTargetDecoration(arrow);
	}

	private void addLabel() {
		ConnectionEndpointLocator relationshipLocator = new ConnectionEndpointLocator(this, false);
		relationshipLocator.setUDistance(10);
		relationshipLocator.setVDistance(-10);
		label = new Label();
		label.setForegroundColor(ColorConstants.darkGray);
		label.setFont(FONT);
		add(label, relationshipLocator);
		label.setVisible(false);
	}
	
	public void paintFigure(Graphics g) {
		g.setForegroundColor(ColorConstants.lightGray);
		super.paintFigure(g);
	}
	
	public Label getLabel() {
		return label;
	}
	
}
