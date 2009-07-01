/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.jboss.tools.flow.jpdl4.model.Argument;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


class ArgumentDeserializer extends PrimitiveObjectDeserializer {

	public void deserializeAttributes(Wrapper wrapper, Element element) {
	}

	public void deserializeChildNodes(Wrapper wrapper, Element element) {
		wrapper.setPropertyValue(Argument.VALUE, streamChildNodes(element));
		
	}

	protected String streamChildNodes(Element element) {
		NodeList nodeList = element.getChildNodes();
		StringBuffer buffer = new StringBuffer();
		DOMSource domSource = new DOMSource();
		for (int i = 0; i < nodeList.getLength(); i++) {
			StringWriter writer = new StringWriter();
			domSource.setNode(nodeList.item(i));
			Result result = new StreamResult(writer);
			try {
				getTransformer().transform(domSource, result);
			} catch (TransformerException e) {
				Logger.logError("Exception while transforming xml.", e);
			}
			buffer.append(writer.getBuffer());
		}
		return buffer.toString();
	}


}