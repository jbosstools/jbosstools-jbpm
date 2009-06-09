package org.jbpm.gd.common.notation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.jbpm.gd.jpdl.Logger;


public class NotationElementFactory {
	
	private static Map NOTATION_ELEMENT_REGISTRY;
	
	private static Map getNotationElementRegistry() {
		if (NOTATION_ELEMENT_REGISTRY == null) {
			initializeNotationElementRegistry();
		}
		return NOTATION_ELEMENT_REGISTRY;
	}
	
	private static void initializeNotationElementRegistry() {
		NOTATION_ELEMENT_REGISTRY = new HashMap();
		IConfigurationElement[] configurationElements = 
			Platform.getExtensionRegistry().getConfigurationElementsFor("org.jbpm.gd.jpdl.notationElements");
		for (int i = 0; i < configurationElements.length; i++) {
			IConfigurationElement element = configurationElements[i];
			if (element.getName().equals("notationElement")) {
				String key = element.getNamespaceIdentifier() + "." + element.getAttribute("id");
				NOTATION_ELEMENT_REGISTRY.put(key, element);
			}
		}
	}

	private Map notationRegistry = new HashMap();
	
	public AbstractNotationElement create(String elementType) {
		AbstractNotationElement result = null;
		IConfigurationElement element = (IConfigurationElement)getNotationElementRegistry().get(elementType);
		if (element != null) {
			try {
				result = (AbstractNotationElement)element.createExecutableExtension("class");
			} catch (CoreException e) {
				Logger.logError("Problem while instantiating notation element for " + elementType, e);
			}
		}
		if (result != null) {
			result.setFactory(this);
		}
		return result;
	}
	
	void register(AbstractNotationElement notationElement) {
		notationRegistry.put(notationElement.getSemanticElement(), notationElement);
	}
	
	void unregister(AbstractNotationElement notationElement) {
		notationRegistry.remove(notationElement.getSemanticElement());
	}
	
	public AbstractNotationElement getRegisteredNotationElementFor(Object semanticElement) {
		return (AbstractNotationElement)notationRegistry.get(semanticElement);
	}
	
}
