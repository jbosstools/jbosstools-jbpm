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
package org.jbpm.gd.pf.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.pf.model.NodeElement;
import org.jbpm.gd.pf.model.PageFlowDefinition;
import org.jbpm.gd.pf.model.StartPage;

public class PageFlowDefinitionOutlineEditPart extends OutlineEditPart {

	public PageFlowDefinitionOutlineEditPart(PageFlowDefinition pageFlowDefinition) {
		super(pageFlowDefinition);
	}

	private PageFlowDefinition getPageFlowDefinition() {
		return (PageFlowDefinition) getModel();
	}

	protected String getText() {
		return getPageFlowDefinition().getName();
	}

	protected List getModelChildren() {
		List result = new ArrayList();
		StartPage startPage = getPageFlowDefinition().getStartPage();
		if (startPage != null) {
			result.add(startPage);
		}
		NodeElement[] nodeElements = getPageFlowDefinition().getNodeElements();
		for (int i = 0; i < nodeElements.length; i++) {
			if (!result.contains(nodeElements[i])) {
				result.add(nodeElements[i]);
			}
		}
		return result;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String eventName = evt.getPropertyName();
		if (eventName.equals("nodeElementAdd") || eventName.equals("startPageAdd")) {
			handleChildAdd(evt.getNewValue());
		} else if (eventName.equals("nodeElementRemove") || eventName.equals("startPageRemove")) {
			refreshChildren();
			getViewer().select(this);
		} else {
			super.propertyChange(evt);
		}
	}
	
}
