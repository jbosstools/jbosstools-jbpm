/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.ProcessNode;

class ProcessNodeSerializer extends AbstractElementSerializer {
	protected List<String> getAttributesToSave() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("name");
		result.add("g");
		return result;
	}
	protected void appendAttributeToSave(String attributeName, StringBuffer buffer, Wrapper wrapper) {
		if (!(wrapper instanceof NodeWrapper)) return;
		Element element = wrapper.getElement();
		if (!(element instanceof ProcessNode)) return;
		if ("name".equals(attributeName)) {
			appendName(buffer, (ProcessNode)element);
		} else if ("g".equals(attributeName)) {
			appendGraphics(buffer, (NodeWrapper)wrapper);
		} else {
			super.appendAttributeToSave(attributeName, buffer, wrapper);
		}
	}
	protected void appendName(StringBuffer buffer, ProcessNode processNode) {
		String value = processNode.getName();
		if (value == null || "".equals(value)) return;
		buffer.append(" name=\"" + value + "\"");
 	}
	protected void appendGraphics(StringBuffer buffer, NodeWrapper wrapper) {
    	Rectangle constraint = wrapper.getConstraint();
    	buffer.append(" g=\"");
    	buffer.append(constraint.x);
    	buffer.append(",");
    	buffer.append(constraint.y);
    	buffer.append(",");
    	buffer.append(constraint.width);
    	buffer.append(",");
    	buffer.append(constraint.height);
    	buffer.append("\"");
	}
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
    	NodeWrapper nodeWrapper = (NodeWrapper)wrapper;
		List<Element> eventListenerContainers = nodeWrapper.getChildren("eventListener");
		if (eventListenerContainers != null) {
			for (Element eventListenerContainer : eventListenerContainers) {
				if (eventListenerContainer instanceof Wrapper) {
					JpdlSerializer.serialize((Wrapper)eventListenerContainer, buffer, level+1);
				}
			}
		}
    	List<ConnectionWrapper> children = nodeWrapper.getOutgoingConnections();
    	for (ConnectionWrapper connectionWrapper : children) {
			JpdlSerializer.serialize(connectionWrapper, buffer, level+1);
    	}
	}
}