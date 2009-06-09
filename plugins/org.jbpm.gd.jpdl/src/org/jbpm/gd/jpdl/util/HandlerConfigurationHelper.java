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
package org.jbpm.gd.jpdl.util;

import org.jbpm.gd.jpdl.Constants;

public class HandlerConfigurationHelper implements Constants {

	public static String configStringFor(String xml) {
		if ("bean".equals(xml)) return BEAN_CONFIG_TYPE;
		if ("constructor".equals(xml)) return CONSTRUCTOR_CONFIG_TYPE;
		if ("configuration-property".equals(xml)) return COMPATIBILITY_CONFIG_TYPE;
		return FIELD_CONFIG_TYPE;
	}
	
	public static String configXMLFor(String string) {
		if (string == null) return null;
		if (COMPATIBILITY_CONFIG_TYPE.equals(string)) return "configuration-property";
		return string.toLowerCase();
	}
	
}
