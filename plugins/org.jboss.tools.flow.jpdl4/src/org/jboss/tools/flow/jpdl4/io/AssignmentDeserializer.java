/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Assignment;
import org.jboss.tools.flow.jpdl4.model.AssignmentPropertySource;
import org.jboss.tools.flow.jpdl4.model.HumanTask;
import org.w3c.dom.Element;

class AssignmentDeserializer extends AbstractElementDeserializer {
	
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		String assignee = element.getAttribute(Assignment.ASSIGNEE);
		if (!"".equals(assignee)) {
			wrapper.setPropertyValue(
					Assignment.ASSIGNMENT_TYPE, 
					AssignmentPropertySource.getAssignmentTypesIndex(Assignment.ASSIGNEE));
			wrapper.setPropertyValue(Assignment.ASSIGNMENT_EXPRESSION, assignee);
			return;
		}
		String candidateGroups = element.getAttribute(Assignment.CANDIDATE_GROUPS);
		if (!"".equals(candidateGroups)) {
			wrapper.setPropertyValue(
					Assignment.ASSIGNMENT_TYPE, 
					AssignmentPropertySource.getAssignmentTypesIndex(Assignment.CANDIDATE_GROUPS));
			wrapper.setPropertyValue(Assignment.ASSIGNMENT_EXPRESSION, candidateGroups);
			return;
		}
		String swimlane = element.getAttribute(HumanTask.SWIMLANE);
		if (!"".equals(swimlane)) {
			wrapper.setPropertyValue(
					Assignment.ASSIGNMENT_TYPE, 
					AssignmentPropertySource.getAssignmentTypesIndex(HumanTask.SWIMLANE));
			wrapper.setPropertyValue(HumanTask.ASSIGNMENT_EXPRESSION, swimlane);
			return;
		}
	}
	
}