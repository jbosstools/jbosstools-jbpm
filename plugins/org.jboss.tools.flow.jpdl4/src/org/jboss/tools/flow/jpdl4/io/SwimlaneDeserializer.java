/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.properties.IPropertyId;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.w3c.dom.Element;

class SwimlaneDeserializer extends AbstractElementDeserializer {
	AssignmentDeserializer assignmentAttributeHandler = new AssignmentDeserializer();
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		String name = element.getAttribute("name");
		if (!"".equals(name) && name != null) {
			wrapper.setPropertyValue(IPropertyId.NAME, name);
		}
		assignmentAttributeHandler.deserializeAttributes(wrapper, element);
	}
}