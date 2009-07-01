/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Process;

class ProcessSerializer extends AbstractElementSerializer {
	public void appendOpening(StringBuffer buffer, Wrapper wrapper, int level) {
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
		buffer.append("<" + Registry.getXmlNodeName(wrapper.getElement()));
		appendAttributes(buffer, wrapper, level);
	}
	protected List<String> getAttributesToSave() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("name");
		result.add("initial");
		result.add("key");
		result.add("version");
		result.add("description");
		return result;
	}
	protected void appendAttributeToSave(String attributeName, StringBuffer buffer, Wrapper wrapper) {
		if (!(wrapper instanceof FlowWrapper)) return;
		Element element = wrapper.getElement();
		if (element == null || !(element instanceof Process)) return;
		if ("name".equals(attributeName)) {
			appendName(buffer, (Process)element);
		} else if ("key".equals(attributeName)) {
			appendKey(buffer, (Process)element);
		} else if ("version".equals(attributeName)) {
			appendVersion(buffer, (Process)element);
		} else if ("description".equals(attributeName)) {
			appendDescription(buffer, (Process)element);
		} else if ("initial".equals(attributeName)) {
			appendInitial(buffer, (Process)element);
		} else {
			super.appendAttributeToSave(attributeName, buffer, wrapper);    	    		
		} 
	}
	protected void appendName(StringBuffer buffer, Process process) {
		String value = process.getName();
		if (value == null || "".equals(value)) return;
		buffer.append(" name=\"" + value + "\"");
 	}
	protected void appendInitial(StringBuffer buffer, Process process) {
		if (process.getInitial() == null) return;
		String value = process.getInitial().getName();
		if (value == null || "".equals(value)) return;
		buffer.append(" initial=\"" + value + "\"");
	}
	protected void appendKey(StringBuffer buffer, Process process) {
		if (process.getKey() == null) return;
		String value = process.getKey();
		if (value == null || "".equals(value)) return;
		buffer.append(" key=\"" + value + "\"");
	}
	protected void appendVersion(StringBuffer buffer, Process process) {
		if (process.getVersion() == null) return;
		String value = process.getVersion();
		if (value == null || "".equals(value)) return;
		buffer.append(" version=\"" + value + "\"");
	}
	protected void appendDescription(StringBuffer buffer, Process process) {
		if (process.getDescription() == null) return;
		String value = process.getDescription();
		if (value == null || "".equals(value)) return;
		buffer.append(" description=\"" + value + "\"");
	}
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
		FlowWrapper flowWrapper = (FlowWrapper)wrapper;
		List<Element> swimlanes = flowWrapper.getChildren("swimlane");
		if (swimlanes != null) {
    		for (Element swimlane : swimlanes) {
    			if (swimlane instanceof Wrapper) {
    				JpdlSerializer.serialize((Wrapper)swimlane, buffer, level+1);
    			}
    		}
		}
		List<Element> timers = flowWrapper.getChildren("timer");
		if (timers != null) {
			for (Element timer : timers) {
				if (timer instanceof Wrapper) {
	    			JpdlSerializer.serialize((Wrapper)timer, buffer, level+1);
				}
			}
		}
		List<Element> eventListenerContainers = flowWrapper.getChildren("eventListener");
		if (eventListenerContainers != null) {
			for (Element eventListenerContainer : eventListenerContainers) {
				if (eventListenerContainer instanceof Wrapper) {
	    			JpdlSerializer.serialize((Wrapper)eventListenerContainer, buffer, level+1);
				}
			}
		}
    	ContainerWrapper containerWrapper = (ContainerWrapper)wrapper;
    	List<NodeWrapper> children = containerWrapper.getNodeWrappers();
    	for (NodeWrapper nodeWrapper : children) {
			JpdlSerializer.serialize(nodeWrapper, buffer, level+1);
    	}
	}
}