package org.jboss.tools.flow.jpdl4.editor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.EndState;
import org.jboss.tools.flow.jpdl4.model.Process;
import org.jboss.tools.flow.jpdl4.model.StartState;
import org.jboss.tools.flow.jpdl4.model.State;
import org.jboss.tools.flow.jpdl4.model.SuperState;
import org.jboss.tools.flow.jpdl4.model.Transition;

public class JpdlSerializer {

    public static void serialize(Wrapper wrapper, OutputStream os) throws IOException {
    	StringBuffer buffer = new StringBuffer();
    	appendToBuffer(buffer, wrapper, 0);
    	Writer writer = new OutputStreamWriter(os);
    	writer.write(buffer.toString());
    	System.out.println(buffer.toString());
    }
    
    private static void appendToBuffer(StringBuffer buffer, Wrapper wrapper, int level) {
    	Object object = wrapper.getElement();
        if (!(object instanceof Element)) return;
       	appendOpening(buffer, wrapper, level);
       	appendBody(buffer, wrapper, level);
        appendClosing(buffer, wrapper, level);    	
    }
    
    private static void appendOpening(StringBuffer buffer, Wrapper wrapper, int level) {
    	Element element = (Element)wrapper.getElement();
    	buffer.append("\n");
    	appendPadding(buffer, level);
    	if (element instanceof Transition) {
    		buffer.append("<transition");
    		Transition transition = (Transition)element;
    		if (transition.getTo() != null) {
        		String value = transition.getTo().getName();
        		value = value == null ? "" : value;
        		buffer.append("to=\"" + value + "\"");
    		}
    		buffer.append(">");
    	} else if (element instanceof EndState) {
    		buffer.append("<end-state");
    		EndState endState = (EndState)element;
    		if (endState.getName() != null) {
    			buffer.append(" ");
    			String value = endState.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		buffer.append(">");
    	} else if (element instanceof StartState) {
    		buffer.append("<start-state");
    		StartState startState = (StartState)element;
    		if (startState.getName() != null) {
    			buffer.append(" ");
    			String value = startState.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		buffer.append(">");
    	} else if (element instanceof SuperState) {
    		buffer.append("<super-state");
    		SuperState superState = (SuperState)element;
    		if (superState.getName() != null) {
    			buffer.append(" ");
    			String value = superState.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		buffer.append(">");
    	} else if (element instanceof State) {
    		buffer.append("<state");
    		State state = (State)element;
    		if (state.getName() != null) {
    			buffer.append(" ");
    			String value = state.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		buffer.append(">");
    	} else if (element instanceof Process) {
    		buffer.append("<process");
    		Process process = (Process)element;
    		if (process.getInitial() != null) {
    			buffer.append(" ");
    			String value = process.getInitial().getName();
    			value = value == null ? "" : value;
    			buffer.append("initial=\"" + value + "\"");
    		}
    		if (process.getName() != null) {
    			buffer.append(" ");
    			String value = process.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		buffer.append(">");
    	}
    	
    }
    
    private static void appendPadding(StringBuffer buffer, int level) {
    	for (int i = 0; i < level; i++) {
    		buffer.append("   ");
    	}
    }
    
    private static void appendClosing(StringBuffer buffer, Wrapper wrapper, int level) {
    	Element element = (Element)wrapper.getElement();
    	buffer.append("\n");
    	appendPadding(buffer, level);
    	if (element instanceof Transition) {
    		buffer.append("</transition>");
    	} else if (element instanceof EndState) {
    		buffer.append("</end-state>");
    	} else if (element instanceof StartState) {
    		buffer.append("</start-state>");
    	} else if (element instanceof SuperState) {
    		buffer.append("</super-state>");
    	} else if (element instanceof State) {
    		buffer.append("</state>");
    	} else if (element instanceof Process) {
    		buffer.append("</process>");
    	}	
    }
    
    private static void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
        if (wrapper instanceof ContainerWrapper) {
        	ContainerWrapper containerWrapper = (ContainerWrapper)wrapper;
        	List<NodeWrapper> children = containerWrapper.getElements();
        	for (NodeWrapper nodeWrapper : children) {
        		appendToBuffer(buffer, nodeWrapper, ++level);
        	}
        }
        if (wrapper instanceof NodeWrapper) {
        	NodeWrapper nodeWrapper = (NodeWrapper)wrapper;
        	List<ConnectionWrapper> children = nodeWrapper.getOutgoingConnections();
        	for (ConnectionWrapper connectionWrapper : children) {
        		appendToBuffer(buffer, connectionWrapper, ++level);
        	}
        } 
    }
    
}
