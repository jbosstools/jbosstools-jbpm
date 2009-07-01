/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.TerminateEndEvent;
import org.w3c.dom.Element;

class TerminateEndEventDeserializer extends NodeDeserializer {
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		if (!(wrapper.getElement() instanceof TerminateEndEvent)) return;
		TerminateEndEvent terminateEndEvent = (TerminateEndEvent)wrapper.getElement();
		terminateEndEvent.setEnds(element.getAttribute("ends"));
		terminateEndEvent.setState(element.getAttribute("state"));
	}
}