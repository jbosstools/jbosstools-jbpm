package org.jboss.tools.flow.jpdl4.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.jboss.tools.flow.common.model.Connection;
import org.jboss.tools.flow.common.model.Container;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.model.Flow;
import org.jboss.tools.flow.common.model.Node;
import org.jboss.tools.flow.jpdl4.model.SequenceFlow;

public class Jpdl4Helper {
	
	@SuppressWarnings("unchecked")
	private static List<org.w3c.dom.Node> getTrailingNodes(Element element) {
		List<org.w3c.dom.Node> result = (List<org.w3c.dom.Node>)element.getMetaData("trailingNodes");
		if (result == null) {
			result = new ArrayList<org.w3c.dom.Node>();
		}
		return result;
	}
		
	@SuppressWarnings("unchecked")
	private static List<org.w3c.dom.Node> getLeadingNodes(Element element) {
		List<org.w3c.dom.Node> result = (List<org.w3c.dom.Node>)element.getMetaData("leadingNodes");
		if (result == null) {
			result = new ArrayList<org.w3c.dom.Node>();
		}
		return result;
	}
	
	public static void mergeLeadingNodes(Element toBeRemoved) {
		if (toBeRemoved == null) return;
		List<org.w3c.dom.Node> successorNodes = getSuccessorNodes(toBeRemoved);
		List<org.w3c.dom.Node> nodesToMerge = getLeadingNodes(toBeRemoved);
		boolean started = false;
		for (int i = nodesToMerge.size(); i > 0; i--) {
			org.w3c.dom.Node node = nodesToMerge.get(i - 1);
			if (node.getNodeType() != org.w3c.dom.Node.TEXT_NODE && !started) {
				started = true;
			}
			if (started) {
				successorNodes.add(0, node);
			}
			if (node .getNodeType() == org.w3c.dom.Node.TEXT_NODE && !started) {
				started = true;
			}
		}
	}
	
	private static List<org.w3c.dom.Node> getSuccessorNodes(Element element) {
		Element successor = getSuccessor(element);
		if (successor != null) {
			return getLeadingNodes(successor);
		}
		Element parent = getParent(element);
		if (parent != null) {
			return getTrailingNodes(parent);
		}
		return new ArrayList<org.w3c.dom.Node>();
	}
	
	private static Element getParent(Element element) {
		if (element instanceof SequenceFlow) {
			return ((SequenceFlow) element).getFrom();
		} else if (element instanceof Node) {
			return ((Node) element).getNodeContainer();
		}
		return null;
	}
	
	private static Element getSuccessor(Element element) {
		if (element instanceof SequenceFlow) {
			Node node = ((SequenceFlow)element).getFrom();
			List<Connection> connections = node.getOutgoingConnections(null);
			for (int i = 0; i < connections.size() - 1; i++) {
				if (connections.get(i) == element) {
					return connections.get(i);
				}
			}
		} else if (element instanceof Node) {
			Container container = ((Node)element).getNodeContainer();
			List<Node> nodes = container.getNodes();
			for (int i = 0; i < nodes.size() - 1; i++) {
				if (nodes.get(i) == element) {
					return nodes.get(i);
				}
			}
		}
		return null;
	}
	
	public static String getLabel(Node child, Flow container) {
		String result = "node";
		IConfigurationElement configurationElement = (IConfigurationElement)child.getMetaData("configurationElement");
		if (configurationElement != null) {
			String label = configurationElement.getAttribute("label");
			if (label != null) {
				result = label;
			}
		}
		int runner = 1;
		while (true) {
			if (getNodeByName(result + runner, container) == null) break;
			runner++;
		}
		return result + runner;
	}
	
	private static Node getNodeByName(String name, Container container) {
		Node result = null;
		List<Node> nodes = container.getNodes();
		for (Node node : nodes) {
			if (name.equals(node.getName())) {
				result = node;
			}
			if (node instanceof Container) {
				result = getNodeByName(name, (Container)node);
			}
			if (result != null) {
				break;
			}
		}
		return result;
	}
	
}
