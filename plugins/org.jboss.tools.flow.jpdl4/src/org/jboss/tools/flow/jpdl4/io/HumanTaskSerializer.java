/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Assignment;
import org.jboss.tools.flow.jpdl4.model.HumanTask;

class HumanTaskSerializer extends ProcessNodeSerializer {
	protected List<String> getAttributesToSave() {
		List<String> result = super.getAttributesToSave();
		result.add(Assignment.ASSIGNEE);
		result.add(Assignment.CANDIDATE_GROUPS);
		result.add(Assignment.SWIMLANE);
		return result;
	}
	protected void appendAttributeToSave(String attributeName, StringBuffer buffer, Wrapper wrapper) {
		if (!(wrapper instanceof NodeWrapper)) return;
		Element element = wrapper.getElement();
		if (!(element instanceof HumanTask)) return;
		if (HumanTask.ASSIGNEE.equals(attributeName)) {
			appendExpression(HumanTask.ASSIGNEE, buffer, wrapper);
		} else if (HumanTask.CANDIDATE_GROUPS.equals(attributeName)) {
			appendExpression(HumanTask.CANDIDATE_GROUPS, buffer, wrapper);
		} else if (HumanTask.SWIMLANE.equals(attributeName)) {
			appendExpression(HumanTask.SWIMLANE, buffer, wrapper);
		} else {
			super.appendAttributeToSave(attributeName, buffer, wrapper);
		}
	}
	protected void appendExpression(String type, StringBuffer buffer, Wrapper wrapper) {
		Object assignmentType = wrapper.getPropertyValue(HumanTask.ASSIGNMENT_TYPE);
		if (!(assignmentType instanceof Integer)) return;
		if (type.equals(HumanTask.ASSIGNMENT_TYPES[(Integer)assignmentType])) {
			Object value = wrapper.getPropertyValue(HumanTask.ASSIGNMENT_EXPRESSION);
			if (value == null || "".equals(value)) return;
			buffer.append(" " + type + "=\"" + value + "\"");
		}
 	}
}