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
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.jboss.tools.jbpm.convert.bpmnto.BpmnToPlugin;
import org.jboss.tools.jbpm.convert.bpmnto.translate.BPMNTranslator;
import org.jboss.tools.jbpm.convert.bpmnto.util.DomXmlWriter;
import org.jboss.tools.jbpm.convert.b2j.messages.B2JMessages;
import org.jboss.tools.jbpm.convert.b2j.translate.TranslateHelper;

/**
 * @author Grid Qian
 * 
 *         this is a translator for bpmn_diagram to gpd of jpdl
 */
public class GraphicalFileGenerator extends BPMNTranslator {

	Document document;

	// gpd.xml documents list
	List<Document> gpdDefs = new ArrayList<Document>();

	// the pool of gpd.xml name
	List<String> gpdPoolNames = new ArrayList<String>();
	Map<String, Element> map = new HashMap<String, Element>();

	public GraphicalFileGenerator(Document bpmnDiagramDocument,
			Map<String, Element> map, String rootLocation, String bpmnFileName) {
		this.rootLocation = rootLocation;
		this.bpmnFileName = bpmnFileName;
		this.document = bpmnDiagramDocument;
		this.map = map;
	}

	/*
	 * translate a bpmn_diagram document to a gpd document string
	 */
	public String[] translateToStrings() {
		this.translateDiagram();
		String[] strForProcessDefs = new String[gpdDefs.size()];
		int i = 0;
		for (Document def : gpdDefs) {
			try {
				strForProcessDefs[i] = DomXmlWriter.toString(def);
			} catch (IOException e) {
				this.errors
						.add(B2JMessages.Translate_Error_GpdFile_CanNotGenerate
								+ e.getMessage());
			}
			i++;
		}
		return strForProcessDefs;
	}

	/*
	 * translate every gpd document string to a gpd file
	 */
	public void translateToFiles(String fileLocation) {
		String[] strForGpdDefs = translateToStrings();
		String[] gpdFileNames = new String[gpdDefs.size()];
		int i = 0;
		for (Document def : gpdDefs) {
			gpdFileNames[i] = def.getRootElement().attributeValue(
					B2JMessages.Dom_Element_Name);
			i++;
		}

		try {
			TranslateHelper.createFiles(fileLocation, bpmnFileName,
					strForGpdDefs, gpdFileNames,
					B2JMessages.Gpd_Definition_Name, false);
		} catch (Exception e) {
			errors.add(B2JMessages.Translate_Error_GpdFile_CanNotWrite
					+ e.getMessage());
		}

		if (errors.size() != 0) {
			for (String str : errors) {
				BpmnToPlugin.getDefault().logError(str);
			}
		}

		if (warnings.size() != 0) {
			for (String str : warnings) {
				BpmnToPlugin.getDefault().logWarning(str);
			}
		}
	}

	/*
	 * translate the graphical bpmn_diagram document
	 */
	public void translateDiagram() {
		Element rootElement = document.getRootElement();
		List<Element> eleList = DomXmlWriter.getElementsByName(rootElement,
				B2JMessages.Gpd_Element_Name);

		for (String bpmnID : map.keySet()) {
			if (bpmnID != null) {
				// when translate bpmn loop activity, we create a mock
				// structure and when translate bpmn transaction, we create a
				// mock structure too. For these reason, we need to give a (x,y)
				// increment to avoid two element overlap
				int xIncre = 0;
				int yIncre = 0;
				Element bpmnGpdEle = TranslateHelper.getDiagramLayoutElement(
						bpmnID, eleList);
				if (bpmnGpdEle == null) {
					if (bpmnID.endsWith(B2JMessages.Bpmn_Vertice_Element_Name)) {
						bpmnGpdEle = TranslateHelper.getDiagramLayoutElement(
								TranslateHelper.getPureBpmnID(bpmnID,
										B2JMessages.Bpmn_Vertice_Element_Name),
								eleList);
						xIncre = 150;
						yIncre = 50;
					} else if (bpmnID
							.endsWith(B2JMessages.Jpdl_Element_Decision_Suffix)) {
						bpmnGpdEle = TranslateHelper
								.getDiagramLayoutElement(
										TranslateHelper
												.getPureBpmnID(
														bpmnID,
														B2JMessages.Jpdl_Element_Decision_Suffix),
										eleList);
						xIncre = 150;
						yIncre = 100;
					} else if (bpmnID
							.endsWith(B2JMessages.Jpdl_Element_Complete_Suffix)) {
						bpmnGpdEle = TranslateHelper
								.getDiagramLayoutElement(
										TranslateHelper
												.getPureBpmnID(
														bpmnID,
														B2JMessages.Jpdl_Element_Complete_Suffix),
										eleList);
						xIncre = 300;
						yIncre = 0;
					} else if (bpmnID
							.endsWith(B2JMessages.Jpdl_Element_Cancel_Suffix)) {
						bpmnGpdEle = TranslateHelper
								.getDiagramLayoutElement(
										TranslateHelper
												.getPureBpmnID(
														bpmnID,
														B2JMessages.Jpdl_Element_Cancel_Suffix),
										eleList);
						xIncre = 300;
						yIncre = 200;
					}
				}
				if (bpmnGpdEle == null) {
					continue;
				}
				translateGraphicalElement(TranslateHelper.getXY(bpmnGpdEle,
						xIncre, yIncre), map.get(bpmnID), eleList);
			}
		}

	}

	private void translateGraphicalElement(String[] xy, Element jpdlEle,
			List<Element> eleList) {
		if (jpdlEle == null) {
			return;
		}

		// if not translate, then translate the pool of the element
		if (!gpdPoolNames.contains(jpdlEle.getParent().attributeValue(
				B2JMessages.Dom_Element_Name))
				&& B2JMessages.Jpdl_Process_Definition_Element_Name
						.equals(jpdlEle.getParent().getName())) {
			translatePool(eleList, jpdlEle);
		}

		// translate the bpmn graphical element
		Element poolEle = null;
		for (Document doc : gpdDefs) {
			if (doc.getRootElement().attributeValue(
					B2JMessages.Dom_Element_Name).equals(
					jpdlEle.getParent().attributeValue(
							B2JMessages.Dom_Element_Name))) {
				poolEle = doc.getRootElement();
			}
		}
		Element pgdEle = TranslateHelper.createNode(poolEle,
				B2JMessages.Jpdl_Node_Element_Name, jpdlEle);

		TranslateHelper.mapXY(pgdEle, xy[0], xy[1]);

		// translate the sequence flow of the element
		for (Object ele : jpdlEle.elements(B2JMessages.Jpdl_Transition_Element)) {
			TranslateHelper.createTransition(pgdEle,
					B2JMessages.Jpdl_Transition_Element, (Element) ele);
		}
	}

	private void translatePool(List<Element> eleList, Element jpdlEle) {
		// get the bpmn pool id
		String poolBpmnID = null;
		for (String id : map.keySet()) {
			if (map.get(id) == jpdlEle.getParent()) {
				poolBpmnID = TranslateHelper.getPureBpmnID(id,
						B2JMessages.Bpmn_Pool_Element_Name);
				break;
			}
		}
		// get the bpmn pool graphical element
		if (poolBpmnID != null) {
			Element poolBpmnGpdEle = TranslateHelper.getDiagramLayoutElement(
					poolBpmnID, eleList);
			Document processDiagramDef = DomXmlWriter.createDomTree(false,
					null, B2JMessages.Gpd_Process_Diagram_Name);
			Element poolEle = processDiagramDef.getRootElement();
			DomXmlWriter.mapAttribute(poolEle, B2JMessages.Dom_Element_Name,
					jpdlEle.getParent());
			DomXmlWriter.mapAttribute(poolEle,
					B2JMessages.Width_Attribute_Name, poolBpmnGpdEle);
			DomXmlWriter.mapAttribute(poolEle,
					B2JMessages.Height_Attribute_Name, poolBpmnGpdEle);

			gpdDefs.add(processDiagramDef);
			gpdPoolNames.add(poolEle
					.attributeValue(B2JMessages.Dom_Element_Name));
		}

	}

	public List<Document> getGpdDefs() {
		return gpdDefs;
	}

	public void setGpdDefs(List<Document> gpdDefs) {
		this.gpdDefs = gpdDefs;
	}

}
