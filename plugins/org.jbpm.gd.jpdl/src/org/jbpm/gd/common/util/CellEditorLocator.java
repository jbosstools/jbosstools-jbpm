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
package org.jbpm.gd.common.util;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;


public class CellEditorLocator implements org.eclipse.gef.tools.CellEditorLocator {
	
	private Label label;
	
	public CellEditorLocator(Label label) {
		this.label = label;
	}
	
	private Viewport getViewPort() {
		Viewport result = null;
		IFigure parent = label.getParent();
		while (parent != null && !(parent instanceof Viewport)) {
			parent = parent.getParent();
		}
		if (parent != null) {
			result = (Viewport)parent;
		}
		return result;
	}
	
	public Point getViewportOrigin() {
		Point result = new Point(0, 0);
		Viewport viewport = getViewPort();
		if (viewport != null) {
			result = new Point(viewport.getLocation());
		}
		return result;
	}

	public void relocate(CellEditor celleditor) {
		Text text = (Text) celleditor.getControl();
		Point origin = getViewportOrigin();
		Rectangle rect = label.getTextBounds().getCopy().expand(5, 0).translate(origin);
		text.setBounds(rect.x, rect.y, rect.width, rect.height);
	}

}
