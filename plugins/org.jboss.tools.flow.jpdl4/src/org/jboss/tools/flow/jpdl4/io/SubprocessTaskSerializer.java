/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.SubprocessTask;

class SubprocessTaskSerializer extends ProcessNodeSerializer {
	protected List<String> getAttributesToSave() {
		List<String> result = super.getAttributesToSave();
		result.add("sub-process-id");
		result.add("sub-process-key");
		result.add("outcome");
		return result;
	}
	protected String getPropertyName(String attributeName) {
		if ("sub-process-id".equals(attributeName)) {
			return SubprocessTask.ID;
		} else if ("sub-process-key".equals(attributeName)) {
			return SubprocessTask.KEY;
		} else if ("outcome".equals(attributeName)) {
			return SubprocessTask.OUTCOME;
		}
		return super.getPropertyName(attributeName);
	}
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
		List<Element> inputParameters = wrapper.getChildren(SubprocessTask.INPUT_PARAMETERS);
		if (inputParameters != null) {
    		for (Element inputParameter : inputParameters) {
    			if (inputParameter instanceof Wrapper) {
        			JpdlSerializer.serialize((Wrapper)inputParameter, buffer, level+1);
    			}
    		}
		}
		List<Element> outputParameters = wrapper.getChildren(SubprocessTask.OUTPUT_PARAMETERS);
		if (outputParameters != null) {
			for (Element outputParameter : outputParameters) {
				if (outputParameter instanceof Wrapper) {
	    			JpdlSerializer.serialize((Wrapper)outputParameter, buffer, level+1);
				}
			}
		}
		super.appendBody(buffer, wrapper, level);
	}
}