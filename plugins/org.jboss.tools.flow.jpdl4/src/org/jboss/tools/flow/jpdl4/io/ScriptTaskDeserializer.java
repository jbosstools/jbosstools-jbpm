/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.ScriptTask;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class ScriptTaskDeserializer extends NodeDeserializer {
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		wrapper.setPropertyValue(ScriptTask.EXPR, element.getAttribute("expr"));
		wrapper.setPropertyValue(ScriptTask.LANG, element.getAttribute("lang"));
		wrapper.setPropertyValue(ScriptTask.VAR, element.getAttribute("var"));
	}
	public Wrapper deserializeChildNode(Wrapper parent, Node node) {
		Wrapper result = null;
		if (node instanceof Element && "text".equals(node.getNodeName())) {
			String text = ((Element)node).getTextContent();
			if (text != null && !("".equals(text))) {
				parent.setPropertyValue(ScriptTask.TEXT, text);
			}
		} else {
			result = super.deserializeChildNode(parent, node);
		}
		return result;
	}
}