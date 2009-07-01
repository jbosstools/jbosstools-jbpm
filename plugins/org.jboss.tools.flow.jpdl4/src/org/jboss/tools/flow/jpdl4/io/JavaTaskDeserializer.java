/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Argument;
import org.jboss.tools.flow.jpdl4.model.Field;
import org.jboss.tools.flow.jpdl4.model.JavaTask;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class JavaTaskDeserializer extends NodeDeserializer {
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		wrapper.setPropertyValue(JavaTask.CLASS, element.getAttribute("class"));
		wrapper.setPropertyValue(JavaTask.METHOD, element.getAttribute("method"));
		wrapper.setPropertyValue(JavaTask.VAR, element.getAttribute("var"));
	}
	public Wrapper deserializeChildNode(Wrapper parent, Node node) {
		Wrapper result = super.deserializeChildNode(parent, node);
		if (result == null) return result;
		if (result.getElement() instanceof Argument) {
			parent.addChild(JavaTask.ARGS, result);
		} else if (result.getElement() instanceof Field) {
			parent.addChild(JavaTask.FIELDS, result);
		}
		return result;
	}
}