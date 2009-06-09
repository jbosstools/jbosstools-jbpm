package org.jbpm.gd.jpdl.xml;

import org.jbpm.gd.common.xml.XmlElementMapper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EsbNodeMapper implements XmlElementMapper {
	
	private static final String CALLBACK_ACTION_CLASSNAME = "org.jboss.soa.esb.services.jbpm.actionhandlers.EsbActionHandler";
	private static final String ONEWAY_ACTION_CLASSNAME = "org.jboss.soa.esb.services.jbpm.actionhandlers.EsbNotifier";
	
	private Node getActionNode(Node node) {
		NodeList nodes = node.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeName().equals("action")) {
				return nodes.item(i);
			}
		}
		return null;
	}

	public boolean accept(Node node) {
		Node actionNode = getActionNode(node);
		if (actionNode == null) return false;
		Node className = actionNode.getAttributes().getNamedItem("class");
		if (className == null) return false;
		return CALLBACK_ACTION_CLASSNAME.equals(className.getNodeValue()) || 
			ONEWAY_ACTION_CLASSNAME.equals(className.getNodeValue());
	}

}
