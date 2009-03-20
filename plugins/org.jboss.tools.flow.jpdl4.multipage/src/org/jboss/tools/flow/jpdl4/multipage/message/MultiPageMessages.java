/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.flow.jpdl4.multipage.message;

import org.eclipse.osgi.util.NLS;

/**
 * @author Grid Qian
 */
public class MultiPageMessages {

	private static final String BUNDLE_NAME = "org.jboss.tools.flow.jpdl4.multipage.message.MultiPage"; //$NON-NLS-1$

	public static String Jpdl_Page_Name;
	public static String Xml_Page_Name;

	static {
		NLS.initializeMessages(BUNDLE_NAME, MultiPageMessages.class);
	}
}