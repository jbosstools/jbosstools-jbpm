package org.jboss.tools.flow.jpdl4.editor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
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
			result.getElement().setMetaData("flows", new ArrayList<ConnectionWrapper>());
			addName(result, element);
			addNodes(result, element);
			resolveSequenceFlowTargets(result);
			result.getElement().setMetaData("flows", null);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private static void resolveSequenceFlowTargets(FlowWrapper flowWrapper) {
		ArrayList<ConnectionWrapper> flows = (ArrayList<ConnectionWrapper>)flowWrapper.getElement().getMetaData("flows");
		for (ConnectionWrapper flow : flows) {
			resolveSequenceFlowTarget(flow);
		}		
	}
	
	private static void resolveSequenceFlowTarget(ConnectionWrapper connectionWrapper) {
		String to = (String)connectionWrapper.getElement().getMetaData("to");
		if (to == null) {
			Logger.logInfo("Ignoring sequenceflow without target");	
			return;
		}
		connectionWrapper.getElement().setMetaData("to", null);
		NodeWrapper source = (NodeWrapper)connectionWrapper.getElement().getMetaData("from");
		if (source == null) {
			Logger.logInfo("Ignoring sequenceflow without source");
			return;
		}
		connectionWrapper.getElement().setMetaData("from", null);
		FlowWrapper flowWrapper = source.getParent().getFlowWrapper();
		NodeWrapper target = getNamedNode(to, flowWrapper);
		if (target != null) {
			connectionWrapper.connect(source, target);
		} else {
			Logger.logInfo("Ignoring unknown target " + to + " while resolving sequenceflow target.");
		}
	}
	
	private static NodeWrapper getNamedNode(String name, FlowWrapper flowWrapper) {
		if (name == null) return null;
		for (NodeWrapper nodeWrapper : flowWrapper.getElements()) {
			if (name.equals(nodeWrapper.getName())) return nodeWrapper;
		}
		return null;
	}
	
	private static void addName(Wrapper wrapper,  Element element) {
		String name = element.getAttribute("name");
		if (name == null) return;
		if (wrapper instanceof FlowWrapper) {
			((Flow)((FlowWrapper)wrapper).getElement()).setName(name);
		} else if (wrapper instanceof NodeWrapper){
			((NodeWrapper)wrapper).setName(name);
		}
	}
	
	private static void addNodes(FlowWrapper wrapper, Element element) {
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			NodeWrapper result = null;
			if ("start".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.startEvent");
			} else if ("state".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.stateTask");
			} else if ("end".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.endEvent");
			} else if ("exclusive".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.exclusiveGateway");
			} else if ("join".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.parallelJoinGateway");
			} else if ("fork".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.parallelForkGateway");
			}
			if (result != null) {
				wrapper.addElement(result);
				addName(result, (Element)child);
				addGraphics(result, (Element)child);
				addSequenceFlow(result, (Element)child);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void addSequenceFlow(NodeWrapper wrapper, Element element) {
		NodeList nodeList = element.getElementsByTagName("flow");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			ConnectionWrapper result = 
				createConnectionWrapper((Element)child, "org.jboss.tools.flow.jpdl4.sequenceFlow");
			if (result != null) {
				result.getElement().setMetaData("from", wrapper);
				String to = ((Element)child).getAttribute("to");
				result.getElement().setMetaData("to", to);
				ContainerWrapper parent = wrapper.getParent();
				FlowWrapper flowWrapper = parent.getFlowWrapper();
				ArrayList<ConnectionWrapper> flows = 
					(ArrayList<ConnectionWrapper>)flowWrapper.getElement().getMetaData("flows");
				flows.add(result);
			}
		}
	}
	
	
	private static ConnectionWrapper createConnectionWrapper(Element element, String elementType) {
		ConnectionWrapper result = (ConnectionWrapper)ElementRegistry.createWrapper(elementType);
		if (result != null) {
			addName(result, element);
			addGraphics(result, element);
		}
		return result;
	}
	
	private static void addGraphics(ConnectionWrapper wrapper, Element element) {
		String graphics = element.getAttribute("g");
		if (graphics != null) {
			StringTokenizer bendpoints = new StringTokenizer(graphics, ";");
			int index = 0;
			while (bendpoints.hasMoreTokens()) {
				StringTokenizer bendpoint = new StringTokenizer(bendpoints.nextToken(), ",");
				if (bendpoint.countTokens() != 2) {
					Logger.logInfo(
							"Wrong info in attribute 'g' while determining bendpoints.");
				} else {
					int x = convertStringToInt(bendpoint.nextToken());
					int y = convertStringToInt(bendpoint.nextToken());
					wrapper.addBendpoint(index++, new Point(x, y));
				}
	
			}
		}
		
	}
	
	private static void addGraphics(NodeWrapper wrapper, Element element) {
		String graphics = element.getAttribute("g");
		Rectangle constraint = new Rectangle(0, 0, 80, 40);
		if (graphics != null) {
			StringTokenizer tokenizer = new StringTokenizer(graphics, ",");
			if (tokenizer.countTokens() != 4) {
				Logger.logInfo(
						"Wrong info in attribute 'g' for element '" + 
						element.getNodeName() + "'" +
						" with name '" +
						element.getAttribute("name") +
						"'. Using defaults." );
			} else {
				constraint.x = convertStringToInt(tokenizer.nextToken());
				constraint.y = convertStringToInt(tokenizer.nextToken());
				constraint.width = convertStringToInt(tokenizer.nextToken());
				constraint.height = convertStringToInt(tokenizer.nextToken());
			}
		}
		wrapper.setConstraint(constraint);
	}
	
	private static int convertStringToInt(String str) {
		int result = 0;
		try {
			result = new Integer(str).intValue();
		} catch (NumberFormatException e) {
			Logger.logError("Error while converting " + str + " to an integer.", e);
		}
		return result;
	}
	
}
