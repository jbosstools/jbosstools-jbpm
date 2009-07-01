/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.jboss.tools.flow.jpdl4.model.PrimitiveObject;
import org.w3c.dom.Element;

class PrimitiveObjectDeserializer implements ElementDeserializer {

	private static TransformerFactory transformerFactory = TransformerFactory.newInstance();
	private static Transformer transformer = null;
	
	protected static Transformer getTransformer() {
		if (transformer == null) {
			try {
				transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			} catch (TransformerConfigurationException e) {				
				Logger.logError("Error while creating XML tranformer.", e);	
			}
		}
		return transformer;
	}
	
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		DOMSource domSource = new DOMSource();
	    StringWriter writer = new StringWriter();
	    domSource.setNode(element);
	    Result result = new StreamResult(writer);
	    try {
			getTransformer().transform(domSource, result);
		} catch (TransformerException e) {
			Logger.logError("Exception while transforming xml.", e);
		}
		wrapper.setPropertyValue(PrimitiveObject.VALUE, writer.getBuffer().toString());
		
	}

	public void deserializeChildNodes(Wrapper wrapper, Element element) {
	}

}