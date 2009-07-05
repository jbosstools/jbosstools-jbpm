/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.HqlTask;
import org.jboss.tools.flow.jpdl4.model.PrimitiveObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class HqlTaskDeserializer extends NodeDeserializer {
	
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		wrapper.setPropertyValue(HqlTask.VAR, element.getAttribute("var"));
		wrapper.setPropertyValue(HqlTask.UNIQUE, element.getAttribute("unique"));
	}
	
	public void deserializeChildNodes(Wrapper wrapper,
			Element element) {
		if (wrapper == null) return;
		NodeList nodeList = element.getChildNodes();
		ArrayList<Node> unknownNodeList = new ArrayList<Node>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if ("query".equals(node.getNodeName())) {
				deserializeQuery(wrapper, node, unknownNodeList);
			} else if ("parameters".equals(node.getNodeName())) {
				deserializeParameters(wrapper, node, unknownNodeList);
			} else {
				deserializeChildNode(wrapper, node, unknownNodeList);
			}
		}
		wrapper.getElement().setMetaData("trailingNodes", unknownNodeList);
	}
	
	private void deserializeQuery(Wrapper parent, Node node, List<Node> unknownNodeList) {
		parent.setMetaData("beforeQueryNodes", new ArrayList<Node>(unknownNodeList));
		unknownNodeList.clear();
		// the query has only one child node
		Node content = node.getChildNodes().item(0);
		parent.setPropertyValue(HqlTask.QUERY, content.getNodeValue());
	}
	
	private void deserializeParameters(Wrapper parent, Node node, List<Node> unknownNodeList) {
		parent.setMetaData("beforeParametersNodes", new ArrayList<Node>(unknownNodeList));
		unknownNodeList.clear();
		// the actual parameters are the children of the <parameters> element
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Wrapper childWrapper = deserializeChildNode(parent, nodeList.item(i));		
			if (childWrapper != null && childWrapper.getElement() instanceof PrimitiveObject) {
				parent.addChild(HqlTask.PARAMETERS, childWrapper);
				childWrapper.getElement().setMetaData("leadingNodes", new ArrayList<Node>(unknownNodeList));
				unknownNodeList.clear();
			} else {
				unknownNodeList.add(node);
			}
		}
	}
	
}