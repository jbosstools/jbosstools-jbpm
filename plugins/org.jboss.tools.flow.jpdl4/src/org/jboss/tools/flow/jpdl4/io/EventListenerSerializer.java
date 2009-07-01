/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.jpdl4.model.EventListener;

class EventListenerSerializer extends AbstractElementSerializer {
	protected List<String> getAttributesToSave() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("class");
		return result;
	}
	protected String getPropertyName(String attributeName) {
		if ("class".equals(attributeName)) {
			return EventListener.CLASS_NAME;
		}
		return super.getPropertyName(attributeName);
	}
}