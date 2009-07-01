/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.draw2d.geometry.Rectangle;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;
import org.jboss.tools.flow.jpdl4.model.EventListenerContainer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class NodeDeserializer extends AbstractElementDeserializer {

	public void deserializeAttributes(Wrapper wrapper, Element element) {
		super.deserializeAttributes(wrapper, element);
		if (!(wrapper instanceof NodeWrapper)) return;
		NodeWrapper nodeWrapper = (NodeWrapper)wrapper;
		addGraphics(nodeWrapper, element);
		nodeWrapper.setName(element.getAttribute("name"));			
	}
	
	void addGraphics(NodeWrapper wrapper, Element element) {
		String graphics = element.getAttribute("g");
		Rectangle constraint = new Rectangle(0, 0, 80, 40);
		if (graphics != null) {
			StringTokenizer tokenizer = new StringTokenizer(graphics, ",");
			if (tokenizer.countTokens() != 4) {
				Logger.logInfo(
						"Wrong info in attribute 'g' for element '" + 
						element.getNodeName() + "'" +
						" with name '" +
						element.getAttribute("name") +
						"'. Using defaults." );
			} else {
				constraint.x = convertStringToInt(tokenizer.nextToken());
				constraint.y = convertStringToInt(tokenizer.nextToken());
				constraint.width = convertStringToInt(tokenizer.nextToken());
				constraint.height = convertStringToInt(tokenizer.nextToken());
			}
		}
		wrapper.setConstraint(constraint);
	}
	
	@SuppressWarnings("unchecked")
	public Wrapper deserializeChildNode(Wrapper parent, Node node) {
		Wrapper result = null;
		if (!(parent instanceof NodeWrapper)) return result;
		NodeWrapper nodeWrapper = (NodeWrapper)parent;
		ArrayList<ConnectionWrapper> flows = (ArrayList<ConnectionWrapper>)nodeWrapper.getElement().getMetaData("flows");
		if (flows == null) {
			flows = new ArrayList<ConnectionWrapper>();
			nodeWrapper.getElement().setMetaData("flows", flows);
		}
		if (node instanceof Element) {
			result = Registry.createWrapper((Element)node);
			if (result != null) {
				if (result instanceof ConnectionWrapper) {
					flows.add((ConnectionWrapper)result);
				} else if (result.getElement() instanceof EventListenerContainer) {
					parent.addChild("eventListener", result);
				}
			}
		}
		return result;
	}

	
}