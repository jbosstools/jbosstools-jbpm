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

package org.jboss.tools.jbpm.convert.b2j.messages;

import org.eclipse.osgi.util.NLS;

/**
 * @author Grid Qian
 */
public class B2JMessages {

	private static final String BUNDLE_NAME = "org.jboss.tools.jbpm.convert.b2j.messages.B2J"; //$NON-NLS-1$

	public static String Label_Select_All;
	public static String Label_Deselect_All;
	public static String Label_Button_Browse;
	public static String Bpmn_Wizard_Title;
	public static String Bpmn_File_Choose_WizardPage_Name;
	public static String Bpmn_File_Choose_WizardPage_Title;
	public static String Bpmn_File_Choose_WizardPage_Message;
	public static String Bpmn_File_Choose_WizardPage_ViewerTitle;
	public static String Bpmn_Pool_Choose_WizardPage_Name;
	public static String Bpmn_Pool_Choose_WizardPage_Message;
	public static String Bpmn_Pool_Choose_WizardPage_Title;
	public static String Bpmn_Pool_Choose_WizardPage_ViewerTitle;
	public static String Bpmn_GeneratedFile_Location_WizardPage_Name;
	public static String Bpmn_GeneratedFile_Location_WizardPage_Message;
	public static String Bpmn_GeneratedFile_Location_WizardPage_Title;
	public static String Bpmn_GeneratedFile_Location_WizardPage_ViewerTitle;
	public static String Bpmn_GeneratedFile_Location_WizardPage_CheckBox;
	public static String Bpmn_Translate_Message_WizardPage_Name;
	public static String Bpmn_Translate_Message_WizardPage_Title;
	public static String Bpmn_Translate_Message_WizardPage_Message;
	public static String Bpmn_Translate_Message_WizardpageViewer_Title;
	public static String Translate_Error_File_CanNotRead;

	static {
		NLS.initializeMessages(BUNDLE_NAME, B2JMessages.class);
	}
}