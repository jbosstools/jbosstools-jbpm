/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.flow.common.properties.IPropertyId;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.jboss.tools.flow.jpdl4.model.EventListener;
import org.jboss.tools.flow.jpdl4.model.SequenceFlow;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class SequenceFlowDeserializer extends AbstractElementDeserializer {

	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		if (!(wrapper instanceof ConnectionWrapper)) return;
		ConnectionWrapper connectionWrapper = (ConnectionWrapper)wrapper;
		addGraphics(connectionWrapper, element);
		connectionWrapper.getElement().setMetaData("to", element.getAttribute("to"));
		connectionWrapper.setPropertyValue(IPropertyId.NAME, element.getAttribute("name"));
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
	
	public void deserializeChildNodes(Wrapper wrapper,
			Element element) {
		if (wrapper == null) return;
		NodeList nodeList = element.getChildNodes();
		ArrayList<Node> unknownNodeList = new ArrayList<Node>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if ("timer".equals(node.getNodeName())) {
				deserializeTimer(wrapper, node, unknownNodeList);
			} else if ("outcome-value".equals(node.getNodeName())) {
				deserializeOutcomeValue(wrapper, node, unknownNodeList);
			} else {
				deserializeChildNode(wrapper, node, unknownNodeList);
			}
		}
		wrapper.getElement().setMetaData("trailingNodes", unknownNodeList);
	}
	
	public Wrapper deserializeChildNode(Wrapper parent, Node node) {
		Wrapper result = null;
		if (!(parent instanceof ConnectionWrapper)) return result;
		ConnectionWrapper connectionWrapper = (ConnectionWrapper)parent;
		if (node instanceof Element) {
			result = Registry.createWrapper((Element)node);
			if (result == null) return null;
			if (result instanceof Wrapper) {
				if (result.getElement() instanceof EventListener) {
					connectionWrapper.addChild("listener", result);
				}
			}
		}
		return result;
	}
	
	private void deserializeTimer(Wrapper parent, Node node, List<Node> unknownNodeList) {
		if (!(node instanceof Element)) return;
		parent.setMetaData("beforeTimerNodes", new ArrayList<Node>(unknownNodeList));
		unknownNodeList.clear();
		parent.setPropertyValue(SequenceFlow.TIMER, ((Element)node).getAttribute("duedate"));
	}

	private void deserializeOutcomeValue(Wrapper parent, Node node, List<Node> unknownNodeList) {
		parent.setMetaData("beforeOutcomeValueNodes", new ArrayList<Node>(unknownNodeList));
		unknownNodeList.clear();
		// outome-value has only one child node
		Node content = node.getChildNodes().item(0);
		parent.setPropertyValue(SequenceFlow.OUTCOME_VALUE, content.getNodeValue());
	}

	
}