package org.jboss.tools.flow.jpdl4.editor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.properties.IPropertyId;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JpdlDeserializer {
	
	interface AttributeDeserializer {
		void deserializeAttributes(Wrapper wrapper, Element element);
	}
	
	interface ChildNodeDeserializer {
		Wrapper deserializeChildNode(Wrapper parent, Node node);
	}
	
	interface PostProcessor {
		void postProcess(Wrapper wrapper);
	}
	
	class DefaultAttributeDeserializer implements AttributeDeserializer {
		public void deserializeAttributes(Wrapper wrapper, Element element) {
			wrapper.getElement().setMetaData("attributes", element.getAttributes());
		}
	}
	
	class ProcessAttributeHandler extends DefaultAttributeDeserializer {
		public void deserializeAttributes(Wrapper wrapper, Element element) {
			super.deserializeAttributes(wrapper, element);
			if (!(wrapper instanceof FlowWrapper)) return;
			FlowWrapper flowWrapper = (FlowWrapper)wrapper;
			((Flow)flowWrapper.getElement()).setName(element.getAttribute("name"));
		}
	}
	
	class NodeAttributeHandler extends DefaultAttributeDeserializer {
		public void deserializeAttributes(Wrapper wrapper, Element element) {
			super.deserializeAttributes(wrapper, element);
			if (!(wrapper instanceof NodeWrapper)) return;
			NodeWrapper nodeWrapper = (NodeWrapper)wrapper;
			addGraphics(nodeWrapper, element);
			nodeWrapper.setName(element.getAttribute("name"));			
		}
	}
	
	class ConnectionAttributeHandler extends DefaultAttributeDeserializer {
		public void deserializeAttributes(Wrapper wrapper, Element element) {
			super.deserializeAttributes(wrapper, element);
			if (!(wrapper instanceof ConnectionWrapper)) return;
			ConnectionWrapper connectionWrapper = (ConnectionWrapper)wrapper;
			addGraphics(connectionWrapper, element);
			connectionWrapper.getElement().setMetaData("to", element.getAttribute("to"));
			connectionWrapper.setPropertyValue(IPropertyId.NAME, element.getAttribute("name"));
		}
	}
	
	class ProcessChildNodeHandler implements ChildNodeDeserializer {
		public Wrapper deserializeChildNode(Wrapper parent, Node node) {
			Wrapper result = null;
			if (!(parent instanceof FlowWrapper)) return result;
			FlowWrapper flowWrapper = (FlowWrapper)parent;
			if (node instanceof Element) {
				result = createWrapper((Element)node);
				if (result != null && result instanceof NodeWrapper) {
					flowWrapper.addElement((NodeWrapper)result);
				}
			}
			return result;
		}
	}
	
	class NodeChildNodeHandler implements ChildNodeDeserializer {
		@SuppressWarnings("unchecked")
		public Wrapper deserializeChildNode(Wrapper parent, Node node) {
			Wrapper result = null;
			if (!(parent instanceof NodeWrapper)) return result;
			NodeWrapper nodeWrapper = (NodeWrapper)parent;
			ArrayList<ConnectionWrapper> flows = (ArrayList<ConnectionWrapper>)nodeWrapper.getElement().getMetaData("flows");
			if (flows == null) {
				flows = new ArrayList<ConnectionWrapper>();
				nodeWrapper.getElement().setMetaData("flows", flows);
			}
			if (node instanceof Element) {
				result = createWrapper((Element)node);
				if (result != null && result instanceof ConnectionWrapper) {
					flows.add((ConnectionWrapper)result);
				}
			}
			return result;
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
		AttributeDeserializer attributeHandler = getAttributeHandler(result);
		if (attributeHandler != null) {
			attributeHandler.deserializeAttributes(result, element);
		}
		ChildNodeDeserializer childNodeHandler = getChildNodeHandler(result);
		if (childNodeHandler != null) {
			NodeList nodeList = element.getChildNodes();
			ArrayList<Node> unknownNodeList = new ArrayList<Node>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Wrapper childWrapper = childNodeHandler.deserializeChildNode(result, nodeList.item(i));		
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
	
	private AttributeDeserializer getAttributeHandler(Wrapper wrapper) {
		if (wrapper instanceof FlowWrapper) {
			return new ProcessAttributeHandler();
		} else if (wrapper instanceof NodeWrapper) {
			return new NodeAttributeHandler();
		} else if (wrapper instanceof ConnectionWrapper) {
			return new ConnectionAttributeHandler();
		}
		return null;
	}
	
	private ChildNodeDeserializer getChildNodeHandler(Wrapper wrapper) {
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
		else if ("transition".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.sequenceFlow";
		else return null;
	}
	
	private NodeWrapper getNamedNode(String name, FlowWrapper flowWrapper) {
		if (name == null) return null;
		for (NodeWrapper nodeWrapper : flowWrapper.getElements()) {
			if (name.equals(nodeWrapper.getName())) return nodeWrapper;
		}
		return null;
	}
	
	private void addGraphics(ConnectionWrapper wrapper, Element element) {
		String graphics = element.getAttribute("g");
		if (graphics != null) {
			int pos = graphics.lastIndexOf(':');
			String labelInfo, bendpointInfo = null;
			if (pos != -1) {
				labelInfo = graphics.substring(pos + 1);
				bendpointInfo = graphics.substring(0, pos);
			} else {
				labelInfo = graphics;
			}
			if (labelInfo != null) {
				addLabelInfo(wrapper, labelInfo);
			}
			if (bendpointInfo != null) {
				addBendpointInfo(wrapper, bendpointInfo);
			}
		}
	}
	
	private void addBendpointInfo(ConnectionWrapper wrapper, String bendpointInfo) {
		StringTokenizer bendpoints = new StringTokenizer(bendpointInfo, ";");
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
	
	private void addLabelInfo(ConnectionWrapper wrapper, String labelInfo) {
		StringTokenizer label = new StringTokenizer(labelInfo, ",");
		if (label.countTokens() != 2) {
			Logger.logInfo("Wrong info in attribute 'g' while determining label location.");
		} else {
			int x = convertStringToInt(label.nextToken());
			int y = convertStringToInt(label.nextToken());
			wrapper.getLabel().setLocation(new Point(x, y));
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
