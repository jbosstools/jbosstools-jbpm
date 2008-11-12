package org.jboss.tools.flow.jpdl4.editor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
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
    	writer.close();
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
    	if (element instanceof Transition) {
    		Transition transition = (Transition)element;
        	buffer.append("\n");
        	appendPadding(buffer, level);
    		buffer.append("<transition");
    		if (transition.getTo() != null) {
    			buffer.append(" ");
        		String value = transition.getTo().getName();
        		value = value == null ? "" : value;
        		buffer.append("to=\"" + value + "\"");
    		}
    		buffer.append(">");
    	} else if (element instanceof EndState) {
    		EndState endState = (EndState)element;
        	buffer.append("\n");
        	appendPadding(buffer, level);
    		buffer.append("<end-state");
    		if (endState.getName() != null) {
    			buffer.append(" ");
    			String value = endState.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		buffer.append(">");
    	} else if (element instanceof StartState) {
    		StartState startState = (StartState)element;
        	buffer.append("\n");
        	appendPadding(buffer, level);
    		buffer.append("<start-state");
    		if (startState.getName() != null) {
    			buffer.append(" ");
    			String value = startState.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		buffer.append(">");
    	} else if (element instanceof SuperState) {
    		SuperState superState = (SuperState)element;
        	buffer.append("\n");
        	appendPadding(buffer, level);
    		buffer.append("<super-state");
    		if (superState.getName() != null) {
    			buffer.append(" ");
    			String value = superState.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		buffer.append(">");
    	} else if (element instanceof State) {
    		State state = (State)element;
        	buffer.append("\n");
        	appendPadding(buffer, level);
    		buffer.append("<state");
    		if (state.getName() != null) {
    			buffer.append(" ");
    			String value = state.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		buffer.append(">");
    	} else if (element instanceof Process) {
    		Process process = (Process)element;
    		buffer.append("<process");
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
    	if (element instanceof Transition) {
        	buffer.append("\n");
        	appendPadding(buffer, level);
    		buffer.append("</transition>");
    	} else if (element instanceof EndState) {
        	buffer.append("\n");
        	appendPadding(buffer, level);
    		buffer.append("</end-state>");
    	} else if (element instanceof StartState) {
        	buffer.append("\n");
        	appendPadding(buffer, level);
    		buffer.append("</start-state>");
    	} else if (element instanceof SuperState) {
        	buffer.append("\n");
        	appendPadding(buffer, level);
    		buffer.append("</super-state>");
    	} else if (element instanceof State) {
        	buffer.append("\n");
        	appendPadding(buffer, level);
    		buffer.append("</state>");
    	} else if (element instanceof Process) {
        	buffer.append("\n");
        	appendPadding(buffer, level);
    		buffer.append("</process>");
    	}	
    }
    
    private static void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
        if (wrapper instanceof ContainerWrapper) {
        	ContainerWrapper containerWrapper = (ContainerWrapper)wrapper;
        	List<NodeWrapper> children = containerWrapper.getElements();
        	for (NodeWrapper nodeWrapper : children) {
        		appendToBuffer(buffer, nodeWrapper, level+1);
        	}
        }
        if (wrapper instanceof NodeWrapper) {
        	NodeWrapper nodeWrapper = (NodeWrapper)wrapper;
        	List<ConnectionWrapper> children = nodeWrapper.getOutgoingConnections();
        	appendLocation(buffer, (NodeWrapper)wrapper, level+1);
        	for (ConnectionWrapper connectionWrapper : children) {
        		appendToBuffer(buffer, connectionWrapper, level+1);
        	}
        } 
    }
    
    private static void appendLocation(StringBuffer buffer, NodeWrapper wrapper, int level) {
    	Rectangle constraint = wrapper.getConstraint();
    	buffer.append("\n");
    	appendPadding(buffer, level);
    	buffer.append("<location x=\"");
    	buffer.append(constraint.x);
    	buffer.append("\" y=\"");
    	buffer.append(constraint.y);
    	buffer.append("\" w=\"");
    	buffer.append(constraint.width);
    	buffer.append("\" h=\"");
    	buffer.append(constraint.height);
    	buffer.append("\"/>");
    }
    
}
