/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.InputParameter;
import org.jboss.tools.flow.jpdl4.model.OutputParameter;
import org.jboss.tools.flow.jpdl4.model.SubprocessTask;
import org.w3c.dom.Node;

class SubprocessTaskDeserializer extends NodeDeserializer {
	protected List<String> getAttributesToRead() {
		List<String> result = super.getAttributesToRead();
		result.add(SubprocessTask.ID);
		result.add(SubprocessTask.KEY);
		result.add(SubprocessTask.OUTCOME);
		return result;
	}
	protected String getXmlName(String attributeName) {
		if (SubprocessTask.ID.equals(attributeName)) {
			return "sub-process-id";
		} else if (SubprocessTask.KEY.equals(attributeName)) {
			return "sub-process-key";
		} else if (SubprocessTask.OUTCOME.equals(attributeName)) {
			return "outcome";
		} else {
			return super.getXmlName(attributeName);
		}
	}
	public Wrapper deserializeChildNode(Wrapper parent, Node node) {
		Wrapper result = super.deserializeChildNode(parent, node);
		if (result == null) return result;
		if (result.getElement() instanceof InputParameter) {
			parent.addChild(SubprocessTask.INPUT_PARAMETERS, result);
		} else if (result.getElement() instanceof OutputParameter) {
			parent.addChild(SubprocessTask.OUTPUT_PARAMETERS, result);
		}
		return result;
	}
}