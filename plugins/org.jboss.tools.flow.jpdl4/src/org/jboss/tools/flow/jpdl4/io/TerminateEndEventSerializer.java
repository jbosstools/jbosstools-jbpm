/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

class TerminateEndEventSerializer extends ProcessNodeSerializer {
	protected List<String> getAttributesToSave() {
		List<String> result = super.getAttributesToSave();
		result.add("ends");
		result.add("state");
		return result;
	}
}