/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.properties.IPropertyId;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Assignment;
import org.jboss.tools.flow.jpdl4.model.HumanTask;
import org.jboss.tools.flow.jpdl4.model.Swimlane;

class SwimlaneSerializer extends AbstractElementSerializer {
	protected List<String> getAttributesToSave() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("name");
		result.add(Assignment.ASSIGNEE);
		result.add(Assignment.CANDIDATE_GROUPS);
		result.add(Assignment.SWIMLANE);
		return result;
	}
	protected void appendAttributeToSave(String attributeName, StringBuffer buffer, Wrapper wrapper) {
		Element element = wrapper.getElement();
		if (!(element instanceof Swimlane)) return;
		if (Assignment.ASSIGNEE.equals(attributeName)) {
			appendExpression(Assignment.ASSIGNEE, buffer, wrapper);
		} else if (Assignment.CANDIDATE_GROUPS.equals(attributeName)) {
			appendExpression(Assignment.CANDIDATE_GROUPS, buffer, wrapper);
		} else if (Assignment.SWIMLANE.equals(attributeName)) {
			appendExpression(Assignment.SWIMLANE, buffer, wrapper);
		} else if ("name".equals(attributeName)){
			appendName(buffer, wrapper);
		}
	}
	protected void appendName(StringBuffer buffer, Wrapper wrapper) {
		String value = (String)wrapper.getPropertyValue(IPropertyId.NAME);
		if (value == null || "".equals(value)) return;
		buffer.append(" name=\"" + value + "\"");
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
	public void appendOpening(StringBuffer buffer, Wrapper wrapper, int level) {
		appendUnknownNodes("leadingNodes", buffer, wrapper, level);
		buffer.append("<swimlane");
		appendAttributes(buffer, wrapper, level);
	}
}