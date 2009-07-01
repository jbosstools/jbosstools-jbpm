/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Argument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class ArgumentDeserializer extends AbstractElementDeserializer {
	protected List<String> getAttributesToRead() {
		List<String> result = super.getAttributesToRead();
		return result;
	}
	public Wrapper deserializeChildNode(Wrapper parent, Node node) {
		Wrapper result = null;
		if (node instanceof Element && "string".equals(node.getNodeName())) {
			String value = ((Element)node).getAttribute("value");
			if (value != null && !("".equals(value))) {
				parent.setPropertyValue(Argument.VALUE, value);
			}
		} else {
			result = super.deserializeChildNode(parent, node);
		}
		return result;
	}
}