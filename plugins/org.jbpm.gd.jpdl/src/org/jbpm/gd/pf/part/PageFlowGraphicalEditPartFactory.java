package org.jbpm.gd.pf.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.jbpm.gd.common.command.AbstractEdgeDeleteCommand;
import org.jbpm.gd.common.command.AbstractNodeCreateCommand;
import org.jbpm.gd.common.command.AbstractNodeDeleteCommand;
import org.jbpm.gd.common.notation.Edge;
import org.jbpm.gd.common.notation.Label;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.RootContainer;
import org.jbpm.gd.common.part.EdgeGraphicalEditPart;
import org.jbpm.gd.common.part.NodeGraphicalEditPart;
import org.jbpm.gd.common.part.RootContainerGraphicalEditPart;
import org.jbpm.gd.common.part.LabelGraphicalEditPart;
import org.jbpm.gd.common.policy.ComponentEditPolicy;
import org.jbpm.gd.common.policy.ConnectionEditPolicy;
import org.jbpm.gd.common.policy.GraphicalNodeEditPolicy;
import org.jbpm.gd.common.policy.XYLayoutEditPolicy;
import org.jbpm.gd.pf.command.EdgeDeleteCommand;
import org.jbpm.gd.pf.command.NodeCreateCommand;
import org.jbpm.gd.pf.command.NodeDeleteCommand;
import org.jbpm.gd.pf.policy.NodeGraphicalNodeEditPolicy;

public class PageFlowGraphicalEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		if (model == null) return null;
		if (model instanceof RootContainer) {
			return createRootContainerGraphicalEditPart(model);
		} else if (model instanceof Node){
			return createNodeGraphicalEditPart(model);
		} else if (model instanceof Edge) {
			return createEdgeGraphicalEditPart(model);
		} else if (model instanceof Label) {
			return createLabelGraphicalEditPart(model);
		}
		return null;
	}

	private EditPart createNodeGraphicalEditPart(Object model) {
		return new NodeGraphicalEditPart((Node)model) {
			protected ComponentEditPolicy createComponentEditPolicy() {
				return new ComponentEditPolicy() {
					protected AbstractNodeDeleteCommand createDeleteCommand() {
						return new NodeDeleteCommand();
					}					
				};
			}
			protected GraphicalNodeEditPolicy createGraphicalNodeEditPolicy() {
				return new NodeGraphicalNodeEditPolicy();
			}			 
		};
	}

	private EditPart createLabelGraphicalEditPart(Object model) {
		return new LabelGraphicalEditPart((Label)model);
	}
	
	private EditPart createRootContainerGraphicalEditPart(Object model) {
		return new RootContainerGraphicalEditPart((RootContainer)model) {
			protected XYLayoutEditPolicy createLayoutEditPolicy() {
				return new XYLayoutEditPolicy() {
					protected AbstractNodeCreateCommand createNodeCreateCommand() {
						return new NodeCreateCommand();
					}					
				};
			}			
		};
	}
	
	private EditPart createEdgeGraphicalEditPart(Object model) {
		return new EdgeGraphicalEditPart((Edge)model) {
			protected ConnectionEditPolicy getConnectionEditPolicy() {
				return new ConnectionEditPolicy() {
					protected AbstractEdgeDeleteCommand createDeleteCommand() {
						return new EdgeDeleteCommand();
					}				
				};
			}			
		};
	}

}
