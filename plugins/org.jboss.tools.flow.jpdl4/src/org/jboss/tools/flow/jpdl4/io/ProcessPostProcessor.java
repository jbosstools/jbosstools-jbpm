/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.ArrayList;

import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.FlowWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.Logger;

class ProcessPostProcessor implements PostProcessor {

	@SuppressWarnings("unchecked")
	public void postProcess(Wrapper wrapper) {
		if (!(wrapper instanceof FlowWrapper)) return;
		FlowWrapper flowWrapper = (FlowWrapper)wrapper;
		for (NodeWrapper source : flowWrapper.getNodeWrappers()) {
			ArrayList<ConnectionWrapper> flows = (ArrayList<ConnectionWrapper>)source.getElement().getMetaData("flows");
			if (flows == null) continue;
			for (ConnectionWrapper connectionWrapper : flows) {
				String to = (String)connectionWrapper.getElement().getMetaData("to");
				if (to == null) {
					Logger.logInfo("Ignoring sequenceflow without target");	
					continue;
				}
				NodeWrapper target = getNamedNode(to, flowWrapper);
				if (target == null) {
					Logger.logInfo("Ignoring unknown target " + to + " while resolving sequenceflow target.");
					continue;
				}
				connectionWrapper.connect(source, target);
			}
		}
	}
	
	NodeWrapper getNamedNode(String name, FlowWrapper flowWrapper) {
		if (name == null) return null;
		for (NodeWrapper nodeWrapper : flowWrapper.getNodeWrappers()) {
			if (name.equals(nodeWrapper.getName())) return nodeWrapper;
		}
		return null;
	}
	

}