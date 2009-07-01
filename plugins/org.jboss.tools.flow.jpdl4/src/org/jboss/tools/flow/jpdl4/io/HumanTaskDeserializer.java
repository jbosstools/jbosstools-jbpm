/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.w3c.dom.Element;

class HumanTaskDeserializer extends NodeDeserializer {
	AssignmentDeserializer assignmentAttributeHandler = new AssignmentDeserializer();
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		assignmentAttributeHandler.deserializeAttributes(wrapper, element);
	}
	
}