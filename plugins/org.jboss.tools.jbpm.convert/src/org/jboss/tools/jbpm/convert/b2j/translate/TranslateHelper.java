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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.jboss.tools.jbpm.convert.bpmnto.util.BPMNToUtil;
import org.jboss.tools.jbpm.convert.bpmnto.util.DomXmlWriter;

/**
 * @author Grid Qian
 * 
 *         this is a helper class for bpmn ->jpdl
 */
public class TranslateHelper {

	// the map for the bpmn element id : the corresponding generated element
	static Map<String, Integer> nameMap = new HashMap<String, Integer>();

	public static void setNameMap(Map<String, Integer> nameMap) {
		TranslateHelper.nameMap = nameMap;
	}

	/*
	 * get the bpmn_diagram file name
	 */
	public static String getBpmnDiagramName(String bpmnFileName) {
		return bpmnFileName + Constants.Bpmn_Diagram_Name_Suffix;
	}

	/*
	 * create a jpdl dom tree
	 */
	public static Document createJpdlDomTree(boolean useNamespace) {
		return DomXmlWriter.createDomTree(useNamespace,
				Constants.Jpdl_32_Namespace_Url,
				Constants.Jpdl_Process_Definition_Element_Name);

	}

	/*
	 * create translated files from dom strings
	 */
	public static File[] createFiles(String parentFolder, String bpmnFileName,
			String[] strsForGenerate, String[] fileFolders, String fileName,
			boolean isOverWrite) throws IOException {
		File jpdlFolder = BPMNToUtil.createFile(parentFolder,
				Constants.Jpdl_Suffix, null, isOverWrite);
		File diagramFolder = BPMNToUtil.createFile(
				jpdlFolder.getAbsolutePath(), bpmnFileName, null, false);

		File[] files = new File[strsForGenerate.length];
		File processFolder = null;
		int i = 0;
		for (String str : strsForGenerate) {
			processFolder = BPMNToUtil.createFile(diagramFolder
					.getAbsolutePath(), fileFolders[i], null, false);
			files[i] = BPMNToUtil.createFile(processFolder.getAbsolutePath(),
					fileName, str, false);
			i++;
		}
		return files;
	}

	/*
	 * generate a process definition name. the name is composed of the names
	 * from bpmn diagram to the element
	 */
	public static String generateProcessName(Element graph) {
		if ("BpmnDiagram".equals(graph.getName())) {
			return graph.attributeValue(Constants.Dom_Element_Name);
		} else {
			String str = generateProcessName(graph.getParent());
			if (str == null) {
				return generateElementName(graph);
			} else {
				return str + Constants.Folder_Name_Separator
						+ generateElementName(graph);
			}
		}

	}

	/*
	 * generate a element name
	 */
	public static String generateElementName(Element graph) {
		String name = graph.attributeValue(Constants.Dom_Element_Name);
		if (name == null || "".equals(name)) {
			name = "eleName";
		}
		Integer i = nameMap.get(name);
		if (i == null) {
			nameMap.put(name, new Integer("1"));
		} else {
			nameMap.put(name, ++i);
		}
		return name;
	}

	/*
	 * check the bpmn element name is null or "" or same to another element name
	 * and generate a different name
	 */
	public static boolean check_mapElementName(Element graph, Element graphEle,
			boolean isTransition) {

		boolean isOk = true;
		String name = graph.attributeValue(Constants.Dom_Element_Name);

		if (name == null || "".equals(name)) {
			if (isTransition) {
				name = Constants.To + Constants.Underline
						+ graphEle.attributeValue(Constants.To);
			} else {
				name = "eleName";
			}

			isOk = false;
		}
		Integer i = nameMap.get(name);
		if (i == null) {
			DomXmlWriter.addAttribute(graphEle, Constants.Dom_Element_Name,
					name);
			nameMap.put(name, new Integer("1"));
		} else {
			DomXmlWriter.addAttribute(graphEle, Constants.Dom_Element_Name,
					name + "_" + i);
			nameMap.put(name, ++i);
			isOk = false;
		}

		return isOk;

	}

	/*
	 * select the elements from process. These elements have no transitions sub
	 * element or have transitions to end-state.
	 */
	public static List<Element> locateLastElements(Element processRoot) {
		List<Element> list = new ArrayList<Element>();

		if (processRoot.element(Constants.Jpdl_End_Element_Name) != null) {
			Element endState = processRoot
					.element(Constants.Jpdl_End_Element_Name);
			for (Object ele : processRoot.elements()) {
				Element subEle = ((Element) ele)
						.element(Constants.Jpdl_Transition_Element);
				if (subEle != null
						&& endState.attributeValue(Constants.Dom_Element_Name)
								.equals(subEle.attributeValue(Constants.To))) {
					list.add((Element) ele);
				}
			}
		} else {
			for (Object ele : processRoot.elements()) {
				if (((Element) ele).element(Constants.Jpdl_Transition_Element) == null) {
					list.add((Element) ele);
				}
			}
		}

		return list;
	}

	/*
	 * get the layout element from bpmn_diagram file by id
	 */
	public static Element getDiagramLayoutElement(String bpmnID,
			List<Element> eleList) {
		Element element = null;
		for (Element ele : eleList) {
			String str = ele.attributeValue(Constants.Bpmn_Href_Attribute_Name);
			if (str != null && str.contains(bpmnID)) {
				element = ele;
				break;
			}
		}

		if (element != null
				&& element.getParent().element(
						Constants.Gpd_Layout_Element_Name) != null) {
			return element.getParent().element(
					Constants.Gpd_Layout_Element_Name);
		}

		return null;
	}

	/*
	 * create a graphical jpdl transition
	 */
	public static Element createTransition(Element parentEle, String name,
			Element jpdlEle) {
		Element ele = DomXmlWriter.addElement(parentEle, name);
		DomXmlWriter.mapAttribute(ele, Constants.Dom_Element_Name, jpdlEle);

		Element label = DomXmlWriter.addElement(ele,
				Constants.Gpd_Label_Element_Name);
		TranslateHelper.mapXY(label, "5", "-10");
		return ele;
	}

	/*
	 * create graphical jpdl element
	 */
	private static Element createElement(Element parentEle, String name,
			String width, String height) {
		Element ele = DomXmlWriter.addElement(parentEle, name);
		DomXmlWriter.addAttribute(ele, Constants.Width_Attribute_Name, width);
		DomXmlWriter.addAttribute(ele, Constants.Height_Attribute_Name, height);
		return ele;
	}

	/*
	 * create graphical jpdl node
	 */
	public static Element createNode(Element rootEle, String name,
			Element jpdlEle) {
		Element ele = TranslateHelper.createElement(rootEle, name, "100", "40");
		DomXmlWriter.mapAttribute(ele, Constants.Dom_Element_Name, jpdlEle);
		return ele;
	}

	/*
	 * map bpmn x,y attribute to jpdl graphical element
	 */
	public static void mapXY(Element pgdEle, String x, String y) {
		DomXmlWriter.addAttribute(pgdEle, Constants.X_Attribute_Name,
				x == null ? "0" : x);
		DomXmlWriter.addAttribute(pgdEle, Constants.Y_Attribute_Name,
				y == null ? "0" : y);

	}

	/*
	 * get a bpmn element id from a given string by subtract suffix for example
	 * bpmn pool has pools suffix a bpmn loop activity has vertices suffix
	 */
	public static String getPureBpmnID(String bpmnID, String suffix) {
		return bpmnID.substring(0, bpmnID.length() - suffix.length());
	}

	/*
	 * get (x,y) from a graphical element and add increments
	 */
	public static String[] getXY(Element bpmnGpdEle, int xIncre, int yIncre) {
		String xy[] = new String[2];
		if (bpmnGpdEle == null) {
			xy[0] = String.valueOf(xIncre);
			xy[1] = String.valueOf(yIncre);
		} else {
			xy[0] = String.valueOf(Integer.parseInt(bpmnGpdEle
					.attributeValue(Constants.X_Attribute_Name) == null ? "0"
					: bpmnGpdEle.attributeValue(Constants.X_Attribute_Name))
					+ xIncre);
			xy[1] = String.valueOf(Integer.parseInt(bpmnGpdEle
					.attributeValue(Constants.Y_Attribute_Name) == null ? "0"
					: bpmnGpdEle.attributeValue(Constants.Y_Attribute_Name))
					+ yIncre);
		}
		return xy;
	}

}
