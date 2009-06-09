package org.jbpm.gd.jpdl.xml;

import org.jbpm.gd.common.xml.XmlElementMapper;
import org.w3c.dom.Node;

public class EsbNotifierMapper implements XmlElementMapper {
	
	private static final String CLASS_NAME = "org.jboss.soa.esb.services.jbpm.actionhandlers.EsbNotifier";
	
	public boolean accept(Node node) {
		Node className = node.getAttributes().getNamedItem("class");
		if (className == null) return false;
		return CLASS_NAME.equals(className.getNodeValue());
	}

}
