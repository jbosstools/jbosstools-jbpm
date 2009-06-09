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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.jbpm.gd.common.util.SharedImages;
import org.jbpm.gd.jpdl.Constants;
import org.jbpm.gd.jpdl.Plugin;

public class NodeHeaderFigure extends Figure {

	private static final Font NAMEFONT = new Font(null, "Arial", 9, SWT.BOLD);

	private static final Font TYPEFONT = new Font(null, "Arial", 9, SWT.ITALIC);

	private Figure embeddedFigure;

	private Figure typeAndLabelColumn;

	private Label typeLabel;

	private Label nameLabel;

	private Label iconLabel;

	public NodeHeaderFigure(String nodeType, String iconName) {
		this(nodeType, iconName, false);
	}

	public NodeHeaderFigure(String nodeType, String iconName, boolean hideName) {
		this("", nodeType, iconName, hideName);
	}

	public NodeHeaderFigure(String nodeType, String iconName, boolean hideName, ImageDescriptor imageDescriptor) {
		this("", nodeType, iconName, hideName, imageDescriptor);
	}

	public NodeHeaderFigure(String nodeName, String nodeType, String iconName) {
		this(nodeName, nodeType, iconName, false);
	}

	public NodeHeaderFigure(String nodeName, String nodeType, String iconName,
			boolean hideName) {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
		setLayoutManager(flowLayout);
		addEmbeddedParent(nodeName, nodeType, getStdIconDescriptor(iconName),
				hideName);
	}

	public NodeHeaderFigure(String nodeName, String nodeType, String iconName,
			boolean hideName, ImageDescriptor imageDescriptor) {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
		setLayoutManager(flowLayout);
		addEmbeddedParent(nodeName, nodeType, imageDescriptor,
				hideName);
	}

	public NodeHeaderFigure(String nodeType, ImageDescriptor iconDescriptor,
			boolean hideName) {
		this("", nodeType, iconDescriptor, hideName);
	}

	public NodeHeaderFigure(String nodeName, String nodeType,
			ImageDescriptor iconDescriptor, boolean hideName) {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
		setLayoutManager(flowLayout);
		addEmbeddedParent(nodeName, nodeType, iconDescriptor, hideName);
	}

	private void addEmbeddedParent(String nodeName, String nodeType,
			ImageDescriptor iconDescriptor, boolean hideName) {
		embeddedFigure = new Figure();
		ToolbarLayout layout = new ToolbarLayout(true);
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		embeddedFigure.setLayoutManager(layout);
		addIconLabel(iconDescriptor);
		addTypeAndNameColumn(nodeType, nodeName, hideName);
		add(embeddedFigure);
	}

	private ImageDescriptor getStdIconDescriptor(String nodeType) {
		return ImageDescriptor.createFromURL(Plugin.getDefault()
				.getBundle().getEntry("/icons/full/obj16/" + nodeType.toLowerCase().replace(' ', '_') + ".gif"));
	}

	private void addTypeAndNameColumn(String nodeType, String nodeName,
			boolean hideName) {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setStretchMinorAxis(false);
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		typeAndLabelColumn = new Figure();
		typeAndLabelColumn.setLayoutManager(layout);
		addTypeLabel(nodeType);
		if (!hideName) {
			addNameLabel(nodeName);
		}
		embeddedFigure.add(typeAndLabelColumn);
	}

	private void addNameLabel(String nodeName) {
		nameLabel = new Label();
		typeLabel.setBorder(new MarginBorder(2));
		nameLabel.setForegroundColor(ColorConstants.darkGray);
		nameLabel.setFont(NAMEFONT);
		nameLabel.setText(nodeName);
		typeAndLabelColumn.add(nameLabel);
	}

	private void addTypeLabel(String nodeType) {
		typeLabel = new Label();
		typeLabel.setBorder(new MarginBorder(2));
		typeLabel.setForegroundColor(ColorConstants.darkGray);
		typeLabel.setFont(TYPEFONT);
		typeLabel.setText("<<" + nodeType + ">>");
		typeAndLabelColumn.add(typeLabel);
	}

	private void addIconLabel(ImageDescriptor iconDescriptor) {
		iconLabel = new Label();
		iconLabel.setBorder(new MarginBorder(2));
		iconLabel.setIcon(getNodeIcon(iconDescriptor));
		embeddedFigure.add(iconLabel);
	}

	private Image getNodeIcon(ImageDescriptor iconDescriptor) {
		return SharedImages.INSTANCE.getImage(iconDescriptor);
	}

	protected void paintClientArea(Graphics graphics) {
		Color foreground = graphics.getForegroundColor();
		graphics.setForegroundColor(Constants.veryLightGray);
		graphics.fillGradient(getClientArea(), true);
		graphics.setForegroundColor(foreground);
		super.paintClientArea(graphics);
	}

	public void setNodeName(String name) {
		if (name != null && nameLabel != null) {
			nameLabel.setText(name);
		}
	}

	public Label getNameLabel() {
		return nameLabel;
	}
}
