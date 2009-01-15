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
	
	interface AttributeHandler {
		void processAttributes(Wrapper wrapper, Element element);
	}
	
	interface ChildNodeHandler {
		Wrapper processChildNode(Wrapper parent, Node node);
	}
	
	interface PostProcessor {
		void postProcess(Wrapper wrapper);
	}
	
	class ProcessAttributeHandler implements AttributeHandler {
		public void processAttributes(Wrapper wrapper, Element element) {
			if (!(wrapper instanceof FlowWrapper)) return;
			FlowWrapper flowWrapper = (FlowWrapper)wrapper;
			((Flow)flowWrapper.getElement()).setName(element.getAttribute("name"));
		}
	}
	
	class NodeAttributeHandler implements AttributeHandler {
		public void processAttributes(Wrapper wrapper, Element element) {
			if (!(wrapper instanceof NodeWrapper)) return;
			NodeWrapper nodeWrapper = (NodeWrapper)wrapper;
			addGraphics(nodeWrapper, element);
			nodeWrapper.setName(element.getAttribute("name"));			
		}
	}
	
	class ConnectionAttributeHandler implements AttributeHandler {
		public void processAttributes(Wrapper wrapper, Element element) {
			if (!(wrapper instanceof ConnectionWrapper)) return;
			ConnectionWrapper connectionWrapper = (ConnectionWrapper)wrapper;
			addGraphics(connectionWrapper, element);
			connectionWrapper.getElement().setMetaData("to", element.getAttribute("to"));
		}
	}
	
	class ProcessChildNodeHandler implements ChildNodeHandler {
		public Wrapper processChildNode(Wrapper parent, Node node) {
			if (!(parent instanceof FlowWrapper)) return null;
			FlowWrapper flowWrapper = (FlowWrapper)parent;
			if (node instanceof Element) {
				Wrapper childWrapper = createWrapper((Element)node);
				if (childWrapper != null && childWrapper instanceof NodeWrapper) {
					flowWrapper.addElement((NodeWrapper)childWrapper);
				}
			}
			return null;
		}
	}
	
	class NodeChildNodeHandler implements ChildNodeHandler {
		@SuppressWarnings("unchecked")
		public Wrapper processChildNode(Wrapper parent, Node node) {
			if (!(parent instanceof NodeWrapper)) return null;
			NodeWrapper nodeWrapper = (NodeWrapper)parent;
			ArrayList<ConnectionWrapper> flows = (ArrayList<ConnectionWrapper>)nodeWrapper.getElement().getMetaData("flows");
			if (flows == null) {
				flows = new ArrayList<ConnectionWrapper>();
				nodeWrapper.getElement().setMetaData("flows", flows);
			}
			if (node instanceof Element) {
				Wrapper childWrapper = createWrapper((Element)node);
				if (childWrapper != null && childWrapper instanceof ConnectionWrapper) {
					flows.add((ConnectionWrapper)childWrapper);
				}
			}
			return null;
		}
	}
	
	class ProcessPostProcessor implements PostProcessor {
		@SuppressWarnings("unchecked")
		public void postProcess(Wrapper wrapper) {
			if (!(wrapper instanceof FlowWrapper)) return;
			FlowWrapper flowWrapper = (FlowWrapper)wrapper;
			for (NodeWrapper source : flowWrapper.getElements()) {
				ArrayList<ConnectionWrapper> flows = (ArrayList<ConnectionWrapper>)source.getElement().getMetaData("flows");
				if (flows == null) continue;
				for (ConnectionWrapper connectionWrapper : flows) {
					String to = (String)connectionWrapper.getElement().getMetaData("to");
					if (to == null) {
						Logger.logInfo("Ignoring sequenceflow without target");	
						continue;
					}
					NodeWrapper target = getNamedNode(to, flowWrapper);
					if (target == null) {
						Logger.logInfo("Ignoring unknown target " + to + " while resolving sequenceflow target.");
						continue;
					}
					connectionWrapper.connect(source, target);
				}
			}
		}
	}
	
	private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	
	public Wrapper deserialize(InputStream is) {
		Wrapper result = null;
		try {
			Document document = documentBuilderFactory.newDocumentBuilder().parse(is);
			result = createWrapper(document.getDocumentElement());
//			Element element = document.getDocumentElement();
//			result = createFlowWrapper(element);
		} catch (Exception e) {
			Logger.logError("An error occurred while creating the diagram", e);
		}
		return result;
	}
	
	private Wrapper createWrapper(Element element) {
		String elementId = getElementId(element.getNodeName());
		if (elementId == null) return null;
		Wrapper result = ElementRegistry.createWrapper(elementId);
		if (result == null) return null;
		AttributeHandler attributeHandler = getAttributeHandler(result);
		if (attributeHandler != null) {
			attributeHandler.processAttributes(result, element);
		}
		ChildNodeHandler childNodeHandler = getChildNodeHandler(result);
		if (childNodeHandler != null) {
			NodeList nodeList = element.getChildNodes();
			ArrayList<Node> unknownNodeList = new ArrayList<Node>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Wrapper childWrapper = childNodeHandler.processChildNode(result, nodeList.item(i));		
				if (childWrapper != null) {
					childWrapper.getElement().setMetaData("leadingNodes", unknownNodeList);
					unknownNodeList = new ArrayList<Node>();
				} else {
					unknownNodeList.add(nodeList.item(i));
				}
			}
			result.getElement().setMetaData("trailingNodes", unknownNodeList);
		}
		PostProcessor postProcessor = getPostProcessor(result);
		if (postProcessor != null) {
			postProcessor.postProcess(result);
		}
		return result;
	}
	
	private PostProcessor getPostProcessor(Wrapper wrapper) {
		if (wrapper instanceof FlowWrapper) {
			return new ProcessPostProcessor();
		}
		return null;
	}
	
	private AttributeHandler getAttributeHandler(Wrapper wrapper) {
		if (wrapper instanceof FlowWrapper) {
			return new ProcessAttributeHandler();
		} else if (wrapper instanceof NodeWrapper) {
			return new NodeAttributeHandler();
		} else if (wrapper instanceof ConnectionWrapper) {
			return new ConnectionAttributeHandler();
		}
		return null;
	}
	
	private ChildNodeHandler getChildNodeHandler(Wrapper wrapper) {
		if (wrapper instanceof FlowWrapper) {
			return new ProcessChildNodeHandler();
		} else if (wrapper instanceof NodeWrapper) {
			return new NodeChildNodeHandler();
		}
		return null;
	}
	 
	private String getElementId(String nodeName) {
		if ("process".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.process";
		else if ("start".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.startEvent";
		else if ("end".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.terminateEndEvent";
		else if ("end-error".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.errorEndEvent";
		else if ("end-cancel".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.cancelEndEvent";
		else if ("state".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.waitTask";
		else if ("hql".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.hqlTask";
		else if ("sql".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.sqlTask";
		else if ("java".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.javaTask";
		else if ("script".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.scriptTask";
		else if ("esb".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.serviceTask";
		else if ("task".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.humanTask";
		else if ("exclusive".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.exclusiveGateway";
		else if ("join".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.parallelJoinGateway";
		else if ("fork".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.parallelForkGateway";
		else if ("flow".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.sequenceFlow";
		else return null;
	}
	
	private Wrapper createFlowWrapper(Element element) {
		FlowWrapper result = null;
		if ("process".equals(element.getNodeName())) {
			result = createProcessWrapper(element);
		}
		return result;
	}
	
	private FlowWrapper createProcessWrapper(Element element) {
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
	private void resolveSequenceFlowTargets(FlowWrapper flowWrapper) {
		ArrayList<ConnectionWrapper> flows = (ArrayList<ConnectionWrapper>)flowWrapper.getElement().getMetaData("flows");
		for (ConnectionWrapper flow : flows) {
			resolveSequenceFlowTarget(flow);
		}		
	}
	
	private void resolveSequenceFlowTarget(ConnectionWrapper connectionWrapper) {
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
	
	private NodeWrapper getNamedNode(String name, FlowWrapper flowWrapper) {
		if (name == null) return null;
		for (NodeWrapper nodeWrapper : flowWrapper.getElements()) {
			if (name.equals(nodeWrapper.getName())) return nodeWrapper;
		}
		return null;
	}
	
	private void addName(Wrapper wrapper,  Element element) {
		String name = element.getAttribute("name");
		if (name == null) return;
		if (wrapper instanceof FlowWrapper) {
			((Flow)((FlowWrapper)wrapper).getElement()).setName(name);
		} else if (wrapper instanceof NodeWrapper){
			((NodeWrapper)wrapper).setName(name);
		}
	}
	
	private void addNodes(FlowWrapper wrapper, Element element) {
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			NodeWrapper result = null;
			if ("start".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.startEvent");
			} else if ("end".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.terminateEndEvent");
			} else if ("end-error".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.errorEndEvent");
			} else if ("end-cancel".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.cancelEndEvent");
			} else if ("state".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.waitTask");
			} else if ("hql".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.hqlTask");
			} else if ("sql".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.sqlTask");
			} else if ("java".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.javaTask");
			} else if ("script".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.scriptTask");
			} else if ("esb".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.serviceTask");
			} else if ("task".equals(child.getNodeName())) {
				result = (NodeWrapper)ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.humanTask");
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
	private void addSequenceFlow(NodeWrapper wrapper, Element element) {
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
	
	
	private ConnectionWrapper createConnectionWrapper(Element element, String elementType) {
		ConnectionWrapper result = (ConnectionWrapper)ElementRegistry.createWrapper(elementType);
		if (result != null) {
			addName(result, element);
			addGraphics(result, element);
		}
		return result;
	}
	
	private void addGraphics(ConnectionWrapper wrapper, Element element) {
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
	
	private void addGraphics(NodeWrapper wrapper, Element element) {
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
	
	private int convertStringToInt(String str) {
		int result = 0;
		try {
			result = new Integer(str).intValue();
		} catch (NumberFormatException e) {
			Logger.logError("Error while converting " + str + " to an integer.", e);
		}
		return result;
	}
	
}
