package org.jbpm.gd.jpdl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

public class AssignmentTypeHelper {
	
	private static HashMap INPUT_TYPE_MAP;
	private static TreeMap LABEL_MAP;
	private static HashMap CONFIG_INFO_MAP = new HashMap();
	private static IConfigurationElement[] ALL_ELEMENTS;
	
	private static HashMap getInputTypeMap() {
		if (INPUT_TYPE_MAP == null) {
			initializeMaps();
		}
		return INPUT_TYPE_MAP;
	}
	
	public static Map getLabelMap() {
		if (LABEL_MAP == null) {
			initializeMaps();
		}
		return LABEL_MAP;
	}
	
	private static void initializeMaps() {
		INPUT_TYPE_MAP = new HashMap();
		LABEL_MAP = new TreeMap();
		HashSet allElements = new HashSet();
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint("org.jbpm.gd.jpdl.assignmentType");
		IExtension[] extensions = extensionPoint.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] types = extensions[i].getConfigurationElements();
			for (int j = 0; j < types.length; j++) {
				String input = types[j].getAttribute("input");
				HashSet set = (HashSet)INPUT_TYPE_MAP.get(input);
				if (set == null) {
					set = new HashSet();
					INPUT_TYPE_MAP.put(input, set);
				}
				set.add(types[j]);
				String label = types[j].getAttribute("label");
				set = (HashSet)LABEL_MAP.get(label);
				if (set == null) {
					set = new HashSet();
					LABEL_MAP.put(label, set);
				}
				set.add(types[j]);
				allElements.add(types[j]);
			}
		}
		ALL_ELEMENTS = (IConfigurationElement[])allElements.toArray(new IConfigurationElement[allElements.size()]);
	}
	
	private static Class[] getClassesAndInterfaces(Object object) {
		ArrayList list = new ArrayList();
		Class clazz = object.getClass();
		while (clazz != null) {
			list.add(clazz);
			ArrayList interfacesList = new ArrayList();
			Class[] interfaces = clazz.getInterfaces();
			for (int i = 0; i< interfaces.length; i++) {
				interfacesList.add(interfaces[i]);
			}
			list.addAll(interfacesList);
			clazz = clazz.getSuperclass();
		}
		return (Class[])list.toArray(new Class[list.size()]);
	}
	
	private static IConfigurationElement[] initializeConfigurationElements(Object object) {
		HashSet result = new HashSet();
		Class[] classes = getClassesAndInterfaces(object);
		for (int i = 0; i < classes.length; i++) {
			HashSet set = (HashSet)getInputTypeMap().get(classes[i].getName());
			if (set != null) {
				result.addAll(set);
			}
		}
		return (IConfigurationElement[])result.toArray(new IConfigurationElement[result.size()]);
	}
	
	public static IConfigurationElement[] getConfigurationElements(Object object) {
		IConfigurationElement[] result = (IConfigurationElement[])CONFIG_INFO_MAP.get(object.getClass().getName());
		if (result == null) {
			result = initializeConfigurationElements(object);
			CONFIG_INFO_MAP.put(object.getClass().getName(), result);
		}
		return result;
	}
	
	public static IConfigurationElement[] getConfigurationElements() {
		if (ALL_ELEMENTS == null) {
			initializeMaps();
		}
		return ALL_ELEMENTS;
	}
	
}
