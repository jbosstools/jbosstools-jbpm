/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.ExclusiveGateway;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class ExclusiveGatewayDeserializer extends NodeDeserializer {
	
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		if (!(wrapper.getElement() instanceof ExclusiveGateway)) return;
		ExclusiveGateway exclusiveGateWay = (ExclusiveGateway)wrapper.getElement();
		exclusiveGateWay.setExpr(element.getAttribute("expr"));
		exclusiveGateWay.setLang(element.getAttribute("lang"));
	}
	
	public void deserializeChildNodes(Wrapper wrapper,
			Element element) {
		if (wrapper == null) return;
		NodeList nodeList = element.getChildNodes();
		ArrayList<Node> unknownNodeList = new ArrayList<Node>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if ("handler".equals(node.getNodeName())) {
				deserializeHandler(wrapper, node, unknownNodeList);
			} else {
				deserializeChildNode(wrapper, node, unknownNodeList);
			}
		}
		wrapper.getElement().setMetaData("trailingNodes", unknownNodeList);
	}
	
	private void deserializeHandler(Wrapper parent, Node node, List<Node> unknownNodeList) {
		parent.setMetaData("beforeHandlerNodes", new ArrayList<Node>(unknownNodeList));
		unknownNodeList.clear();
		String className = ((Element)node).getAttribute("class");
		parent.setPropertyValue(ExclusiveGateway.HANDLER, "".equals(className) ? null : className);
	}

}