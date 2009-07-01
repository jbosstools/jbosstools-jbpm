/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.jpdl4.model.Timer;

class TimerDeserializer extends AbstractElementDeserializer {
	protected List<String> getAttributesToRead() {
		List<String> result = super.getAttributesToRead();
		result.add(Timer.DUE_DATE);
		result.add(Timer.REPEAT);
		result.add(Timer.DUE_DATETIME);
		return result;
	}
	protected String getXmlName(String attributeName) {
		if (Timer.DUE_DATE.equals(attributeName)) {
			return "duedate";
		} else if (Timer.REPEAT.equals(attributeName)) {
			return "repeat";
		} else if (Timer.DUE_DATETIME.equals(attributeName)) {
			return "duedatetime";
		} else {
			return super.getXmlName(attributeName);
		}
	}
}