/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

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
	
	public Wrapper deserializeChildNode(Wrapper parent, Node node) {
		Wrapper result = null;
		if (!(parent instanceof ConnectionWrapper)) return result;
		ConnectionWrapper connectionWrapper = (ConnectionWrapper)parent;
		if (node instanceof Element) {
			if ("timer".equals(node.getNodeName())) {
				String duedate = ((Element)node).getAttribute("duedate");
				if (duedate != null && !("".equals(duedate))) {
					parent.setPropertyValue(SequenceFlow.TIMER, duedate);
				}
			} else if ("outcome-value".equals(node.getNodeName())) {
				NodeList nodeList = ((Element)node).getElementsByTagName("string");
				if (nodeList.getLength() == 1) {
					String value = ((Element)nodeList.item(0)).getAttribute("value");
					if (value != null && !("".equals("value"))) {
						parent.setPropertyValue(SequenceFlow.OUTCOME_VALUE, value);
					}
				}
			} else {
				result = Registry.createWrapper((Element)node);
			}
			if (result == null) return null;
			if (result instanceof Wrapper) {
				if (result.getElement() instanceof EventListener) {
					connectionWrapper.addChild("listener", result);
				}
			}
		}
		return result;
	}

	
}