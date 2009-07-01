/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.ExclusiveGateway;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class ExclusiveGatewayDeserializer extends NodeDeserializer {
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		if (!(wrapper.getElement() instanceof ExclusiveGateway)) return;
		ExclusiveGateway exclusiveGateWay = (ExclusiveGateway)wrapper.getElement();
		exclusiveGateWay.setExpr(element.getAttribute("expr"));
		exclusiveGateWay.setLang(element.getAttribute("lang"));
	}
	public Wrapper deserializeChildNode(Wrapper parent, Node node) {
		Wrapper result = null;
		ExclusiveGateway exclusiveGateway = (ExclusiveGateway)parent.getElement();
		if (node instanceof Element && "handler".equals(node.getNodeName())) {
			String className = ((Element)node).getAttribute("class");
			exclusiveGateway.setHandler("".equals(className) ? null : className);
		} else {
			result = super.deserializeChildNode(parent, node);
		}
		return result;
	}
}