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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jboss.tools.jbpm.convert.b2j.translate.Constants;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * @author Grid Qian
 * 
 *         this a util class
 */
public class BPMNToUtil {

	/*
	 * create a file
	 */
	public static File createFile(String parentFolder, String fileName,
			String inputStr, boolean isOverWrite) throws IOException {
		File child = new File(parentFolder, fileName);
		if(isOverWrite){
			deleteAll(child);
		}
		if (inputStr == null) {
			if (!child.exists()) {
				child.mkdir();
			}
		} else {
			if (!child.exists()) {
				child.createNewFile();
			}
			FileWriter childWriter = new FileWriter(child);
			PrintWriter printFile = new PrintWriter(childWriter);
			printFile.println(inputStr);
			printFile.close();
			childWriter.close();

		}
		return child;
	}

	public static void deleteAll(File path) {
		if (!path.exists())
			return;
		if (path.isFile()) {
			path.delete();
			return;
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteAll(files[i]);
		}
		path.delete();
	}

	/*
	 * get a sax input source
	 */
	public static InputSource getInputSource(String parentFolder,
			String fileName) throws FileNotFoundException {
		return new InputSource(BPMNToUtil
				.getInputStream(parentFolder, fileName));

	}

	/*
	 * get a input stream
	 */
	public static InputStream getInputStream(String parentFolder,
			String fileName) throws FileNotFoundException {
		File file = new File(parentFolder, fileName);
		InputStream input = new FileInputStream(file);
		return input;

	}

	/*
	 * parse a file to a dom document
	 */
	public static Document parse(String parentFolder, String fileName)
			throws Exception {
		Document document = null;
		SAXReader saxReader = createSaxReader();
		document = saxReader.read(BPMNToUtil.getInputSource(parentFolder,
				fileName));
		return document;
	}

	/*
	 * create a sax reader
	 */
	public static SAXReader createSaxReader() throws Exception {
		XMLReader xmlReader = createXmlReader();
		SAXReader saxReader = new SAXReader(xmlReader);
		return saxReader;
	}

	/*
	 * create a sax xml reader
	 */
	public static XMLReader createXmlReader() throws Exception {

		SAXParser saxParser = createSaxParserFactory().newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();

		saxParser.setProperty(
				"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");

		xmlReader.setFeature(
				"http://apache.org/xml/features/validation/dynamic", true);

		return xmlReader;
	}

	/*
	 * create a sax parser factory
	 */
	private static SAXParserFactory createSaxParserFactory() {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setValidating(true);
		saxParserFactory.setNamespaceAware(true);
		return saxParserFactory;
	}

	/*
	 * get bpmn pool id list from a dom document
	 */
	public static Map<String, String> getPoolIDsFromDocument(Document document) {
		Map<String, String> poolIDMap = new HashMap<String, String>();
		Element diagram = document.getRootElement();
		for (Object pool : diagram.elements(Constants.Bpmn_Pool_Element_Name)) {
			if (((Element) pool).attributeValue(Constants.Bpmn_Element_ID) != null) {
				poolIDMap.put(((Element) pool)
						.attributeValue(Constants.Bpmn_Element_ID),
						((Element) pool)
								.attributeValue(Constants.Dom_Element_Name));
			}
		}
		return poolIDMap;
	}
}
