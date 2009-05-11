package org.jboss.tools.flow.jpdl4.editor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.properties.IPropertyId;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.DefaultWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.jboss.tools.flow.jpdl4.model.Assignment;
import org.jboss.tools.flow.jpdl4.model.AssignmentPropertySource;
import org.jboss.tools.flow.jpdl4.model.EventListener;
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;
import org.jboss.tools.flow.jpdl4.model.HumanTask;
import org.jboss.tools.flow.jpdl4.model.SubprocessTask;
import org.jboss.tools.flow.jpdl4.model.Swimlane;
import org.jboss.tools.flow.jpdl4.model.Timer;
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
    	protected List<String> getAttributesToRead() {
    		return new ArrayList<String>();
    	}
    	protected String getXmlName(String attributeName) {
    		return null;
    	}
		public void deserializeAttributes(Wrapper wrapper, Element element) {
			wrapper.getElement().setMetaData("attributes", element.getAttributes());
			List<String> attributeNames = getAttributesToRead();
			for (String attributeName : attributeNames) {
				String xmlName = getXmlName(attributeName);
				if (xmlName == null) continue;
				String attribute = element.getAttribute(xmlName);
				if (!"".equals(attribute) && attribute != null) {
					wrapper.setPropertyValue(attributeName, attribute);
				}
			}
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
	
	class HumanTaskAttributeHandler extends NodeAttributeHandler {
		AssignmentAttributeHandler assignmentAttributeHandler = new AssignmentAttributeHandler();
		public void deserializeAttributes(Wrapper wrapper, Element element) {
			super.deserializeAttributes(wrapper, element);
			assignmentAttributeHandler.deserializeAttributes(wrapper, element);
		}
		
	}
	
	class SwimlaneAttributeHandler extends DefaultAttributeDeserializer {
		AssignmentAttributeHandler assignmentAttributeHandler = new AssignmentAttributeHandler();
		public void deserializeAttributes(Wrapper wrapper, Element element) {
			super.deserializeAttributes(wrapper, element);
			String name = element.getAttribute("name");
			if (!"".equals(name) && name != null) {
				wrapper.setPropertyValue(IPropertyId.NAME, name);
			}
			assignmentAttributeHandler.deserializeAttributes(wrapper, element);
		}
 	}
	
	class TimerAttributeHandler extends DefaultAttributeDeserializer {
    	protected List<String> getAttributesToRead() {
    		List<String> result = super.getAttributesToRead();
    		result.add(Timer.DUE_DATE);
    		result.add(Timer.REPEAT);
    		result.add(Timer.DUE_DATETIME);
    		return result;
    	}
    	protected String getXmlName(String attributeName) {
    		if (Timer.DUE_DATE.equals(attributeName)) {
    			return "duedate";
    		} else if (Timer.REPEAT.equals(attributeName)) {
    			return "repeat";
    		} else if (Timer.DUE_DATETIME.equals(attributeName)) {
    			return "duedatetime";
    		} else {
    			return super.getXmlName(attributeName);
    		}
    	}
 	}
	
	class SubprocessTaskAttributeHandler extends NodeAttributeHandler {
    	protected List<String> getAttributesToRead() {
    		List<String> result = super.getAttributesToRead();
    		result.add(SubprocessTask.ID);
    		result.add(SubprocessTask.KEY);
    		result.add(SubprocessTask.OUTCOME);
    		return result;
    	}
    	protected String getXmlName(String attributeName) {
    		if (SubprocessTask.ID.equals(attributeName)) {
    			return "sub-process-id";
    		} else if (SubprocessTask.KEY.equals(attributeName)) {
    			return "sub-process-key";
    		} else if (SubprocessTask.OUTCOME.equals(attributeName)) {
    			return "outcome";
    		} else {
    			return super.getXmlName(attributeName);
    		}
    	}
 	}
	
	class EventListenerContainerAttributeHandler extends DefaultAttributeDeserializer {
    	protected List<String> getAttributesToRead() {
    		ArrayList<String> result = new ArrayList<String>();
    		result.add(EventListenerContainer.EVENT_TYPE);
    		return result;
    	}
    	protected String getXmlName(String attributeName) {
    		if (EventListenerContainer.EVENT_TYPE.equals(attributeName)) {
    			return "event";
    		} else {
    			return super.getXmlName(attributeName);
    		}
    	}
	}
	
	class EventListenerAttributeHandler extends DefaultAttributeDeserializer {
    	protected List<String> getAttributesToRead() {
    		ArrayList<String> result = new ArrayList<String>();
    		result.add(EventListener.CLASS_NAME);
    		return result;
    	}
    	protected String getXmlName(String attributeName) {
    		if (EventListener.CLASS_NAME.equals(attributeName)) {
    			return "class";
    		} else {
    			return super.getXmlName(attributeName);
    		}
    	}
	}
	
	class AssignmentAttributeHandler implements AttributeDeserializer {
		public void deserializeAttributes(Wrapper wrapper, Element element) {
			String assignee = element.getAttribute(Assignment.ASSIGNEE);
			if (!"".equals(assignee)) {
				wrapper.setPropertyValue(
						Assignment.ASSIGNMENT_TYPE, 
						AssignmentPropertySource.getAssignmentTypesIndex(Assignment.ASSIGNEE));
				wrapper.setPropertyValue(Assignment.ASSIGNMENT_EXPRESSION, assignee);
				return;
			}
			String candidateGroups = element.getAttribute(Assignment.CANDIDATE_GROUPS);
			if (!"".equals(candidateGroups)) {
				wrapper.setPropertyValue(
						Assignment.ASSIGNMENT_TYPE, 
						AssignmentPropertySource.getAssignmentTypesIndex(Assignment.CANDIDATE_GROUPS));
				wrapper.setPropertyValue(Assignment.ASSIGNMENT_EXPRESSION, candidateGroups);
				return;
			}
			String swimlane = element.getAttribute(HumanTask.SWIMLANE);
			if (!"".equals(swimlane)) {
				wrapper.setPropertyValue(
						Assignment.ASSIGNMENT_TYPE, 
						AssignmentPropertySource.getAssignmentTypesIndex(HumanTask.SWIMLANE));
				wrapper.setPropertyValue(HumanTask.ASSIGNMENT_EXPRESSION, swimlane);
				return;
			}
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
				if (result == null) return null;
				if (result instanceof NodeWrapper) {
					flowWrapper.addElement((NodeWrapper)result);
				} else if (result.getElement() instanceof Swimlane) {
					flowWrapper.addChild("swimlane", result);
				} else if (result.getElement() instanceof Timer) {
					flowWrapper.addChild("timer", result);
				} else if (result.getElement() instanceof EventListenerContainer) {
					flowWrapper.addChild("eventListener", result);
				}
			}
			return result;
		}
	}
	
	class EventListenerContainerChildNodeHandler implements ChildNodeDeserializer {
		public Wrapper deserializeChildNode(Wrapper parent, Node node) {
			Wrapper result = null;
			if (node instanceof Element) {
				result = createWrapper((Element)node);
				if (result == null) return null;
				if (result.getElement() instanceof EventListener) {
					parent.addChild(EventListenerContainer.LISTENERS, result);
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
			for (NodeWrapper source : flowWrapper.getNodeWrappers()) {
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
			return getNodeAttributeHandler(wrapper);
		} else if (wrapper instanceof ConnectionWrapper) {
			return new ConnectionAttributeHandler();
		} else if (wrapper instanceof DefaultWrapper) {
			return getDefaultAttributeHandler(wrapper); 
		}
		return null;
	}
	
	private AttributeDeserializer getDefaultAttributeHandler(Wrapper wrapper) {
		Object element = wrapper.getElement();
		if (element instanceof Swimlane) {
			return new SwimlaneAttributeHandler();
		} else if (element instanceof Timer) {
			return new TimerAttributeHandler();
		} else if (element instanceof EventListenerContainer) {
			return new EventListenerContainerAttributeHandler();
		} else if (element instanceof EventListener) {
			return new EventListenerAttributeHandler();
		}
		return null;
	}
	
	private AttributeDeserializer getNodeAttributeHandler(Wrapper wrapper) {
		Object element = wrapper.getElement();
		if (element instanceof HumanTask) {
			return new HumanTaskAttributeHandler();
		} else if (element instanceof SubprocessTask) {
			return new SubprocessTaskAttributeHandler();
		} else {
			return new NodeAttributeHandler();
		}
	}
	
	private ChildNodeDeserializer getChildNodeHandler(Wrapper wrapper) {
		if (wrapper instanceof FlowWrapper) {
			return new ProcessChildNodeHandler();
		} else if (wrapper instanceof NodeWrapper) {
			return new NodeChildNodeHandler();
		} else if (wrapper instanceof DefaultWrapper) {
			return getDefaultChildNodeHandler(wrapper);
		}
		return null;
	}
	
	private ChildNodeDeserializer getDefaultChildNodeHandler(Wrapper wrapper) {
		if (wrapper.getElement() != null && wrapper.getElement() instanceof EventListenerContainer) {
			return new EventListenerContainerChildNodeHandler();
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
		else if ("mail".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.mailTask";
		else if ("task".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.humanTask";
		else if ("sub-process".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.subprocessTask";
		else if ("decision".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.exclusiveGateway";
		else if ("join".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.parallelJoinGateway";
		else if ("fork".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.parallelForkGateway";
		else if ("transition".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.sequenceFlow";
		else if ("swimlane".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.swimlane";
		else if ("timer".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.timer";
		else if ("on".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.eventListenerContainer";
		else if ("event-listener".equals(nodeName)) return "org.jboss.tools.flow.jpdl4.eventListener";
		else return null;
	}
	
	private NodeWrapper getNamedNode(String name, FlowWrapper flowWrapper) {
		if (name == null) return null;
		for (NodeWrapper nodeWrapper : flowWrapper.getNodeWrappers()) {
			if (name.equals(nodeWrapper.getName())) return nodeWrapper;
		}
		return null;
	}
	
	private void addGraphics(ConnectionWrapper wrapper, Element element) {
		String graphics = element.getAttribute("g");
		if (graphics != null && !"".equals(graphics)) {
			int pos = graphics.lastIndexOf(':');
			String labelInfo, bendpointInfo = null;
			if (pos != -1) {
				labelInfo = graphics.substring(pos + 1);
				bendpointInfo = graphics.substring(0, pos);
			} else {
				labelInfo = graphics;
			}
			if (labelInfo != null && !"".equals(labelInfo)) {
				addLabelInfo(wrapper, labelInfo);
			}
			if (bendpointInfo != null && !"".equals(bendpointInfo)) {
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
