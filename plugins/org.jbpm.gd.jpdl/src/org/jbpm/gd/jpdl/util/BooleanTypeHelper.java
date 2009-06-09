package org.jbpm.gd.jpdl.util;

public class BooleanTypeHelper {
	
	public static boolean booleanValue(String string) {
		return ("true".equals(string) || "yes".equals(string) || "on".equals(string));
	}

}
