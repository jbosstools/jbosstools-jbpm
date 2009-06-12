package org.jboss.tools.flow.common.figure;

import java.net.URL;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class NodeFigureFactory implements IFigureFactory {
	
	private static final HashMap<String, Image> imageMap = new HashMap<String, Image>();
	private static final HashMap<String, Color> colorMap = new HashMap<String, Color>();
	
	private IConfigurationElement configurationElement;
	
	public NodeFigureFactory(IConfigurationElement configurationElement) {
		this.configurationElement = configurationElement;
	}
	
	public IFigure createFigure() {
		ElementFigure result = null;
		IConfigurationElement figureElement = null;
		IConfigurationElement[] children = configurationElement.getChildren("node");
		if (children.length == 1) {
			children = children[0].getChildren("figure");
			if (children.length == 1) {
				figureElement = children[0];
			}
		}
		if (figureElement == null) {
			return new RectangleElementFigure();
		}
		if (figureElement.getAttribute("class") != null) {
			try {
				return (IFigure)figureElement.createExecutableExtension("class");
			}
			catch (CoreException e) {
				return null;
			}
		}
		children = figureElement.getChildren();
		if (children.length < 1) {
			result = new RectangleElementFigure();
		} else if ("ellipse".equals(children[0].getName())){
			result = new EllipseElementFigure();
		} else if ("rounded-rectangle".equals(children[0].getName())) {
			result = new RoundedRectangleElementFigure();
		} else {
			result = new RectangleElementFigure();
		}
		if (figureElement.getAttribute("icon") != null) {
			String iconPath = figureElement.getAttribute("icon");
			URL url = Platform.getBundle(figureElement.getContributor().getName()).getEntry(iconPath);
			Image icon = null;
			if (imageMap.containsKey(url.getPath())) {
				icon = imageMap.get(url.getPath());
			} else {
				icon = ImageDescriptor.createFromURL(url).createImage();
				imageMap.put(url.getPath(), icon);
			}
			result.setIcon(icon);
		} else if (configurationElement.getAttribute("figure") != null) {
			String iconPath = configurationElement.getAttribute("figure");
			URL url = Platform.getBundle(configurationElement.getContributor().getName()).getEntry(iconPath);
			Image icon = null;
			if (imageMap.containsKey(url.getPath())) {
				icon = imageMap.get(url.getPath());
			} else {
				icon = ImageDescriptor.createFromURL(url).createImage();
				imageMap.put(url.getPath(), icon);
			}
			result.setIcon(icon);
		}
		if (figureElement.getAttribute("color") != null) {
			String colorString = figureElement.getAttribute("color");
			Color color = null;
			if (colorMap.containsKey(colorString)) {
				color = colorMap.get(colorString);
			} else {
				try {
					StringTokenizer tokenizer = new StringTokenizer(colorString, ",");
					int[] rgb = new int[3];
					int i = 0;
					while (tokenizer.hasMoreTokens()) {
						rgb[i++] = Integer.parseInt(tokenizer.nextToken());
					}
					color = new Color(Display.getCurrent(), rgb[0], rgb[1], rgb[2]);
					colorMap.put(colorString, color);
				} catch (NumberFormatException e) {}
			}
			result.setColor(color);
		}
		Dimension size = new Dimension(result.getSize());
		if (figureElement.getAttribute("width") != null) {
			try {
				size.width = Integer.parseInt(figureElement.getAttribute("width"));
			} catch (NumberFormatException e) {}
		}
		if (figureElement.getAttribute("height") != null) {
			try {
				size.height = Integer.parseInt(figureElement.getAttribute("height"));
			} catch (NumberFormatException e) {}
		}
		result.setSize(size);
		return result;
	}
		
	
}
