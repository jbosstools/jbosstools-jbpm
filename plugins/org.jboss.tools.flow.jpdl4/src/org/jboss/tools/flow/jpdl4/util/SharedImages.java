package org.jboss.tools.flow.jpdl4.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class SharedImages {
	
	public static final SharedImages INSTANCE = new SharedImages();
	
	private final Map<ImageDescriptor, Image> imageMap = new HashMap<ImageDescriptor, Image>();
	
	public Image getImage(ImageDescriptor imageDescriptor) {
		if (imageDescriptor == null) {
			return null;
		}
		Image image = imageMap.get(imageDescriptor);
		if (image == null) {
			image = imageDescriptor.createImage();
			imageMap.put(imageDescriptor, image);
		}
		return image;
	}
	
	public void dispose() {
		Iterator<Image> iterator = imageMap.values().iterator();
		while (iterator.hasNext()) {
			iterator.next().dispose();
		}
		imageMap.clear();
	}

}
