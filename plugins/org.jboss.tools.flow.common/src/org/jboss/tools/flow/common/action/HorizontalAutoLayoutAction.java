package org.jboss.tools.flow.common.action;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Map;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.flow.common.Activator;

/**
 * Action for auto layouting a process.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class HorizontalAutoLayoutAction extends VerticalAutoLayoutAction {

	public static final String ID = "org.jboss.tools.process.editor.action.HorizontalAutoLayoutAction";
	
	public HorizontalAutoLayoutAction(GraphicalViewer diagramViewer) {
		super(diagramViewer);
	}
	
	protected void initialize() {
		setId(ID);
		setText("Auto Layout (Horizontal)");
		setImageDescriptor(
				ImageDescriptor.createFromURL(
						Activator.getDefault().getBundle().getEntry("icons/layoutH.gif")));
		setToolTipText("Auto layout the process (horizontal)");
	}

    protected DirectedGraph createDirectedGraph(Map<String, Node> mapping) {
        DirectedGraph graph = super.createDirectedGraph(mapping);
        graph.setDirection(PositionConstants.HORIZONTAL);
        return graph;
    }

}
