package org.jboss.tools.flow.jpdl4.io;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class AbstractElementSerializer implements ElementSerializer {

	private static TransformerFactory transformerFactory = TransformerFactory.newInstance();
	private static Transformer transformer = null;
	
	static {
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		} catch (TransformerConfigurationException e) {				
			Logger.logError("Error while creating XML tranformer.", e);	
		}
	}
	
	public void appendToBuffer(StringBuffer buffer, Wrapper wrapper, int level) {		
      	appendOpening(buffer, wrapper, level);
       	StringBuffer body = new StringBuffer();
       	appendBody(body, wrapper, level);
       	appendTrailingNodes(body, wrapper, level);
       	if (body.length() > 0) {
       		buffer.append(">");
       		buffer.append(body);
       		appendClosing(buffer, wrapper, level);
       	} else {
       		buffer.append("/>"); 
       	}
	}
	
	protected void appendPadding(StringBuffer buffer, int level) {
		for (int i = 0; i < level; i++) {
			buffer.append("   ");
		}
	}

	protected List<String> getAttributesToSave() {
		return new ArrayList<String>();
	}
	
	protected String getPropertyName(String attributeName) {
		return attributeName;
	}
	
	protected void appendAttributeToSave(String attributeName, StringBuffer buffer, Wrapper wrapper) {
		String value = (String)wrapper.getPropertyValue(getPropertyName(attributeName));
		if (value == null || "".equals(value)) return;
		buffer.append(" " + attributeName + "=\"" + value + "\"");
	}
	
	@SuppressWarnings("unchecked")
	protected void appendUnknownNodes(String type, StringBuffer buffer, Wrapper wrapper, int level) {
    	ArrayList<Node> leadingNodeList = (ArrayList<Node>)wrapper.getElement().getMetaData(type);
    	boolean appendLeadingNodes = leadingNodeList != null && !leadingNodeList.isEmpty();
    	if (appendLeadingNodes) {
    		appendNodeList(buffer, leadingNodeList);
    	} else {
    		buffer.append("\n");
    		appendPadding(buffer, level);
    	}
	}
	
	@SuppressWarnings("unchecked")
	protected void appendTrailingNodes(StringBuffer buffer, Wrapper wrapper, int level) {
		ArrayList<Node> trailingNodeList = (ArrayList<Node>)wrapper.getElement().getMetaData("trailingNodes");
		boolean appendTrailingNodes = trailingNodeList != null && !trailingNodeList.isEmpty();
		if (appendTrailingNodes) {
			appendNodeList(buffer, trailingNodeList);
		} else if (buffer.length() > 0){
			buffer.append("\n");
			appendPadding(buffer, level);
		}		
	}
	
	protected void appendDefaultAttribute(StringBuffer buffer, Node node) {
		buffer.append(" " + node.getNodeName() + "=\"" + node.getNodeValue() + "\"");
	}
	
	protected void appendAttributes(StringBuffer buffer, Wrapper wrapper, int level) {
		Element element = wrapper.getElement();
		if (element == null) return;
		NamedNodeMap attributes = (NamedNodeMap)element.getMetaData("attributes");
		List<String> attributeNames = getAttributesToSave();
		if (attributes != null) {
     		for (int i = 0; i < attributes.getLength(); i++) {
    			String nodeName = attributes.item(i).getNodeName();
    			if (attributeNames.contains(nodeName)) {
    				appendAttributeToSave(nodeName, buffer, wrapper);
    				attributeNames.remove(nodeName);
    			} else {
    				appendDefaultAttribute(buffer, attributes.item(i));
    			}
    		}
		}
 		for (int i = 0; i < attributeNames.size(); i++) {
 			appendAttributeToSave(attributeNames.get(i), buffer, wrapper);
 		}
	}
	
    protected void appendNodeList(StringBuffer buffer, ArrayList<Node> nodeList) {
		if (transformer == null) {
			Logger.logInfo("Skipping append nodes as transformer is not initialized.");
			return;
		}
		DOMSource domSource = new DOMSource();
		for (Node node : nodeList) {
	    	StringWriter writer = new StringWriter();
	    	domSource.setNode(node);
	    	Result result = new StreamResult(writer);
	    	try {
				transformer.transform(domSource, result);
			} catch (TransformerException e) {
				Logger.logError("Exception while transforming xml.", e);
			}
			buffer.append(writer.getBuffer());
		}
	}
    
	protected void appendOpening(StringBuffer buffer, Wrapper wrapper, int level) {
		appendUnknownNodes("leadingNodes", buffer, wrapper, level);
		buffer.append("<" + Registry.getXmlNodeName(wrapper.getElement()));
		appendAttributes(buffer, wrapper, level);
	}
	
	protected void appendClosing(StringBuffer buffer, Wrapper wrapper, int level) {
		buffer.append("</" + Registry.getXmlNodeName(wrapper.getElement()) + ">");
	}
	
	protected void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
	}
	
}
