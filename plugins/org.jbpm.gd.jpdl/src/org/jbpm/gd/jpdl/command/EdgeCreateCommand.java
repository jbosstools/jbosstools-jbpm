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
package org.jbpm.gd.jpdl.command;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.gd.common.command.AbstractEdgeCreateCommand;
import org.jbpm.gd.common.model.NamedElement;
import org.jbpm.gd.common.notation.Node;
import org.jbpm.gd.common.notation.NotationElement;
import org.jbpm.gd.jpdl.model.NodeElement;
import org.jbpm.gd.jpdl.model.Transition;

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
		List sourcePath = getPathToRootFrom(source);
		List targetPath = getPathToRootFrom(target);
		NotationElement common = findFirstCommonElement(sourcePath, targetPath);
		getTransition().setTo(getPrefix(sourcePath, common) + getSuffix(common, targetPath));
	}
	
	private List getPathToRootFrom(NotationElement notationElement) {
		List result = new ArrayList();
		while (notationElement != null) {
			result.add(notationElement);
			if (notationElement instanceof Node) {
				notationElement = (NotationElement)((Node)notationElement).getContainer();
			} else {
				notationElement = null;
			}
		}
		return result;
	}
	
	private NotationElement findFirstCommonElement(List sourcePath, List targetPath) {
		NotationElement result = null;
		int i = 1;
		while (i <= sourcePath.size() && i <= targetPath.size()) {
			if (sourcePath.get(sourcePath.size() - i) == targetPath.get(targetPath.size() - i)) {
				result = (NotationElement)sourcePath.get(sourcePath.size() - i);
			}
			i++;
		}
		return result;
	}
	
	private String getPrefix(List sourcePath, NotationElement common) {
		StringBuffer result = new StringBuffer("");
		if (source != target) {
			int i = 1;
			while (i < sourcePath.size() && sourcePath.get(i) != common) {
				result.append("../");
				i++;
			}
		}
		return result.toString();
	}
	
	private String getSuffix(NotationElement common, List targetPath) {
		StringBuffer result = new StringBuffer();
		if (source != target) {
			int i = 0;
			while (i < targetPath.size() && targetPath.get(i) != common) {
				result.insert(0, getSemanticElementName((NotationElement)targetPath.get(i++)));
				if (i < targetPath.size() && targetPath.get(i) != common) {
					result.insert(0, "/");
				}
			}
		} else {
			result.append(getSemanticElementName(common));
		}
		return result.toString();
	}
	
	private String getSemanticElementName(NotationElement notationElement) {
		return ((NamedElement)notationElement.getSemanticElement()).getName();
	}
	
	private NodeElement getTransitionSource() {
		return (NodeElement)source.getSemanticElement();
	}
	
	private Transition getTransition() {
		return (Transition)edge.getSemanticElement();
	}
	
}
