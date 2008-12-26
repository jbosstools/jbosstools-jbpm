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

package org.jboss.tools.jbpm.convert.bpmnto.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.*;
import org.dom4j.io.*;

/**
 * @author Grid Qian
 * 
 *         this is for generating dom element or attribute
 */
public class DomXmlWriter {

	/*
	 * map a dom document to a string
	 */
	public static String toString(Document document) throws IOException {
		OutputFormat outputFormat = new OutputFormat("  ", true);
		Writer writer = new StringWriter();
		XMLWriter xmlWriter = new XMLWriter(writer, outputFormat);
		xmlWriter.write(document);
		xmlWriter.flush();
		writer.flush();
		return writer.toString();
	}

	/*
	 * create a dom tree
	 */
	public static Document createDomTree(boolean useNamespace, String url,
			String rootElementName) {
		Document document = DocumentHelper.createDocument();
		Element root = null;

		if (useNamespace) {
			Namespace jbpmNamespace = new Namespace(null, url);
			root = document.addElement(rootElementName, jbpmNamespace.getURI());
		} else {
			root = document.addElement(rootElementName);
		}
		root.addText(System.getProperty("line.separator"));

		return document;
	}

	/*
	 * add a new element to a dom element
	 */
	public static Element addElement(Element element, String elementName) {
		Element newElement = element.addElement(elementName);
		return newElement;
	}

	/*
	 * add a new element to the location of a dom element
	 */
	@SuppressWarnings("unchecked")
	public static Element addElement(Element element, String elementName,
			int location) {
		Element newElement = null;
		if (element.elements(elementName) != null
				&& element.elements(elementName).size() > location
				&& location >= 0) {
			newElement = DocumentHelper.createElement(elementName);
			element.elements(elementName).add(location, newElement);
		}
		return newElement;
	}

	/*
	 * add a attribute to a dom element
	 */
	public static void addAttribute(Element e, String attributeName,
			String value) {
		if (value != null) {
			e.addAttribute(attributeName, value);
		}
	}

	/*
	 * get a named element from a element (any depth)
	 */
	public static List<Element> getElementsByName(Element element, String name) {
		List<Element> list = new ArrayList<Element>();
		for (Object ele : element.elements()) {
			if (name.equals(((Element) ele).getName())) {
				list.add((Element) ele);
			}
			list.addAll(getElementsByName((Element) ele, name));
		}
		return list;
	}
	
	/*
	 * set a element attribute using a same name attribute of other element
	 */
	public static void mapAttribute(Element ele, String attrName, Element sourceEle){
		DomXmlWriter.addAttribute(ele, attrName,sourceEle.attributeValue(attrName));
	}

}
