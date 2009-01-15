package org.jboss.tools.flow.jpdl4.editor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.jboss.tools.flow.jpdl4.model.CancelEndEvent;
import org.jboss.tools.flow.jpdl4.model.ErrorEndEvent;
import org.jboss.tools.flow.jpdl4.model.ExclusiveGateway;
import org.jboss.tools.flow.jpdl4.model.ForkParallelGateway;
import org.jboss.tools.flow.jpdl4.model.HqlTask;
import org.jboss.tools.flow.jpdl4.model.HumanTask;
import org.jboss.tools.flow.jpdl4.model.JavaTask;
import org.jboss.tools.flow.jpdl4.model.JoinParallelGateway;
import org.jboss.tools.flow.jpdl4.model.Process;
import org.jboss.tools.flow.jpdl4.model.ScriptTask;
import org.jboss.tools.flow.jpdl4.model.SequenceFlow;
import org.jboss.tools.flow.jpdl4.model.ServiceTask;
import org.jboss.tools.flow.jpdl4.model.SqlTask;
import org.jboss.tools.flow.jpdl4.model.StartEvent;
import org.jboss.tools.flow.jpdl4.model.SuperState;
import org.jboss.tools.flow.jpdl4.model.TerminateEndEvent;
import org.jboss.tools.flow.jpdl4.model.WaitTask;
import org.w3c.dom.Node;

public class JpdlSerializer {
	
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
	
    public void serialize(Wrapper wrapper, OutputStream os) throws IOException {
    	StringBuffer buffer = new StringBuffer();
    	appendToBuffer(buffer, wrapper, 0);
    	Writer writer = new OutputStreamWriter(os);
    	writer.write(buffer.toString());
    	writer.close();
    }
    
    private void appendToBuffer(StringBuffer buffer, Wrapper wrapper, int level) {
    	Object object = wrapper.getElement();
        if (!(object instanceof Element)) return;
       	appendOpening(buffer, wrapper, level);
       	StringBuffer body = new StringBuffer();
       	appendBody(body, wrapper, level);
       	if (body.length() > 0) {
       		buffer.append(">");
       		buffer.append(body);
       		appendClosing(buffer, wrapper, level);
       	} else {
       		buffer.append("/>");
       	}
    }
    
    
    
    private void appendNodeList(StringBuffer buffer, ArrayList<Node> nodeList) {
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
    
    @SuppressWarnings("unchecked")
	private void appendOpening(StringBuffer buffer, Wrapper wrapper, int level) {
    	Element element = (Element)wrapper.getElement();
    	ArrayList<Node> leadingNodeList = (ArrayList<Node>)element.getMetaData("leadingNodes");
    	boolean appendLeadingNodes = leadingNodeList != null && !leadingNodeList.isEmpty();
    	if (appendLeadingNodes) {
    		appendNodeList(buffer, leadingNodeList);
    	}
    	if (element instanceof SequenceFlow) {
    		SequenceFlow transition = (SequenceFlow)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<flow");
    		if (transition.getTo() != null) {
    			buffer.append(" ");
        		String value = transition.getTo().getName();
        		value = value == null ? "" : value;
        		buffer.append("to=\"" + value + "\"");
    		}
    		appendConnectionGraphics(buffer, (ConnectionWrapper)wrapper);
    	} else if (element instanceof TerminateEndEvent) {
    		TerminateEndEvent terminateEndEvent = (TerminateEndEvent)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<end");
    		if (!isEmpty(terminateEndEvent.getName())) {
    			buffer.append(" ");
    			String value = terminateEndEvent.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof ErrorEndEvent) {
    		ErrorEndEvent errorEndEvent = (ErrorEndEvent)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<end-error");
    		if (!isEmpty(errorEndEvent.getName())) {
    			buffer.append(" ");
    			String value = errorEndEvent.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof CancelEndEvent) {
    		CancelEndEvent cancelEndEvent = (CancelEndEvent)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<end-cancel");
    		if (!isEmpty(cancelEndEvent.getName())) {
    			buffer.append(" ");
    			String value = cancelEndEvent.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof StartEvent) {
    		StartEvent startEvent = (StartEvent)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<start");
    		if (!isEmpty(startEvent.getName())) {
    			buffer.append(" ");
    			String value = startEvent.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof SuperState) {
    		SuperState superState = (SuperState)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<super-state");
    		if (!isEmpty(superState.getName())) {
    			buffer.append(" ");
    			String value = superState.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    	} else if (element instanceof WaitTask) {
    		WaitTask waitTask = (WaitTask)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<state");
    		if (!isEmpty(waitTask.getName())) {
    			buffer.append(" ");
    			String value = waitTask.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof HqlTask) {
    		HqlTask hqlTask = (HqlTask)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<hql");
    		if (!isEmpty(hqlTask.getName())) {
    			buffer.append(" ");
    			String value = hqlTask.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof SqlTask) {
    		SqlTask sqlTask = (SqlTask)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<sql");
    		if (!isEmpty(sqlTask.getName())) {
    			buffer.append(" ");
    			String value = sqlTask.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof JavaTask) {
    		JavaTask javaTask = (JavaTask)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<java");
    		if (!isEmpty(javaTask.getName())) {
    			buffer.append(" ");
    			String value = javaTask.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof ScriptTask) {
    		ScriptTask scriptTask = (ScriptTask)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<script");
    		if (!isEmpty(scriptTask.getName())) {
    			buffer.append(" ");
    			String value = scriptTask.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof ServiceTask) {
    		ServiceTask serviceTask = (ServiceTask)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<esb");
    		if (!isEmpty(serviceTask.getName())) {
    			buffer.append(" ");
    			String value = serviceTask.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof HumanTask) {
    		HumanTask humanTask = (HumanTask)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<task");
    		if (!isEmpty(humanTask.getName())) {
    			buffer.append(" ");
    			String value = humanTask.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof ExclusiveGateway) {
    		ExclusiveGateway exclusiveGateway = (ExclusiveGateway)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<exclusive");
    		if (!isEmpty(exclusiveGateway.getName())) {
    			buffer.append(" ");
    			String value = exclusiveGateway.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof ForkParallelGateway) {
    		ForkParallelGateway parallelForkGateway = (ForkParallelGateway)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<fork");
    		if (!isEmpty(parallelForkGateway.getName())) {
    			buffer.append(" ");
    			String value = parallelForkGateway.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof JoinParallelGateway) {
    		JoinParallelGateway parallelJoinGateway = (JoinParallelGateway)element;
    		if (!appendLeadingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("<join");
    		if (!isEmpty(parallelJoinGateway.getName())) {
    			buffer.append(" ");
    			String value = parallelJoinGateway.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    		appendNodeGraphics(buffer, (NodeWrapper)wrapper);
    	} else if (element instanceof Process) {
    		Process process = (Process)element;
    		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
    		buffer.append("<process");
    		buffer.append(" xmlns=\"http://jbpm.org/4/jpdl\"");
    		if (process.getInitial() != null) {
    			buffer.append(" ");
    			String value = process.getInitial().getName();
    			value = value == null ? "" : value;
    			buffer.append("initial=\"" + value + "\"");
    		}
    		if (!isEmpty(process.getName())) {
    			buffer.append(" ");
    			String value = process.getName();
    			buffer.append("name=\"" + value + "\"");
    		}
    	}
    	
    }
    
    private boolean isEmpty(String str) {
    	return str == null || "".equals(str);
    }
    
    private void appendPadding(StringBuffer buffer, int level) {
    	for (int i = 0; i < level; i++) {
    		buffer.append("   ");
    	}
    }
    
    @SuppressWarnings("unchecked")
	private void appendClosing(StringBuffer buffer, Wrapper wrapper, int level) {
    	Element element = (Element)wrapper.getElement();
    	ArrayList<Node> trailingNodeList = (ArrayList<Node>)element.getMetaData("trailingNodes");
    	boolean appendTrailingNodes = trailingNodeList != null && !trailingNodeList.isEmpty();
    	if (appendTrailingNodes) {
    		appendNodeList(buffer, trailingNodeList);
    	}
    	if (element instanceof SequenceFlow) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</flow>");
    	} else if (element instanceof TerminateEndEvent) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</end>");
    	} else if (element instanceof ErrorEndEvent) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</end-error>");
    	} else if (element instanceof CancelEndEvent) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</end-cancel>");
    	} else if (element instanceof StartEvent) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
   		buffer.append("</start>");
    	} else if (element instanceof SuperState) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</super-state>");
    	} else if (element instanceof WaitTask) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</state>");
    	} else if (element instanceof HqlTask) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</hql>");
    	} else if (element instanceof SqlTask) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</sql>");
    	} else if (element instanceof JavaTask) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</java>");
    	} else if (element instanceof ScriptTask) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</script>");
    	} else if (element instanceof ServiceTask) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</esb>");
    	} else if (element instanceof HumanTask) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</task>");
    	} else if (element instanceof ExclusiveGateway) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</exclusive>");
    	} else if (element instanceof ForkParallelGateway) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</fork>");
    	} else if (element instanceof JoinParallelGateway) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</join>");
    	} else if (element instanceof Process) {
    		if (!appendTrailingNodes) {
    			buffer.append("\n");
    			appendPadding(buffer, level);
    		}
    		buffer.append("</process>");
    	}	
    }
    
    private void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {
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
        	for (ConnectionWrapper connectionWrapper : children) {
        		appendToBuffer(buffer, connectionWrapper, level+1);
        	}
        } 
    }
    
    private void appendNodeGraphics(StringBuffer buffer, NodeWrapper wrapper) {
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
    
    private void appendConnectionGraphics(StringBuffer buffer, ConnectionWrapper wrapper) {
    	List<Point> bendPoints = wrapper.getBendpoints();
    	if (bendPoints.size() == 0) return;
    	buffer.append(" g=\"");
    	for (int i = 0; i < bendPoints.size(); i++) {
    		buffer.append(bendPoints.get(i).x);
    		buffer.append(",");
    		buffer.append(bendPoints.get(i).y);
    		if (i < bendPoints.size() - 1) buffer.append(";");
    	}
    	buffer.append("\"");
    }
    
}
