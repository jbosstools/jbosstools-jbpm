/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.CustomTask;
import org.w3c.dom.Element;

class CustomTaskDeserializer extends NodeDeserializer {
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		wrapper.setPropertyValue(CustomTask.CLASS, element.getAttribute("class"));

	}

}