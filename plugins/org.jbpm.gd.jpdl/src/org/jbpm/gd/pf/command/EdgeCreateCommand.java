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
package org.jbpm.gd.pf.command;

import org.jbpm.gd.common.command.AbstractEdgeCreateCommand;
import org.jbpm.gd.pf.model.NodeElement;
import org.jbpm.gd.pf.model.Transition;

public class EdgeCreateCommand extends AbstractEdgeCreateCommand {
	
	public void execute() {
		super.execute();
		initializeTransitionAttributes();
		getTransition().setSource(getTransitionSource());
		getTransitionSource().addTransition(getTransition());
	}
	
	public void undo() {
		getTransitionSource().removeTransition(getTransition());
		getTransition().setSource(null);
	}
	
	private void initializeTransitionAttributes() {
		initializeToAttribute();
		if (getTransitionSource().getTransitions().length > 0) {
			getTransition().setName("to " + getTransition().getTo());
			edge.getLabel().setText(getTransition().getName());
		}
	}
	
	private void initializeToAttribute() {
		getTransition().setTo(((NodeElement)target.getSemanticElement()).getName());
	}
	
	private NodeElement getTransitionSource() {
		return (NodeElement)source.getSemanticElement();
	}
	
	private Transition getTransition() {
		return (Transition)edge.getSemanticElement();
	}
	
}
