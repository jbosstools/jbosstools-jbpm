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

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jbpm.gd.jpdl.Constants;

public class TaskListFigureBorder extends AbstractBorder {
	
	private static final Insets INSETS = new Insets(2,1,2,1);
	
	public Insets getInsets(IFigure figure) {
		return INSETS;
	}
	
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		graphics.setForegroundColor(ColorConstants.lightGray);
		Rectangle rect = getPaintRectangle(figure, insets);
		graphics.drawLine(rect.x, rect.y, rect.x + rect.width - 1, rect.y);
		graphics.setForegroundColor(Constants.veryLightGray);
		graphics.drawLine(rect.x, rect.y + 1, rect.x + rect.width - 1, rect.y + 1);
	}
	
}