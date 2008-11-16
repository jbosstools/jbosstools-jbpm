package org.jboss.tools.flow.jpdl4.editor;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JpdlDeserializer {
	
	private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	
	public static Wrapper deserialize(InputStream is) {
		Wrapper result = null;
		try {
			Document document = documentBuilderFactory.newDocumentBuilder().parse(is);
			Element element = document.getDocumentElement();
			result = createFlowWrapper(element);
		} catch (Exception e) {
			Logger.logError("An error occurred while creating the diagram", e);
		}
		return result;
	}
	
	private static Wrapper createFlowWrapper(Element element) {
		FlowWrapper result = null;
		if ("process".equals(element.getNodeName())) {
			result = createProcessWrapper(element);
		}
		return result;
	}
	
	private static FlowWrapper createProcessWrapper(Element element) {
		FlowWrapper result = (FlowWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.process");
		if (result != null) {
			addName(result, element);
			addChildren(result, element);
		}
		return result;
	}
	
	private static void addName(Wrapper wrapper, Element element) {
		String name = element.getAttribute("name");
		if (name == null) return;
		if (wrapper instanceof FlowWrapper) {
			((Flow)((FlowWrapper)wrapper).getElement()).setName(name);
		} else if (wrapper instanceof NodeWrapper){
			((NodeWrapper)wrapper).setName(name);
		}
	}
	
	private static void addChildren(FlowWrapper wrapper, Element element) {
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			NodeWrapper result = null;
			if ("start".equals(child.getNodeName())) {
				result = createElementWrapper((Element)child, "org.jboss.tools.flow.jpdl4.startEvent");
			} else if ("state".equals(child.getNodeName())) {
				result = createElementWrapper((Element)child, "org.jboss.tools.flow.jpdl4.stateTask");
			}
			if (result != null) {
				wrapper.addElement(result);
			}
		}
	}
	
	private static NodeWrapper createElementWrapper(Element element, String elementType) {
		NodeWrapper result = (NodeWrapper)ElementRegistry.createWrapper(elementType);
		if (result != null) {
			addName(result, element);
			addLocation(result, element);
		}
		return result;
	}
	
	private static void addLocation(NodeWrapper wrapper, Element element) {
		NodeList nodelist = element.getElementsByTagName("location");
		Rectangle constraint = new Rectangle(0, 0, 80, 40);
		if (nodelist.getLength() >= 1) {
			Element location = (Element)nodelist.item(0);
			constraint.x = convertAttributeToInt(location, "x");
			constraint.y = convertAttributeToInt(location, "y");
			constraint.height = convertAttributeToInt(location, "h");
			constraint.width = convertAttributeToInt(location, "w");
		}
		wrapper.setConstraint(constraint);
	}
	
	private static int convertAttributeToInt(Element element, String attributeName) {
		int result = 0;
		String str = element.getAttribute(attributeName);
		if (str != null) {
			try {
				result = new Integer(str).intValue();
			} catch (NumberFormatException e) {
				Logger.logError(attributeName + " is not correctly formatted.", e);
			}
		}
		return result;
	}

}
