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

package org.jboss.tools.jbpm.convert.b2j.translate;

/**
 * @author Grid Qian
 */
public class Constants {

	public static final String Loop_Decision = "Loop_Decition";
	public static final String To = "to";
	public static final String Space = " ";
	public static final String Underline = "_";
	public static final String Folder_Name_Separator = ".";
	public static final String Dom_Element_Name = "name";
	public static final String Dom_Element_ID = "id";
	public static final String Width_Attribute_Name = "width";
	public static final String Height_Attribute_Name = "height";
	public static final String X_Attribute_Name = "x";
	public static final String Y_Attribute_Name = "y";
	public static final String Bpmn_Diagram_Name_Suffix = "_diagram";
	public static final String Bpmn_Pool_Element_Name = "pools";
	public static final String Bpmn_Vertice_Element_Name = "vertices";
	public static final String Bpmn_SequenceFlow_Element_Name = "sequenceEdges";
	public static final String Bpmn_Href_Attribute_Name = "href";
	public static final String Bpmn_XmiType_Attribute_Name = "type";
	public static final String Bpmn_ActivityType_Attribute_Name = "activityType";
	public static final String Bpmn_InFlow_Attribute_Name = "incomingEdges";
	public static final String Bpmn_Looping_Attribute_Name = "looping";
	public static final String Bpmn_FlowSource_Attribute_Name = "source";
	public static final String Bpmn_FlowTarget_Attribute_Name = "target";
	public static final String Bpmn_FlowDefault_Attribute_Name = "isDefault";
	public static final String Bpmn_FlowOutgoings_Attribute_Name = "outgoingEdges";
	public static final String Bpmn_FlowIncomings_Attribute_Name = "incomingEdges";
	public static final String Bpmn_Element_ID = "iD";
	public static final String Bpmn_EAnnotations_Element_Name = "eAnnotations";
	public static final String Bpmn_Details_Element_Name = "details";
	public static final String Bpmn_Value_Attribute_Name = "value";
	public static final String Jpdl_Suffix = "jpdl";
	public static final String Jpdl_Element_Decision_Suffix = "Decision";
	public static final String Jpdl_Element_Complete_Suffix = "Complete";
	public static final String Jpdl_Element_Cancel_Suffix = "Cancel";
	public static final String Jpdl_Element_Successful_Name = "Successful?";
	public static final String Jpdl_Process_Definition_Name = "processdefinition.xml";
	public static final String Jpdl_32_Namespace_Url = "urn:jbpm.org:jpdl-3.2";
	public static final String Jpdl_Process_Definition_Element_Name = "process-definition";
	public static final String Jpdl_Transition_Element = "transition";
	public static final String Jpdl_ProcessState_Element_Name = "process-state";
	public static final String Jpdl_SubProcess_Element_Name = "sub-process";
	public static final String Jpdl_Decision_Element_Name = "decision";
	public static final String Jpdl_Fork_Element_Name = "fork";
	public static final String Jpdl_Join_Element_Name = "join";
	public static final String Jpdl_Start_Element_Name = "start-state";
	public static final String Jpdl_State_Element_Name = "state";
	public static final String Jpdl_End_Element_Name = "end-state";
	public static final String Jpdl_Node_Element_Name = "node";
	public static final String Gpd_Transition_Element = "edge";
	public static final String Gpd_Definition_Name = "gpd.xml";
	public static final String Gpd_Layout_Element_Name = "layoutConstraint";
	public static final String Gpd_Element_Name = "element";
	public static final String Gpd_Process_Diagram_Name = "root-container";
	public static final String Gpd_Label_Element_Name = "label";
	public static final String Translate_Error_File_CanNotRead = "Error: Couldn't read or parse the file to a DOM document:";
	public static final String Translate_Error_JpdlFile_CanNotGenerate = "Error: Couldn't write process definition xml: ";
	public static final String Translate_Error_JpdlFile_CanNotWrite = "Error: Couldn't write process definition to a jpdl file: ";
	public static final String Translate_Warning_Bpmn_Element_Type = "Warning: The type of this bpmn element is not translated to corresponding jpdl element: ";
	public static final String Translate_Warning_Bpmn_Element_Name = "Warning: The bpmn element's name is null or same to another element's name:";
	public static final String Translate_Error_GpdFile_CanNotGenerate = "Error: Couldn't write gpd.xml:";
	public static final String Translate_Error_JpdlProcess_Definition_Null= "Error: The JPDL process definition is null.";
	public static final String Translate_Error_GpdFile_CanNotWrite="Error: Couldn't write gpd definition to a gpd.xml:";

}
