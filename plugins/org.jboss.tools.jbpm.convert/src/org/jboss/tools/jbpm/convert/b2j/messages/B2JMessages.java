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
import org.jboss.tools.jbpm.convert.b2j.messages.B2JMessages;

/**
 * @author Grid Qian
 */
public class B2JMessages {

	private static final String BUNDLE_NAME = "org.jboss.tools.jbpm.convert.b2j.messages.B2J"; //$NON-NLS-1$

	private B2JMessages() {
		// Do not instantiate
	}

	public static String Loop_Decision;
	public static String To;
	public static String Folder_Name_Separator;
	public static String Space;
	public static String Underline;
	public static String Width_Attribute_Name;
	public static String Height_Attribute_Name;
	public static String X_Attribute_Name;
	public static String Y_Attribute_Name;
	public static String Label_Select_All;
	public static String Label_Deselect_All;
	public static String Label_Button_Browse;
	public static String Bpmn_Diagram_Name_Suffix;
	public static String Bpmn_Pool_Element_Name;
	public static String Bpmn_Element_ID;
	public static String Bpmn_Vertice_Element_Name;
	public static String Bpmn_SequenceFlow_Element_Name;
	public static String Bpmn_Looping_Attribute_Name;
	public static String Bpmn_InFlow_Attribute_Name;
	public static String Bpmn_XmiType_Attribute_Name;
	public static String Bpmn_Href_Attribute_Name;
	public static String Bpmn_ActivityType_Attribute_Name;
	public static String Bpmn_FlowSource_Attribute_Name;
	public static String Bpmn_FlowTarget_Attribute_Name;
	public static String Bpmn_FlowDefault_Attribute_Name;
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
	public static String Bpmn_EAnnotations_Element_Name;
	public static String Bpmn_Details_Element_Name;
	public static String Bpmn_Value_Attribute_Name;
	public static String Jpdl_Element_Decision_Suffix;
	public static String Jpdl_Element_Complete_Suffix;
	public static String Jpdl_Element_Cancel_Suffix;
	public static String Jpdl_Element_Successful_Name;
	public static String Jpdl_Suffix;
	public static String Jpdl_Process_Definition_Name;
	public static String Jpdl_ProcessState_Element_Name;
	public static String Jpdl_SubProcess_Element_Name;
	public static String Jpdl_32_Namespace_Url;
	public static String Jpdl_Process_Definition_Element_Name;
	public static String Jpdl_Transition_Element;
	public static String Jpdl_Decision_Element_Name;
	public static String Jpdl_Fork_Element_Name;
	public static String Jpdl_Join_Element_Name;
	public static String Jpdl_Start_Element_Name;
	public static String Jpdl_State_Element_Name;
	public static String Jpdl_End_Element_Name;
	public static String Jpdl_Node_Element_Name;
	public static String Gpd_Definition_Name;
	public static String Gpd_Layout_Element_Name;
	public static String Gpd_Element_Name;
	public static String Gpd_Process_Diagram_Name;
	public static String Gpd_Label_Element_Name;
	public static String Dom_Element_Name;
	public static String Dom_Element_ID;
	public static String Translate_Error_JpdlProcess_Definition_Null;
	public static String Translate_Error_JpdlFile_CanNotWrite;
	public static String Translate_Error_JpdlFile_CanNotGenerate;
	public static String Translate_Error_GpdFile_CanNotGenerate;
	public static String Translate_Error_GpdFile_CanNotWrite;
	public static String Translate_Error_File_CanNotRead;
	public static String Translate_Warning_Bpmn_Element_Name;
	public static String Translate_Warning_Bpmn_Element_Type;

	static {
		NLS.initializeMessages(BUNDLE_NAME, B2JMessages.class);
	}
}