package org.jboss.tools.flow.common.registry;

import java.util.HashMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class LanguageRegistry {
	
	private static final String languagesExtensionPointId = "org.jboss.tools.flow.common.languages";
	private static HashMap<String, String> languageMap = null;
	
	private static void initializeRegistry() {
		languageMap = new HashMap<String, String>();
		IConfigurationElement[] configurationElements = 
			Platform.getExtensionRegistry().getConfigurationElementsFor(languagesExtensionPointId);
		for (IConfigurationElement configElement: configurationElements) {
			String id = configElement.getAttribute("id");
			String editor = configElement.getAttribute("editor");
			if (id != null && editor != null) {
				languageMap.put(editor, id);
			}
		}
	}
	
	public static boolean isLanguageRegisteredFor(String editorId) {
		if (languageMap == null) {
			initializeRegistry();
		}
		return languageMap.containsKey(editorId);
	}
	
	public static String getLanguageRegisteredFor(String editorId) {
		if (languageMap == null) {
			initializeRegistry();
		}
		return languageMap.get(editorId);
	}

}
