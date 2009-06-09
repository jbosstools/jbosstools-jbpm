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
package org.jbpm.gd.common.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;


public class PaletteRoot extends org.eclipse.gef.palette.PaletteRoot {
	
	private Editor editor;
	
	private List categoryList = new ArrayList();
	private Map categoryMap = new HashMap();
	
	public PaletteRoot(Editor editor) {
		this.editor = editor;
		addControls();	
	}
	
	private void addControls() {
		add(createDefaultControls());
		IConfigurationElement[] configurationElements = 
			Platform.getExtensionRegistry().getConfigurationElementsFor("org.jbpm.gd.jpdl.palette");
		for (int i = 0; i < configurationElements.length; i++) {
			String editorClass = configurationElements[i].getAttribute("editorClass");
			if (editorClass != null && editorClass.equals(editor.getClass().getName())) {
				processPaletteContribution(configurationElements[i]);
			}
		}
		for (int i = 0; i < categoryList.size(); i++) {
			IConfigurationElement categoryElement = (IConfigurationElement)categoryList.get(i);
			String categoryName = categoryElement.getNamespaceIdentifier() + "." + categoryElement.getAttribute("id");
			List entries = (List)categoryMap.get(categoryName);
			if (entries != null && !entries.isEmpty()) {
				PaletteGroup paletteGroup = new PaletteGroup(categoryName);
				for (int j = 0; j < entries.size(); j++) {
					IConfigurationElement entryElement = (IConfigurationElement)entries.get(j);
					String entryName = entryElement.getNamespaceIdentifier() + "." + entryElement.getAttribute("id");
					String label = entryElement.getAttribute("label");
					boolean isNode = !"false".equals(entryElement.getAttribute("node"));
					String object = entryElement.getAttribute("object");
					String tooltip = entryElement.getAttribute("tooltip");
					PaletteEntry entry;
					CreationFactory factory = new CreationFactory(object, editor.getSemanticElementFactory(), editor.getNotationElementFactory());
					if (isNode) {
						entry = new CreationToolEntry(label, tooltip, factory, getIconDescriptor(entryElement), null);
					} else {
						entry = new ConnectionCreationToolEntry(label, tooltip, factory, getIconDescriptor(entryElement), null);
					}
					entry.setId(entryName);
					paletteGroup.add(entry);
				}
				add(paletteGroup);
			}
		}
	}
	
	private ImageDescriptor getIconDescriptor(IConfigurationElement element) {
		Bundle bundle = Platform.getBundle(element.getDeclaringExtension().getContributor().getName());
		return ImageDescriptor.createFromURL(bundle.getEntry("/" + element.getAttribute("icon")));
	}
	
	private void processPaletteContribution(IConfigurationElement configurationElement) {
		IConfigurationElement[] elements;
		elements = configurationElement.getChildren("category");
		for (int i = 0; i < elements.length; i++) {
			categoryList.add(elements[i]);
		}
		elements = configurationElement.getChildren("entry");
		for (int i = 0; i < elements.length; i++) {
			List list = (List)categoryMap.get(elements[i].getAttribute("category"));
			if (list == null) {
				list = new ArrayList();
				categoryMap.put(elements[i].getAttribute("category"), list);
			}
			list.add(elements[i]);
		}
	}

	private PaletteGroup createDefaultControls() {
		PaletteGroup controls = new PaletteGroup("Default Tools");
		controls.setId("org.jbpm.palette.DefaultTools");
		addSelectionTool(controls);
		addMarqueeTool(controls);
		return controls;
	}
	
	private void addMarqueeTool(PaletteGroup controls) {
		ToolEntry tool = new MarqueeToolEntry();
		tool.setId("org.jbpm.ui.palette.Marquee");
		controls.add(tool);
	}

	private void addSelectionTool(PaletteGroup controls) {
		ToolEntry tool = new SelectionToolEntry();
		tool.setId("org.jbpm.ui.palette.Selection");
		controls.add(tool);
		setDefaultEntry(tool);
	}

}
