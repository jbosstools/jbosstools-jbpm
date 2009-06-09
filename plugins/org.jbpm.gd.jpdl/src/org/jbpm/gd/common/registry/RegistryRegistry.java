package org.jbpm.gd.common.registry;

import java.util.HashMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.jbpm.gd.jpdl.Logger;

public class RegistryRegistry {
	
	private static HashMap editorRegistry = new HashMap();
	private static HashMap semanticElementRegistries = new HashMap();
	private static HashMap xmlAdapterRegistries = new HashMap();
	
	static {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint("org.jbpm.gd.jpdl.dsl");
		IExtension[] extensions = extensionPoint.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++) {
				String editorId = configElements[j].getAttribute("editor");
				String language = configElements[j].getAttribute("id");
				editorRegistry.put(editorId, language);
			}
		}				
	}
		
	public static SemanticElementRegistry getSemanticElementRegistry(String editorId) throws RuntimeException {
		SemanticElementRegistry result = (SemanticElementRegistry)semanticElementRegistries.get(editorId);
		if (result == null) {
			String language = (String)editorRegistry.get(editorId);
			if (language != null) {
				result = new SemanticElementRegistry(language);
			}
		}
		if (result == null) {
			handleError(editorId);
		}
		return result;
	}

	private static void handleError(String editorId) {
		String message = "Editor " + editorId + " has no associated language";
		RuntimeException e = new RuntimeException(message);
		Logger.logError(message, e);
		throw e;
	}
	
	public static XmlAdapterRegistry getXmlAdapterRegistry(String editorId) throws RuntimeException {
		XmlAdapterRegistry result = (XmlAdapterRegistry)xmlAdapterRegistries.get(editorId);
		if (result == null) {
			String language = (String)editorRegistry.get(editorId);
			if (language != null) {
				SemanticElementRegistry semanticElementRegistry = getSemanticElementRegistry(editorId);
				if (semanticElementRegistry != null) {
					result = new XmlAdapterRegistry(semanticElementRegistry.getSemanticElementIds());
				}
			}
		}
		if (result == null) {
			handleError(editorId);		}
		return result;
	}
	
}
