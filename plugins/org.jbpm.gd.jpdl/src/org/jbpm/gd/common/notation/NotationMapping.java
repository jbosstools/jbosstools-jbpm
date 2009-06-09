package org.jbpm.gd.common.notation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.geometry.Dimension;

public class NotationMapping {

	private static Map NOTATION_MAPPINGS;
	private static HashMap NOTATION_ELEMENT_MAP = new HashMap();
	private static HashMap INITIAL_DIMENSION_MAP = new HashMap();
	private static HashMap HIDE_NAME_MAP = new HashMap();
	
	private static Map getNotationMappings() {
		if (NOTATION_MAPPINGS == null) {
			initializeNotationMappings();
		}
		return NOTATION_MAPPINGS;
	}
	
	private static void initializeNotationMappings() {
		NOTATION_MAPPINGS = new HashMap();
		IConfigurationElement[] configurationElements =
			Platform.getExtensionRegistry().getConfigurationElementsFor("org.jbpm.gd.jpdl.notationMappings");
		for (int i = 0; i < configurationElements.length; i++) {
			if (configurationElements[i].getName().equals("mapping")) {
				NOTATION_MAPPINGS.put(
						configurationElements[i].getAttribute("semanticElement"), 
						configurationElements[i]);
			}
		}
	}
	
	public static String getNotationElementId(String semanticElementId) {
		if (!NOTATION_ELEMENT_MAP.containsKey(semanticElementId)) {
			IConfigurationElement element = (IConfigurationElement)getNotationMappings().get(semanticElementId);
			NOTATION_ELEMENT_MAP.put(semanticElementId, element.getAttribute("notationElement"));
		}
		return (String)NOTATION_ELEMENT_MAP.get(semanticElementId);
	}
	
	public static Dimension getInitialDimension(String semanticElementId) {
		if (!INITIAL_DIMENSION_MAP.containsKey(semanticElementId)) {
			processFigureElement(semanticElementId);
		}
		return (Dimension)INITIAL_DIMENSION_MAP.get(semanticElementId);
	}

	private static void processFigureElement(String semanticElementId) {
		IConfigurationElement[] children = 
			((IConfigurationElement)getNotationMappings().get(semanticElementId)).getChildren("figure");
		Dimension dimension = null;
		Boolean hideName = Boolean.FALSE;
		if (children.length == 1) {
			dimension = 
				new Dimension(
						Integer.parseInt(children[0].getAttribute("width")),
						Integer.parseInt(children[0].getAttribute("height")));
			hideName = new Boolean(children[0].getAttribute("hideName") == null ? "false" : children[0].getAttribute("hideName"));
		}
		INITIAL_DIMENSION_MAP.put(semanticElementId, dimension);	
		HIDE_NAME_MAP.put(semanticElementId, hideName);
	}
	
	public static boolean hideName(String semanticElementId) {
		if (!HIDE_NAME_MAP.containsKey(semanticElementId)) {
			processFigureElement(semanticElementId);
		}
		return ((Boolean)HIDE_NAME_MAP.get(semanticElementId)).booleanValue();
	}
	
}
