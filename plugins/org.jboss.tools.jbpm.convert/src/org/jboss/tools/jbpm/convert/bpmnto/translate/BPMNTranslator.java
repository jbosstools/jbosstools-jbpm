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

package org.jboss.tools.jbpm.convert.bpmnto.translate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Grid Qian
 * 
 * this is a root translator. You should extend it and realize the
 * method: translateDiagram.
 */
public abstract class BPMNTranslator {

	// the bpmn file name
	public String bpmnFileName;
	
	//the location of the folder containing the bpmn file 
	public String rootLocation;
	
	// the bpmn pool's id list that are translated
	public List<String> poolIDList;


	// the warning messages when translate
	public List<String> warnings = new ArrayList<String>();

	// the error messages when translate
	public List<String> errors = new ArrayList<String>();

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	public BPMNTranslator() {
	}

	public BPMNTranslator(String bpmnFileName, String bpmnFilePath) {
		this(bpmnFileName, bpmnFilePath, null);
	}

	public BPMNTranslator(String bpmnFileName, String bpmnFilePath,
			List<String> poolIDList) {
		this.bpmnFileName = bpmnFileName;
		this.rootLocation = bpmnFilePath;
		this.poolIDList = poolIDList;
	}

	/*
	 * Translate a bpmn diagram to anything
	 */
	public abstract void translateDiagram();
}