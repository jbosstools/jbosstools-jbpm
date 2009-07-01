/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;

class EventListenerContainerSerializer extends AbstractElementSerializer {
	protected List<String> getAttributesToSave() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("event");
		return result;
	}
	protected String getPropertyName(String attributeName) {
		if ("event".equals(attributeName)) {
			return EventListenerContainer.EVENT_TYPE;
		}
		return super.getPropertyName(attributeName);
	}
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
		EventListenerContainer eventListenerContainer = (EventListenerContainer)wrapper.getElement();
		String dueDate = eventListenerContainer.getDueDate();
		String repeat = eventListenerContainer.getRepeat();
		if ((dueDate != null && !"".equals(dueDate)) || (repeat != null && !"".equals(repeat))) {
			buffer.append("\n");
			appendPadding(buffer, level);
			buffer.append("<timer");
			if (dueDate != null && !"".equals(dueDate)) {
				buffer.append(" duedate=\"" + dueDate + "\"");
			}
			if (repeat != null && !"".equals(repeat)) {
				buffer.append(" repeat=\"" + repeat + "\"");
			}
			buffer.append("/>");
		}
		List<Element> eventListeners = wrapper.getChildren(EventListenerContainer.LISTENERS);
		if (eventListeners != null) {
			for (Element eventListener : eventListeners) {
				if (eventListener instanceof Wrapper) {
	    			JpdlSerializer.serialize((Wrapper)eventListener, buffer, level+1);
				}
			}
		}
	}
}