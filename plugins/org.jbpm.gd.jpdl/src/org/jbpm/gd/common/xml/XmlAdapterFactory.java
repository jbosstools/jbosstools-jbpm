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
package org.jbpm.gd.common.xml;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.sse.core.internal.provisional.AbstractAdapterFactory;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.model.SemanticElementFactory;
import org.jbpm.gd.common.registry.RegistryRegistry;
import org.jbpm.gd.common.registry.XmlAdapterRegistry;
import org.jbpm.gd.jpdl.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XmlAdapterFactory extends AbstractAdapterFactory {

	private SemanticElementFactory semanticElementFactory;
	private XmlAdapterRegistry xmlAdapterRegistry;
	private Map adapterRegistry = new HashMap();	
	private Document document;
	
	public XmlAdapterFactory(Document document, SemanticElementFactory elementFactory) {
		super();
		this.document = document;
		this.semanticElementFactory = elementFactory;
		xmlAdapterRegistry = RegistryRegistry.getXmlAdapterRegistry(elementFactory.getEditorId());
		setAdapterKey(this);
		setShouldRegisterAdapter(true);
	}
	
	public XmlAdapter adapt(Node node) {
		return (XmlAdapter)super.adapt((INodeNotifier)node);
	}
	
	protected INodeAdapter createAdapter(INodeNotifier target) {
		if (document == null) throw new RuntimeException("The document property of the JpdlElementDomAdapterFactory is not initialized");
		XmlAdapter result = null;
		try {
			IConfigurationElement element = xmlAdapterRegistry.getConfigurationElementByXmlNode((Node)target);
			if (element != null) {
				result = (XmlAdapter)element.createExecutableExtension("adapterClass");
			} else if (((Node)target).getNodeType() != Node.TEXT_NODE){
				result = new GenericElementXmlAdapter();
			}
			if (result != null) {
				result.setNode((Node)target);
				result.setFactory(this);
			}
		} catch (CoreException e) {
			throw new RuntimeException("Creation of executable extension failed", e);
		}
		return result;
	}
	
	private String calculateElementName(IConfigurationElement configurationElement, SemanticElement semanticElement) {
		String elementName = configurationElement.getAttribute("xmlElement");
		if (elementName == null) {
			String nameProviderClass = configurationElement.getAttribute("nameProvider");
			if (nameProviderClass != null) {
				try {
					XmlAdapterNameProvider nameProvider = (XmlAdapterNameProvider)configurationElement.createExecutableExtension("nameProvider");
					if (nameProvider != null) {
						elementName = nameProvider.getName(semanticElement);
					}
				}
				catch (CoreException e) {
					Logger.logError("Problem creating nameProvider for " + semanticElement.getElementId(), e);
				}
			}
		}
		return elementName;
	}
	
	private XmlAdapter createAdapter(IConfigurationElement configurationElement, String elementName) {
		XmlAdapter result = null;
		try {
			result = (XmlAdapter) configurationElement.createExecutableExtension("adapterClass");
			INodeNotifier element = (INodeNotifier)document.createElement(elementName);
			element.addAdapter(result);
			result.setFactory(this);
			result = (XmlAdapter)adapt(element);
			result.setNode((Node)element);
		} catch (CoreException e) {
		    Logger.logError("Unable to create XML Adapter for " + elementName, e);
		}
		return result;
	}
	
	public XmlAdapter createAdapterFromModel(SemanticElement semanticElement) {
		IConfigurationElement configurationElement = 
			xmlAdapterRegistry.getConfigurationElementBySemanticElementId(semanticElement.getElementId());
		String elementName = calculateElementName(configurationElement, semanticElement);
		if (elementName != null) {
			return createAdapter(configurationElement, elementName);
		} else {
			return null;
		}
	}
	
	void register(XmlAdapter jpdlElementDomAdapter) {
		adapterRegistry.put(jpdlElementDomAdapter.getSemanticElement(), jpdlElementDomAdapter);
	}
	
	void unregister(XmlAdapter jpdlElementDomAdapter) {
		adapterRegistry.remove(jpdlElementDomAdapter.getSemanticElement());
	}
	
	XmlAdapter getRegisteredAdapterFor(SemanticElement jpdlElement) {
		return (XmlAdapter)adapterRegistry.get(jpdlElement);
	}
	
	SemanticElementFactory getSemanticElementFactory() {
		return semanticElementFactory;
	}
	
	XmlAdapterRegistry getXmlAdapterRegistry() {
		return xmlAdapterRegistry;
	}
	
}
