/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;
import org.jboss.tools.flow.jpdl4.model.Process;
import org.jboss.tools.flow.jpdl4.model.Swimlane;
import org.jboss.tools.flow.jpdl4.model.Timer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class ProcessDeserializer extends AbstractElementDeserializer {
	
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		if (!(wrapper.getElement() instanceof Process)) return;
		Process process = (Process)wrapper.getElement();
		process.setName(element.getAttribute("name"));
		process.setKey(element.getAttribute("key"));
		process.setVersion(element.getAttribute("version"));
		process.setDescription(element.getAttribute("description"));
	}
	
	public Wrapper deserializeChildNode(Wrapper parent, Node node) {
		Wrapper result = null;
		if (!(parent instanceof FlowWrapper)) return result;
		FlowWrapper flowWrapper = (FlowWrapper)parent;
		if (node instanceof Element) {
			result = Registry.createWrapper((Element)node);
			if (result == null) return null;
			if (result instanceof NodeWrapper) {
				flowWrapper.addElement((NodeWrapper)result);
			} else if (result.getElement() instanceof Swimlane) {
				flowWrapper.addChild("swimlane", result);
			} else if (result.getElement() instanceof Timer) {
				flowWrapper.addChild("timer", result);
			} else if (result.getElement() instanceof EventListenerContainer) {
				flowWrapper.addChild("eventListener", result);
			}
		}
		return result;
	}
	
}