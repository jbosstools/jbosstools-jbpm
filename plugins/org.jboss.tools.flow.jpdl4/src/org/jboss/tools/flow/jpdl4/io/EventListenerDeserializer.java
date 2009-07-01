/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.jpdl4.model.EventListener;

class EventListenerDeserializer extends AbstractElementDeserializer {
	protected List<String> getAttributesToRead() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(EventListener.CLASS_NAME);
		return result;
	}
	protected String getXmlName(String attributeName) {
		if (EventListener.CLASS_NAME.equals(attributeName)) {
			return "class";
		} else {
			return super.getXmlName(attributeName);
		}
	}
}