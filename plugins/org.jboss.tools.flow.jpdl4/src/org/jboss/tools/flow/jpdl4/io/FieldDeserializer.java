/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Field;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class FieldDeserializer extends AbstractElementDeserializer {
	protected List<String> getAttributesToRead() {
		List<String> result = super.getAttributesToRead();
		result.add(Field.NAME);
		return result;
	}
	protected String getXmlName(String attributeName) {
		if (Field.NAME.equals(attributeName)) {
			return "name";
		} else {
			return super.getXmlName(attributeName);
		}
	}
	public Wrapper deserializeChildNode(Wrapper parent, Node node) {
		Wrapper result = null;
		if (node instanceof Element && "string".equals(node.getNodeName())) {
			String value = ((Element)node).getAttribute("value");
			if (value != null && !("".equals(value))) {
				parent.setPropertyValue(Field.VALUE, value);
			}
		} else {
			result = super.deserializeChildNode(parent, node);
		}
		return result;
	}
}