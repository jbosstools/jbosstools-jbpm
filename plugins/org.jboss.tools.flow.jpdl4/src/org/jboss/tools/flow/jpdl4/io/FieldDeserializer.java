/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Field;
import org.w3c.dom.Element;

class FieldDeserializer extends ArgumentDeserializer {
	
	protected List<String> getAttributesToRead() {
		List<String> result = new ArrayList<String>();
		result.add(Field.NAME);
		return result;
	}
	
	protected String getXmlName(String attributeName) {
		if (Field.NAME.equals(attributeName)) {
			return "name";
		} else {
			return null;
		}
	}
	
	public void deserializeAttributes(Wrapper wrapper, Element element) {
		wrapper.getElement().setMetaData("attributes", element.getAttributes());
		List<String> attributeNames = getAttributesToRead();
		for (String attributeName : attributeNames) {
			String xmlName = getXmlName(attributeName);
			if (xmlName == null) continue;
			String attribute = element.getAttribute(xmlName);
			if (!"".equals(attribute) && attribute != null) {
				wrapper.setPropertyValue(attributeName, attribute);
			}
		}
	}
	
	
	public void deserializeChildNodes(Wrapper wrapper, Element element) {
		wrapper.setPropertyValue(Field.VALUE, streamChildNodes(element));
		
	}
}