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

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jbpm.gd.jpdl.Constants;

public class NodeFigure extends AbstractNodeFigure {

	private FixedAnchor selfReferencingAnchor;
	private NodeHeaderFigure header;
	private IFigure body;

	public NodeFigure(String nodeType) {
		this(nodeType, nodeType);
	}

	public NodeFigure(String nodeType, ImageDescriptor imageDescriptor) {
		this(nodeType, nodeType, imageDescriptor);
	}

	public NodeFigure(String nodeType, boolean hideName) {
		this(nodeType, nodeType, hideName);
	}

	public NodeFigure(String nodeType, boolean hideName, ImageDescriptor imageDescriptor) {
		this(nodeType, nodeType, hideName, imageDescriptor);
	}

	public NodeFigure(String nodeType, String iconName) {
		this(nodeType, iconName, false);
	}
	
	public NodeFigure(String nodeType, String iconName, ImageDescriptor imageDescriptor) {
		this(nodeType, iconName, false, imageDescriptor);
	}

	public NodeFigure(String nodeType, String iconName, boolean hideName) {
		header = new NodeHeaderFigure(nodeType, iconName, hideName);
		initialize();
	}

	public NodeFigure(String nodeType, String iconName, boolean hideName, ImageDescriptor imageDescriptor) {
		header = new NodeHeaderFigure(nodeType, iconName, hideName, imageDescriptor);
		initialize();
	}

	public NodeFigure(String nodeType, ImageDescriptor iconDescriptor,
			boolean hideName) {
		header = new NodeHeaderFigure(nodeType, iconDescriptor, hideName);
		initialize();
	}

	private void initialize() {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setSpacing(2);
		layout.setStretchMinorAxis(true);
		setLayoutManager(layout);
		setOpaque(true);
		add(header);
		connectionAnchor = new ChopboxAnchor(this);
		selfReferencingAnchor = new FixedAnchor(this);
		body = new Figure();
		body.setLayoutManager(new ToolbarLayout());
		add(body);
	}
	
	public IFigure getContentPane() {
		return body;
	}

	public void setName(String name) {
		header.setNodeName(name);
		getLayoutManager().layout(this);
	}

	public Label getNameLabel() {
		return header.getNameLabel();
	}

	public ConnectionAnchor getSelfReferencingAnchor() {
		return selfReferencingAnchor;
	}

	protected void paintBorder(Graphics graphics) {
		Rectangle bounds = getBounds().getCopy();
		Point origin = bounds.getLocation();
		int height = bounds.height;
		int width = bounds.width;
		graphics.translate(origin);
		graphics.setForegroundColor(ColorConstants.lightGray);
		graphics.drawLine(0, 0, width - 2, 0);
		graphics.drawLine(width - 2, 0, width - 2, height - 2);
		graphics.drawLine(width - 2, height - 2, 0, height - 2);
		graphics.drawLine(0, height - 2, 0, 0);
		graphics.setForegroundColor(Constants.veryLightGray);
		graphics.drawLine(width - 1, 1, width - 1, height - 1);
		graphics.drawLine(width - 1, height - 1, 1, height - 1);
	}

}
