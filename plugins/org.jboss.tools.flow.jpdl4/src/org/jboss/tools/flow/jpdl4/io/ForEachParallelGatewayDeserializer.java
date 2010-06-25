package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.ForeachParallelGateway;
import org.w3c.dom.Element;

public class ForEachParallelGatewayDeserializer extends NodeDeserializer {
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		wrapper.setPropertyValue(ForeachParallelGateway.VAR, element.getAttribute("var"));
		wrapper.setPropertyValue(ForeachParallelGateway.IN, element.getAttribute("in"));
	}
}
