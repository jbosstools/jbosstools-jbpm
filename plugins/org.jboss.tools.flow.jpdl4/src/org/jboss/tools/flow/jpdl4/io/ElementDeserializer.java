/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.w3c.dom.Element;

interface ElementDeserializer {
	void deserializeAttributes(Wrapper wrapper, Element element);
	void deserializeChildNodes(Wrapper wrapper, Element element);
}