package org.jbpm.gd.common.registry;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.jbpm.gd.common.xml.XmlElementMapper;
import org.jbpm.gd.jpdl.Logger;
import org.w3c.dom.Node;

public class XmlAdapterRegistry {
	
	Map xmlElementMap = new HashMap();
	Map semanticElementIdMap = new HashMap();
	
	public XmlAdapterRegistry(Set elementIds) {
		initializeMaps(elementIds);
	}
	
	private void initializeMaps(Set elementIds) {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint("org.jbpm.gd.jpdl.xmlMappings");
		IExtension[] extensions = extensionPoint.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				String semanticElement = configElements[j].getAttribute("semanticElement");
				if (!elementIds.contains(semanticElement)) continue;
				semanticElementIdMap.put(semanticElement, configElements[j]);
				String xmlElement = configElements[j].getAttribute("xmlElement");
				if (xmlElement != null) {
					SortedSet set = (SortedSet)xmlElementMap.get(xmlElement);
					if (set == null) {
						set = new TreeSet(PriorityComparator.INSTANCE);
						xmlElementMap.put(xmlElement, set);
					}
					set.add(configElements[j]);
				}
			}
		}				
	}
	
	public IConfigurationElement getConfigurationElementByXmlNode(Node node) {
		String nodeName = node.getNodeName();
		SortedSet set = (SortedSet)xmlElementMap.get(nodeName);
		if (set == null) {
			return null;
		}
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			IConfigurationElement element = (IConfigurationElement)iterator.next();
			if (element.getAttribute("mapperClass") == null) {
				return element;
			}
			if (createXmlElementMapper(element).accept(node)) {
				return element;
			}
		}
		return null;
	}
	
	private XmlElementMapper createXmlElementMapper(IConfigurationElement element) {
		XmlElementMapper result = null;
		try {
			result = (XmlElementMapper)element.createExecutableExtension("mapperClass");
		} catch (CoreException e) {
			Logger.logError("Could not create mapper class", e);
		}
		return result;
	}
	
	public IConfigurationElement getConfigurationElementBySemanticElementId(String semanticElementId) {
		return (IConfigurationElement)semanticElementIdMap.get(semanticElementId);
	}
	
	private static class PriorityComparator implements Comparator {
		private static PriorityComparator INSTANCE = new PriorityComparator();
		public int compare(Object left, Object right) {
			int result = getPriority(right) - getPriority(left);
			if (result == 0) {
				result = right.hashCode() - left.hashCode();
			}
			return result;
		}
		private int getPriority(Object object) {
			int result = 0;
			String priority = ((IConfigurationElement)object).getAttribute("mapperClassPriority");
			if (priority != null) {
				result = Integer.valueOf(priority).intValue();
			}
			return result;
		}
	}

}
