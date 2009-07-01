/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.EventListener;
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;
import org.jboss.tools.flow.jpdl4.model.Timer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class EventListenerContainerDeserializer extends AbstractElementDeserializer {
	protected List<String> getAttributesToRead() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(EventListenerContainer.EVENT_TYPE);
		return result;
	}
	protected String getXmlName(String attributeName) {
		if (EventListenerContainer.EVENT_TYPE.equals(attributeName)) {
			return "event";
		} else {
			return super.getXmlName(attributeName);
		}
	}
	public Wrapper deserializeChildNode(Wrapper parent, Node node) {
		Wrapper result = null;
		if (!(parent.getElement() instanceof EventListenerContainer)) return result;
		if (node instanceof Element) {
			result = Registry.createWrapper((Element)node);
			if (result != null) {
				if (result.getElement() instanceof EventListener) {
					parent.addChild(EventListenerContainer.LISTENERS, result);
				} else if (result.getElement() instanceof Timer) {
					parent.setPropertyValue(EventListenerContainer.DUE_DATE, result.getPropertyValue(Timer.DUE_DATE));
					parent.setPropertyValue(EventListenerContainer.REPEAT, result.getPropertyValue(Timer.REPEAT));
				}
			}
		}
		return result;
	}
}