/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.LabelWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.SequenceFlow;

class SequenceFlowSerializer extends AbstractElementSerializer {
	
	protected List<String> getAttributesToSave() {
		ArrayList<String> result = new ArrayList<String>();
		result.add("name");
		result.add("to");
		result.add("g");
		return result;
	}
	
	protected void appendAttributeToSave(String attributeName, StringBuffer buffer, Wrapper wrapper) {
		if (!(wrapper instanceof ConnectionWrapper)) return;
		Element element = wrapper.getElement();
		if (!(element instanceof SequenceFlow)) return;
		if ("name".equals(attributeName)) { 
			appendName(buffer, (SequenceFlow)element);
		} else if ("to".equals(attributeName)) {
			appendTo(buffer, (SequenceFlow)element);
		} else if ("g".equals(attributeName)) {
			appendGraphics(buffer, (ConnectionWrapper)wrapper);
		}
	}
	
	protected void appendName(StringBuffer buffer, SequenceFlow sequenceFlow) {
		if (sequenceFlow.getName() == null) return;
		String value = sequenceFlow.getName();
		if (value == null || "".equals(sequenceFlow.getName())) return;
		buffer.append(" name=\"" + value + "\"");
	}
	
	protected void appendTo(StringBuffer buffer, SequenceFlow sequenceFlow) {
		if (sequenceFlow.getTo() == null) return;
		String value = sequenceFlow.getTo().getName();
		if (value == null || "".equals(value)) return;
		buffer.append(" to=\"" + value + "\"");
	}
	
	protected void appendGraphics(StringBuffer buffer, ConnectionWrapper wrapper) {
    	StringBuffer bendPointBuffer = new StringBuffer();
    	List<Point> bendPoints = wrapper.getBendpoints();
    	if (bendPoints != null && bendPoints.size() > 0) {
    		for (int i = 0; i < bendPoints.size(); i++) {
    			bendPointBuffer.append(bendPoints.get(i).x);
    			bendPointBuffer.append(",");
    			bendPointBuffer.append(bendPoints.get(i).y);
    			if (i < bendPoints.size() - 1) bendPointBuffer.append(";");
    		}	    		
    	}
    	StringBuffer labelBuffer = new StringBuffer();
    	LabelWrapper labelWrapper = wrapper.getLabel();
    	if (labelWrapper != null && !isEmpty(labelWrapper.getText())) {
    		Point location = labelWrapper.getLocation();
    		if (location != null) {
    			labelBuffer.append(location.x);
    			labelBuffer.append(',');
    			labelBuffer.append(location.y);
    		}
    	}	    	
    	if (bendPointBuffer.length() + labelBuffer.length() == 0) return;
    	buffer.append(" g=\"");
    	buffer.append(bendPointBuffer);
    	if (bendPointBuffer.length() > 0) buffer.append(':');
    	buffer.append(labelBuffer);
    	buffer.append("\"");
	}
	
    private boolean isEmpty(String str) {
    	return str == null || "".equals(str);
    }
    
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
		String timer = (String)wrapper.getPropertyValue(SequenceFlow.TIMER);
		if (timer != null && !("".equals(timer))) {
			appendTimer(buffer, wrapper, timer, level);
		}
		String outcome = (String)wrapper.getPropertyValue(SequenceFlow.OUTCOME_VALUE);
		if (outcome != null && !("".equals(outcome))) {
			appendOutcomeValue(buffer, wrapper, outcome, level);
		}
		List<Element> eventListeners = wrapper.getChildren("listener");
		if (eventListeners != null) {
			for (Element eventListener : eventListeners) {
				if (eventListener instanceof Wrapper) {
	    			JpdlSerializer.serialize((Wrapper)eventListener, buffer, level+1);
				}
			}
		}
	}
	
	private void appendTimer(StringBuffer buffer, Wrapper wrapper, String timer, int level) {
		appendUnknownNodes("beforeTimerNodes", buffer, wrapper, level);
		buffer.append("<timer duedate=\"").append(timer).append("\"/>");
	}

	private void appendOutcomeValue(StringBuffer buffer, Wrapper wrapper, String outcome, int level) {
		appendUnknownNodes("beforeOutcomeValueNodes", buffer, wrapper, level);
		buffer.append("<outcome-value>").append(outcome).append("</outcome-value>");
	}

	
}