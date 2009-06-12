package org.jboss.tools.flow.common.registry;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.requests.CreationFactory;
import org.jboss.tools.flow.common.Logger;
import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Container;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.common.strategy.AcceptsElementStrategy;
import org.jboss.tools.flow.common.strategy.AcceptsIncomingConnectionStrategy;
import org.jboss.tools.flow.common.strategy.AcceptsOutgoingConnectionStrategy;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultContainerWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultFlowWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultLabelWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultNodeWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class ElementRegistry {
	
	private static final String elementsExtensionPointId = "org.jboss.tools.flow.common.elements";
	private static HashMap<String, IConfigurationElement> elementMap = null;
	
	
	private static void initializeRegistry() {
		elementMap = new HashMap<String, IConfigurationElement>();
		IConfigurationElement[] configurationElements = 
			Platform.getExtensionRegistry().getConfigurationElementsFor(elementsExtensionPointId);
		for (IConfigurationElement configurationElement: configurationElements) {
			elementMap.put(configurationElement.getAttribute("id"), configurationElement);
		}
	}
	
	private static Wrapper createWrapper(IConfigurationElement configurationElement) 
			throws CoreException {
		IConfigurationElement[] children = configurationElement.getChildren();
		if (children.length > 1) return null;
		if (children.length == 0) {
			return createDefault(configurationElement);
		}
		String type = children[0].getName();
		if ("flow".equals(type)) {
			return createFlow(configurationElement); 
		} else if ("container".equals(type)) {
			return createContainer(configurationElement);
		} else if ("node".equals(type)) {
			return createNode(configurationElement);
		} else if ("connection".equals(type)) {
			return createConnection(configurationElement);
		} else {
			return null;
		}
	}
	
	private static Wrapper createDefault(IConfigurationElement configurationElement) 
			throws CoreException {
		Object element = configurationElement.createExecutableExtension("class");
		if (!(element instanceof Element)) {
			String message = "Expecting to instantiate a org.jboss.tools.flow.common.model.Element instance.";
			Logger.logError(message, new RuntimeException(message));
			return null;
		}
		((Element)element).setMetaData("configurationElement", configurationElement);
		DefaultWrapper result = new DefaultWrapper();
		result.setElement((Element)element);
		return result;
	}
	
	private static Wrapper createConnection(IConfigurationElement configurationElement)
			throws CoreException {
		Object element = configurationElement.createExecutableExtension("class");
		if (!(element instanceof Connection)) {
			String message = "Expecting to instantiate a org.jboss.tools.flow.common.model.Connection instance.";
			Logger.logError(message, new RuntimeException(message));
			return null;
		}
		((Element)element).setMetaData("configurationElement", configurationElement);
		ConnectionWrapper result = new DefaultConnectionWrapper();
		result.setElement((Element)element);
		result.setLabel(new DefaultLabelWrapper(result));
		return result;
	}
	
	private static Wrapper createNode(IConfigurationElement configurationElement)
			throws CoreException {
		Object element = configurationElement.createExecutableExtension("class");
		if (!(element instanceof Node)) {
			String message = "Expecting to instantiate a org.jboss.tools.flow.common.model.Node instance.";
			Logger.logError(message, new RuntimeException(message));
			return null;
		}
		((Node)element).setMetaData("configurationElement", configurationElement);
		DefaultNodeWrapper result = new DefaultNodeWrapper();
		result.setElement((Element)element);
		AcceptsIncomingConnectionStrategy acceptsIncomingConnectionStrategy = createAcceptsIncomingConnectionStrategy(configurationElement);
		if (acceptsIncomingConnectionStrategy != null) {
			acceptsIncomingConnectionStrategy.setNode((Node)element);
			result.setAcceptsIncomingConnectionStrategy(acceptsIncomingConnectionStrategy);
		}
		AcceptsOutgoingConnectionStrategy acceptsOutgoingConnectionStrategy = createAcceptsOutgoingConnectionStrategy(configurationElement);
		if (acceptsOutgoingConnectionStrategy != null) {
			acceptsOutgoingConnectionStrategy.setNode((Node)element);
			result.setAcceptsOutgoingConnectionStrategy(acceptsOutgoingConnectionStrategy);
		}
		((Node)element).setName(configurationElement.getAttribute("name"));
		return result;
	}
	
	private static Wrapper createContainer(IConfigurationElement configurationElement) 
			throws CoreException {
		Object element = configurationElement.createExecutableExtension("class");
		if (!(element instanceof Container)) {
			String message = "Expecting to instantiate a org.jboss.tools.flow.common.model.Container instance.";
			Logger.logError(message, new RuntimeException(message));
			return null;
		}
		((Element) element).setMetaData("configurationElement", configurationElement);
		DefaultContainerWrapper result = new DefaultContainerWrapper();
		result.setElement((Element)element);
		AcceptsElementStrategy acceptsElementStrategy = createAcceptsElementStrategy(configurationElement);
		if (acceptsElementStrategy != null) {
			acceptsElementStrategy.setContainer((Container)element);
			result.setAcceptsElementStrategy(acceptsElementStrategy);
		}
		AcceptsIncomingConnectionStrategy acceptsIncomingConnectionStrategy = createAcceptsIncomingConnectionStrategy(configurationElement);
		if (acceptsIncomingConnectionStrategy != null) {
			acceptsIncomingConnectionStrategy.setNode((Node)element);
			result.setAcceptsIncomingConnectionStrategy(acceptsIncomingConnectionStrategy);
		}
		AcceptsOutgoingConnectionStrategy acceptsOutgoingConnectionStrategy = createAcceptsOutgoingConnectionStrategy(configurationElement);
		if (acceptsOutgoingConnectionStrategy != null) {
			acceptsOutgoingConnectionStrategy.setNode((Node)element);
			result.setAcceptsOutgoingConnectionStrategy(acceptsOutgoingConnectionStrategy);
		}
		if (element instanceof Node) {
			((Node)element).setName(configurationElement.getAttribute("name"));
		}
		return result;
	}
	
	private static AcceptsIncomingConnectionStrategy createAcceptsIncomingConnectionStrategy(IConfigurationElement configurationElement) 
			throws CoreException {
		Object result = null;
		IConfigurationElement[] children = configurationElement.getChildren();
		if (children[0].getAttribute("acceptsIncomingConnectionStrategy") != null) {
			result = children[0].createExecutableExtension("acceptsIncomingConnectionStrategy");
		}
		return (AcceptsIncomingConnectionStrategy)result;
	}
	
	private static AcceptsOutgoingConnectionStrategy createAcceptsOutgoingConnectionStrategy(IConfigurationElement configurationElement) 
			throws CoreException {
		Object result = null;
		IConfigurationElement[] children = configurationElement.getChildren();
		if (children[0].getAttribute("acceptsOutgoingConnectionStrategy") != null) {
			result = children[0].createExecutableExtension("acceptsOutgoingConnectionStrategy");
		}
		return (AcceptsOutgoingConnectionStrategy)result;
	}
	
	private static AcceptsElementStrategy createAcceptsElementStrategy(IConfigurationElement configurationElement) 
			throws CoreException {
		Object result = null;
		IConfigurationElement[] children = configurationElement.getChildren();
		if (children[0].getAttribute("acceptsElementStrategy") != null) {
			result = children[0].createExecutableExtension("acceptsElementStrategy");
		}
		return (AcceptsElementStrategy)result;
	}
	
	private static Wrapper createFlow(IConfigurationElement configurationElement) 
			throws CoreException {
		Object element = configurationElement.createExecutableExtension("class");
		if (!(element instanceof Container)) {
			String message = "Expecting to instantiate a org.jboss.tools.flow.common.model.Container instance.";
			Logger.logError(message, new RuntimeException(message));
			return null;
		}
		((Element) element).setMetaData("configurationElement", configurationElement);
		DefaultFlowWrapper result = new DefaultFlowWrapper();		
		result.setElement((Element)element);
		AcceptsElementStrategy acceptsElementStrategy = createAcceptsElementStrategy(configurationElement);
		if (acceptsElementStrategy != null) {
			acceptsElementStrategy.setContainer((Container)result.getElement());
			result.setAcceptsElementStrategy(acceptsElementStrategy);
		}
		if (element instanceof Flow) {
			((Flow)element).setName(configurationElement.getAttribute("name"));
		}
		return result;
	}
	
//	private static EditPart createConnectionEditPart(final IConfigurationElement configurationElement) {
//		return new ConnectionEditPart() {
//		};
//	}
//	
//	private static EditPart createNodeEditPart(final IConfigurationElement configurationElement) {
//		return new ElementEditPart() {
//			protected IFigure createFigure() {
//				ElementFigure result = null;
//				IConfigurationElement figureElement = null;
//				IConfigurationElement[] children = configurationElement.getChildren("node");
//				if (children.length == 1) {
//					children = children[0].getChildren("figure");
//					if (children.length == 1) {
//						figureElement = children[0];
//					}
//				}
//				if (figureElement == null) {
//					return new RectangleElementFigure();
//				}
//				if (figureElement.getAttribute("class") != null) {
//					try {
//						return (IFigure)figureElement.createExecutableExtension("class");
//					}
//					catch (CoreException e) {
//						return null;
//					}
//				}
//				children = figureElement.getChildren();
//				if (children.length < 1) {
//					result = new RectangleElementFigure();
//				} else if ("ellipse".equals(children[0].getName())){
//					result = new EllipseElementFigure();
//				} else if ("rounded-rectangle".equals(children[0].getName())) {
//					result = new RoundedRectangleElementFigure();
//				} else {
//					result = new RectangleElementFigure();
//				}
//				if (figureElement.getAttribute("icon") != null) {
//					String iconPath = figureElement.getAttribute("icon");
//					URL url = Platform.getBundle(figureElement.getContributor().getName()).getEntry(iconPath);
//					Image icon = null;
//					if (imageMap.containsKey(url.getPath())) {
//						icon = imageMap.get(url.getPath());
//					} else {
//						icon = ImageDescriptor.createFromURL(url).createImage();
//						imageMap.put(url.getPath(), icon);
//					}
//					result.setIcon(icon);
//				} else if (configurationElement.getAttribute("figure") != null) {
//					String iconPath = configurationElement.getAttribute("figure");
//					URL url = Platform.getBundle(configurationElement.getContributor().getName()).getEntry(iconPath);
//					Image icon = null;
//					if (imageMap.containsKey(url.getPath())) {
//						icon = imageMap.get(url.getPath());
//					} else {
//						icon = ImageDescriptor.createFromURL(url).createImage();
//						imageMap.put(url.getPath(), icon);
//					}
//					result.setIcon(icon);
//				}
//				if (figureElement.getAttribute("color") != null) {
//					String colorString = figureElement.getAttribute("color");
//					Color color = null;
//					if (colorMap.containsKey(colorString)) {
//						color = colorMap.get(colorString);
//					} else {
//						try {
//							StringTokenizer tokenizer = new StringTokenizer(colorString, ",");
//							int[] rgb = new int[3];
//							int i = 0;
//							while (tokenizer.hasMoreTokens()) {
//								rgb[i++] = Integer.parseInt(tokenizer.nextToken());
//							}
//							color = new Color(Display.getCurrent(), rgb[0], rgb[1], rgb[2]);
//							colorMap.put(colorString, color);
//						} catch (NumberFormatException e) {}
//					}
//					result.setColor(color);
//				}
//				Dimension size = new Dimension(result.getSize());
//				if (figureElement.getAttribute("width") != null) {
//					try {
//						size.width = Integer.parseInt(figureElement.getAttribute("width"));
//					} catch (NumberFormatException e) {}
//				}
//				if (figureElement.getAttribute("height") != null) {
//					try {
//						size.height = Integer.parseInt(figureElement.getAttribute("height"));
//					} catch (NumberFormatException e) {}
//				}
//				result.setSize(size);
//				return result;
//			}
//		};
//	}
	
	public static Wrapper createWrapper(String elementId) {
		if (elementMap == null) {
			initializeRegistry();
		}
		IConfigurationElement configurationElement = elementMap.get(elementId);
		if (configurationElement != null) {
			try {
				return createWrapper(configurationElement);
			} catch (CoreException e) {
				Logger.logError("Creating a wrapper for " + elementId + " failed.", e);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static IConfigurationElement getConfigurationElement(String elementId) {
		return elementMap.get(elementId);
	}
	
	public static CreationFactory getCreationFactory(final String elementId) {
		return new CreationFactory() {
			public Object getNewObject() {
				return createWrapper(elementId);
			}
			public Object getObjectType() {
				return elementId;
			}			
		};
	}
	
	public static CreationFactory getCreationFactory(Element element) {
		String id = null;
		IConfigurationElement configurationElement = 
			(IConfigurationElement)element.getMetaData("configurationElement");
		if (configurationElement != null) {
			id = configurationElement.getAttribute("id");
		}
		return id == null ? null : getCreationFactory(id);
	}

}
