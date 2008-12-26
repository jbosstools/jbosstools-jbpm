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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.jboss.tools.jbpm.convert.bpmnto.translate.BPMNTranslator;
import org.jboss.tools.jbpm.convert.bpmnto.util.BPMNToUtil;
import org.jboss.tools.jbpm.convert.bpmnto.util.DomXmlWriter;
import org.jboss.tools.jbpm.convert.b2j.translate.TranslateHelper;

/**
 * @author Grid Qian
 * 
 *         this is a translator for bpmn -> jpdl
 */
public class BPMN2JPDL extends BPMNTranslator {

	private Document bpmnDocument;
	private List<Document> processDefs = new ArrayList<Document>();
	
	// map: bpmn element's(activity) id to jpdl element
	private Map<String, Element> map = new HashMap<String, Element>();
	// map: bpmn outgoingEdges element's(sequenceEdges) id to bpmn element's(activity) id
	private Map<String, String> sourceMap = new HashMap<String, String>();
	// map: bpmn incomingEdges element's(sequenceEdges) id to bpmn element's(activity) id
	private Map<String, String> targetMap = new HashMap<String, String>();

	public BPMN2JPDL() {
	}
	
	public BPMN2JPDL(String bpmnFileName, String bpmnFilePath){
		super(bpmnFileName, bpmnFilePath, null);
	}

	public BPMN2JPDL(String bpmnFileName, String bpmnFilePath,
			Document bpmnDocument) {
		this(bpmnFileName, bpmnFilePath, null, bpmnDocument);
	}

	public BPMN2JPDL(String bpmnFileName, String bpmnFilePath,
			List<String> poolIDList, Document bpmnDocument) {
		super(bpmnFileName, bpmnFilePath, poolIDList);
		this.bpmnDocument = bpmnDocument;
	}

	/*
	 * Translate a bpmn diagram to string[]. Every string is jpdl process
	 * definition
	 */
	public String[] translateToStrings() {
		this.translateDiagram();
		String[] strForProcessDefs = new String[processDefs.size()];
		int i = 0;
		for (Document def : processDefs) {
			try {
				strForProcessDefs[i] = DomXmlWriter.toString(def);
			} catch (IOException e) {
				this.errors
						.add(Constants.Translate_Error_JpdlFile_CanNotGenerate
								+ e.getMessage());
			}
			i++;
		}

		return strForProcessDefs;
	}

	/*
	 * Translate a bpmn diagram to file[]. Every file is a jpdl file
	 */
	public void translateToFiles(String fileLocation) {
		String[] strForProcessDefs = translateToStrings();
		String[] jpdlFileNames = new String[processDefs.size()];

		int i = 0;
		for (Document def : processDefs) {
			jpdlFileNames[i] = def.getName();
			i++;
		}

		try {
			TranslateHelper.createFiles(fileLocation, bpmnFileName,
					strForProcessDefs, jpdlFileNames,
					Constants.Jpdl_Process_Definition_Name, false);
		} catch (Exception e) {
			errors.add(Constants.Translate_Error_JpdlFile_CanNotWrite
					+ e.getMessage());
		}
	}

	/*
	 * Translate a bpmn diagram Domument tree to some jpdl process Dom trees
	 */
	public void translateDiagram() {

		// set the namemap = null
		TranslateHelper.setNameMap(new HashMap<String, Integer>());
		
		if(bpmnDocument == null) {
			try {
				bpmnDocument = BPMNToUtil.parse(rootLocation, bpmnFileName);
			} catch (Exception e) {
				errors.add(Constants.Translate_Error_File_CanNotRead
						+ e.getMessage());
			}
		}
		
		Element diagram = bpmnDocument.getRootElement();
		if (this.poolIDList == null || this.poolIDList.size() == 0) {
			for (Object pool : diagram
					.elements(Constants.Bpmn_Pool_Element_Name)) {
				translateGraph((Element) pool);
			}
		} else {
			for (Object pool : diagram
					.elements(Constants.Bpmn_Pool_Element_Name)) {
				if (this.poolIDList.contains(((Element) pool)
						.attributeValue(Constants.Bpmn_Element_ID))) {
					translateGraph((Element) pool);
				}
			}
		}
	}

	/*
	 * Translate a bpmn pool or subprocess to a jpdl process Dom tree
	 */
	private Element translateGraph(Element graph) {
		Document processDef = TranslateHelper.createJpdlDomTree(true);
		Element processRoot = processDef.getRootElement();

		DomXmlWriter.addAttribute(processRoot, Constants.Dom_Element_Name,
				TranslateHelper.generateProcessName(graph));
		processDef.setName(processRoot
				.attributeValue(Constants.Dom_Element_Name));

		map.put(graph.attributeValue(Constants.Dom_Element_ID)
				+ Constants.Bpmn_Pool_Element_Name, processRoot);

		for (Object activity : graph.elements()) {
			if (Constants.Bpmn_Vertice_Element_Name
					.equals(((Element) activity).getName())) {
				translateActivity((Element) activity, processRoot);
				getSequenceFlowInfo((Element)activity);
			}
		}
		translateSequenceFlows(graph, processRoot);
		processDefs.add(processDef);

		return processRoot;
	}

	/*
	 * get Incomingedges and Outgoingedges Map from the activity element
	 */
	private void getSequenceFlowInfo(Element activity) {
		String id = activity.attributeValue(Constants.Dom_Element_ID);
		String ins = activity.attributeValue(Constants.Bpmn_FlowIncomings_Attribute_Name);
		String outs = activity.attributeValue(Constants.Bpmn_FlowOutgoings_Attribute_Name);
		String[] inArray = null;
		String[] outArray = null;
		if(ins != null){
			inArray = ins.split(" ");
			for(String in : inArray){
				targetMap.put(in, id);
			}
		}
		if(outs != null){
			outArray = outs.split(" ");
			for(String out : outArray){
				sourceMap.put(out, id);
			}
		}	
	}

	/*
	 * Translate a bpmn activity to a jpdl node according to activity type
	 */
	private void translateActivity(Element activity, Element processRoot) {
		String type = activity
				.attributeValue(Constants.Bpmn_XmiType_Attribute_Name);
		Element element = null;

		// According to bpmn activity type, map to different jpdl node
		// Some type can not be supported by this translation, we give
		// a warining message for it.
		if ("bpmn:Activity".equals(type)) {
			String activityType = activity
					.attributeValue(Constants.Bpmn_ActivityType_Attribute_Name);
			if (activityType == null || "Task".equals(activityType)) {
				element = DomXmlWriter.addElement(processRoot,
						Constants.Jpdl_Node_Element_Name);
			} else if ("EventStartEmpty".equals(activityType)
					|| "EventStartMessage".equals(activityType)
					|| "EventStartRule".equals(activityType)
					|| "EventStartTimer".equals(activityType)
					|| "EventStartLink".equals(activityType)
					|| "EventStartMultiple".equals(activityType)
					|| "EventStartSignal".equals(activityType)) {
				element = DomXmlWriter.addElement(processRoot,
						Constants.Jpdl_Start_Element_Name);
				if (!"EventStartEmpty".equals(activityType)) {
					warnings
							.add(Constants.Translate_Warning_Bpmn_Element_Type
									+ activityType);
				}
			} else if ("EventIntermediateEmpty".equals(activityType)
					|| "EventIntermediateMessage".equals(activityType)
					|| "EventIntermediateTimer".equals(activityType)
					|| "EventIntermediateError".equals(activityType)
					|| "EventIntermediateCompensation".equals(activityType)
					|| "EventIntermediateRule".equals(activityType)
					|| "EventIntermediateMultiple".equals(activityType)
					|| "EventIntermediateCancel".equals(activityType)
					|| "EventIntermediateLink".equals(activityType)
					|| "EventIntermediateSignal".equals(activityType)) {

				element = DomXmlWriter.addElement(processRoot,
						Constants.Jpdl_State_Element_Name);
				if (!"EventIntermediateEmpty".equals(activityType)) {
					warnings
							.add(Constants.Translate_Warning_Bpmn_Element_Type
									+ activityType);
				}
			} else if ("EventEndEmpty".equals(activityType)
					|| "EventEndMessage".equals(activityType)
					|| "EventEndError".equals(activityType)
					|| "EventEndCompensation".equals(activityType)
					|| "EventEndTerminate".equals(activityType)
					|| "EventEndLink".equals(activityType)
					|| "EventEndMultiple".equals(activityType)
					|| "EventEndCancel".equals(activityType)
					|| "EventEndSignal".equals(activityType)) {

				element = DomXmlWriter.addElement(processRoot,
						Constants.Jpdl_End_Element_Name);
				if (!"EventEndEmpty".equals(activityType)) {
					warnings
							.add(Constants.Translate_Warning_Bpmn_Element_Type
									+ activityType);
				}
			} else if ("GatewayDataBasedExclusive".equals(activityType)
					|| "GatewayEventBasedExclusive".equals(activityType)
					|| "GatewayComplex".equals(activityType)) {
				element = DomXmlWriter.addElement(processRoot,
						Constants.Jpdl_Decision_Element_Name);
				if (!"GatewayDataBasedExclusive".equals(activityType)) {
					warnings
							.add(Constants.Translate_Warning_Bpmn_Element_Type
									+ activityType);
				}
			} else if ("GatewayParallel".equals(activityType)
					|| "GatewayDataBasedInclusive".equals(activityType)) {
				if (activity
						.attributeValue(Constants.Bpmn_InFlow_Attribute_Name) == null
						|| activity.attributeValue(
								Constants.Bpmn_InFlow_Attribute_Name).split(
										Constants.Space).length == 1) {
					element = DomXmlWriter.addElement(processRoot,
							Constants.Jpdl_Fork_Element_Name);
				} else {
					element = DomXmlWriter.addElement(processRoot,
							Constants.Jpdl_Join_Element_Name);
				}
				if (!"GatewayDataBasedInclusive".equals(activityType)) {
					warnings
							.add(Constants.Translate_Warning_Bpmn_Element_Type
									+ activityType);
				}
			}
		} else if ("bpmn:SubProcess".equals(type)) {
			element = DomXmlWriter.addElement(processRoot,
					Constants.Jpdl_ProcessState_Element_Name);
			translateSubprocess(activity, element);
		}

		if (!TranslateHelper.check_mapElementName(activity, element, false)) {
			warnings.add(Constants.Translate_Warning_Bpmn_Element_Name
					+ activity.attributeValue(Constants.Bpmn_Element_ID));
		}
		map.put(activity.attributeValue(Constants.Dom_Element_ID), element);

		// If bpmn activity is loop type, then create a structure to mock it.
		if ("true".equals(activity
				.attributeValue(Constants.Bpmn_Looping_Attribute_Name))) {
			createMockLoop(activity, element);
		}
	}

	/*
	 * Translate a bpmn subprocess to a jpdl process-state and a new jpdl process
	 * definition
	 */
	private void translateSubprocess(Element subProcess, Element element) {
		Element processRoot = translateGraph(subProcess);
		Element ele = DomXmlWriter.addElement(element,
				Constants.Jpdl_SubProcess_Element_Name);
		DomXmlWriter.mapAttribute(ele, Constants.Dom_Element_Name,
				processRoot);

		// translate the transaction of subprocess
		Element eAnnot = subProcess
				.element(Constants.Bpmn_EAnnotations_Element_Name);
		if (eAnnot != null) {
			Element details = eAnnot
					.element(Constants.Bpmn_Details_Element_Name);
			if (details != null
					&& "true"
							.equals(details
									.attributeValue(Constants.Bpmn_Value_Attribute_Name))) {
				translateTransaction(processRoot);
			}
		}
	}

	/*
	 * translate a transaction of sub process
	 */
	private void translateTransaction(Element processRoot) {
		List<Element> lastEleList = TranslateHelper.locateLastElements(processRoot);

		if (lastEleList.size() == 0) {
			return;
		}
		// create a decision
		Element decision = DomXmlWriter.addElement(processRoot,
				Constants.Jpdl_Decision_Element_Name);

		DomXmlWriter.addAttribute(decision, Constants.Dom_Element_Name,
				Constants.Jpdl_Element_Successful_Name);
		// get bpmn id from map
		String bpmnId = null;
		for (String key : map.keySet()) {
			if (map.get(key) == lastEleList.get(0)) {
				bpmnId = key;
				break;
			}
		}
		map.put(bpmnId + Constants.Jpdl_Element_Decision_Suffix, decision);

		// create a transition from element to decision
		for (Element ele : lastEleList) {
			Element transition = DomXmlWriter.addElement(ele,
					Constants.Jpdl_Transition_Element);
			transition.addAttribute(Constants.Dom_Element_Name, ele
					.attributeValue(Constants.Dom_Element_Name)
					+ Constants.To + Constants.Jpdl_Decision_Element_Name);
			transition.addAttribute(Constants.To, decision
					.attributeValue(Constants.Dom_Element_Name));
		}

		// create a complete element
		Element complete = DomXmlWriter.addElement(processRoot,
				Constants.Jpdl_Node_Element_Name);
		DomXmlWriter.addAttribute(complete, Constants.Dom_Element_Name,
				Constants.Jpdl_Element_Complete_Suffix);
		map.put(bpmnId + Constants.Jpdl_Element_Complete_Suffix, complete);

		// create a cancel element
		Element cancel = DomXmlWriter.addElement(processRoot,
				Constants.Jpdl_Node_Element_Name);
		DomXmlWriter.addAttribute(cancel, Constants.Dom_Element_Name,
				Constants.Jpdl_Element_Cancel_Suffix);
		map.put(bpmnId + Constants.Jpdl_Element_Cancel_Suffix, cancel);

		// create transition from decision to complete element
		Element toComplete = DomXmlWriter.addElement(decision,
				Constants.Jpdl_Transition_Element);
		toComplete.addAttribute(Constants.Dom_Element_Name, "true");
		toComplete.addAttribute(Constants.To, complete
				.attributeValue(Constants.Dom_Element_Name));

		// create transition from decision to cancel element
		Element toCancel = DomXmlWriter.addElement(decision,
				Constants.Jpdl_Transition_Element);
		toCancel.addAttribute(Constants.Dom_Element_Name, "false");
		toCancel.addAttribute(Constants.To, cancel
				.attributeValue(Constants.Dom_Element_Name));
	}
	

	/*
	 * Translate bpmn sequenceflows to jpdl transitions
	 */
	private void translateSequenceFlows(Element graph, Element processRoot) {
		for (Object edge : graph
				.elements(Constants.Bpmn_SequenceFlow_Element_Name)) {
			translateSequenceFlow((Element) edge, processRoot);
		}
	}

	/*
	 * Translate a bpmn sequenceflow to a jpdl transition
	 */
	private void translateSequenceFlow(Element edge, Element processRoot) {

		Element source = map.get(sourceMap.get(edge.attributeValue(Constants.Dom_Element_ID)));

		Element transition = null;
		if ("true".equals(edge
				.attributeValue(Constants.Bpmn_FlowDefault_Attribute_Name))
				&& source.element(Constants.Jpdl_Transition_Element) != null) {
			// move default transition to the first of transition list
			transition = DomXmlWriter.addElement(source,
					Constants.Jpdl_Transition_Element, 0);
		} else {
			transition = DomXmlWriter.addElement(source,
					Constants.Jpdl_Transition_Element);
		}

		transition.addAttribute(Constants.To,map.get(targetMap.get(edge.attributeValue(Constants.Dom_Element_ID))).attributeValue(Constants.Dom_Element_Name));
		
		if (!TranslateHelper.check_mapElementName(edge, transition, true)) {
			warnings.add(Constants.Translate_Warning_Bpmn_Element_Name
					+ edge.attributeValue(Constants.Bpmn_Element_ID));
		}
	}

	/*
	 * create a jpdl decision structure to map bpmn loop activity
	 */
	private void createMockLoop(Element activity, Element element) {

		// create a decision
		Element decision = DomXmlWriter.addElement(element.getParent(),
				Constants.Jpdl_Decision_Element_Name);
		String name = TranslateHelper.generateElementName(activity)
				+ Constants.Underline + Constants.Loop_Decision;
		DomXmlWriter.addAttribute(decision, Constants.Dom_Element_Name, name);

		// use the decision to replace the activity in the map
		map.put(activity.attributeValue(Constants.Dom_Element_ID), decision);
		// add the activity to map
		map.put(activity.attributeValue(Constants.Dom_Element_ID)
				+ Constants.Bpmn_Vertice_Element_Name, element);

		// create a transition from element to decision
		Element first = DomXmlWriter.addElement(element,
				Constants.Jpdl_Transition_Element);
		first.addAttribute(Constants.Dom_Element_Name, Constants.To
				+ Constants.Underline + name);
		first.addAttribute(Constants.To, decision
				.attributeValue(Constants.Dom_Element_Name));

		// create a transition from decision to element
		Element second = DomXmlWriter.addElement(decision,
				Constants.Jpdl_Transition_Element);
		second.addAttribute(Constants.Dom_Element_Name, Constants.To
				+ Constants.Underline
				+ element.attributeValue(Constants.Dom_Element_Name));
		second.addAttribute(Constants.To, element
				.attributeValue(Constants.Dom_Element_Name));

	}
	
	public List<Document> getProcessDefs() {
		return processDefs;
	}

	public void setProcessDefs(List<Document> processDefs) {
		this.processDefs = processDefs;
	}

	public Map<String, Element> getMap() {
		return map;
	}

	public void setMap(Map<String, Element> map) {
		this.map = map;
	}
}
