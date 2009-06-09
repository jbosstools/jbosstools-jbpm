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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class SharedImages {
	
	public static final SharedImages INSTANCE = new SharedImages();

	private final Map imageMap = new HashMap();
	
	public Image getImage(ImageDescriptor imageDescriptor) {
		if (imageDescriptor == null) {
			return null;
		}
		Image image = (Image)imageMap.get(imageDescriptor);
		if (image == null) {
			image = imageDescriptor.createImage();
			imageMap.put(imageDescriptor, image);
		}
		return image;
	}
	
	public void dispose() {
		Iterator iter = imageMap.values().iterator();
		while (iter.hasNext()) {
			((Image)iter.next()).dispose();
		}
		imageMap.clear();
	}
	
}
