package org.jboss.tools.flow.jpdl4.io;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.w3c.dom.Document;

public class JpdlDeserializer {
	
	private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	
	public static Wrapper deserialize(InputStream is) {
		Wrapper result = null;
		try {
			Document document = documentBuilderFactory.newDocumentBuilder().parse(is);
			result = Registry.createWrapper(document.getDocumentElement());
		} catch (Exception e) {
			Logger.logError("An error occurred while creating the diagram", e);
		}
		return result;
	}
	
}
