package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractElementDeserializer implements ElementDeserializer {

	protected Wrapper deserializeChildNode(Wrapper parent, Node node) {
		return null;
	}

	public void deserializeChildNodes(Wrapper wrapper,
			Element element) {
		if (wrapper == null) return;
		NodeList nodeList = element.getChildNodes();
		ArrayList<Node> unknownNodeList = new ArrayList<Node>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			deserializeChildNode(wrapper, nodeList.item(i), unknownNodeList);
		}
		wrapper.getElement().setMetaData("trailingNodes", unknownNodeList);
	}

	protected void deserializeChildNode(Wrapper parent, Node node, List<Node> unknownNodeList) {
		Wrapper childWrapper = deserializeChildNode(parent, node);		
		if (childWrapper != null) {
			childWrapper.getElement().setMetaData("leadingNodes", new ArrayList<Node>(unknownNodeList));
			unknownNodeList.clear();
		} else {
			unknownNodeList.add(node);
		}
		
	}

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
	
	protected int convertStringToInt(String str) {
		int result = 0;
		try {
			result = new Integer(str).intValue();
		} catch (NumberFormatException e) {
			Logger.logError("Error while converting " + str + " to an integer.", e);
		}
		return result;
	}
	
}
