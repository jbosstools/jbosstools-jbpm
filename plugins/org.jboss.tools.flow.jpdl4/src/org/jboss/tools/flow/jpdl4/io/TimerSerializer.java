/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.jpdl4.model.Timer;

class TimerSerializer extends AbstractElementSerializer {
	protected List<String> getAttributesToSave() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("duedate");
		result.add("repeat");
		result.add("duedatetime");
		return result;
	}
	protected String getPropertyName(String attributeName) {
		if ("duedate".equals(attributeName)) {
			return Timer.DUE_DATE;
		} else if ("repeat".equals(attributeName)) {
			return Timer.REPEAT;
		} else if ("duedatetime".equals(attributeName)) {
			return Timer.DUE_DATETIME;
		}
		return super.getPropertyName(attributeName);
	}
}