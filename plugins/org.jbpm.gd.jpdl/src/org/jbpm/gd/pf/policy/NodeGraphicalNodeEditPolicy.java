/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jbpm.gd.pf.policy;

import org.jbpm.gd.common.command.AbstractEdgeCreateCommand;
import org.jbpm.gd.common.command.AbstractEdgeMoveCommand;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.common.policy.GraphicalNodeEditPolicy;
import org.jbpm.gd.pf.command.EdgeCreateCommand;
import org.jbpm.gd.pf.command.EdgeMoveCommand;
import org.jbpm.gd.pf.model.StartPage;

public class NodeGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	protected boolean canStart() {
		return true;
	}
	
	protected boolean canStop() {
		SemanticElement semanticElement = ((NotationElement)getHost().getModel()).getSemanticElement();
		if (semanticElement instanceof StartPage) {
			return false;
		} else {
			return true;
		}
	}
	
	protected AbstractEdgeCreateCommand createEdgeCreateCommand() {
		return new EdgeCreateCommand();
	}

	protected AbstractEdgeMoveCommand createEdgeMoveCommand() {
		return new EdgeMoveCommand();
	}
}
