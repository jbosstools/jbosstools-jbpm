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

package org.jboss.tools.jbpm.convert.bpmnto.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.jbpm.convert.b2j.messages.B2JMessages;
import org.jboss.tools.jbpm.convert.bpmnto.util.BPMNToUtil;
import org.jboss.tools.jbpm.convert.bpmnto.wizard.BpmnPoolsChoicePage;
import org.jboss.tools.jbpm.convert.bpmnto.wizard.ErrorMessagesPage;
import org.jboss.tools.jbpm.convert.bpmnto.wizard.GeneratedFileLocationPage;
import org.jboss.tools.jbpm.convert.bpmnto.wizard.BPMNResourcesChoicePage;

/**
 * @author Grid Qian
 * 
 *         the wizard for bpmn translation
 */
public abstract class BpmnToWizard extends Wizard implements IExportWizard {

	private BPMNResourcesChoicePage bpmnResPage;
	private BpmnPoolsChoicePage poolsPage;
	private GeneratedFileLocationPage locationPage;
	private ErrorMessagesPage errorPage;

	// the selected *.bpmn file
	private IStructuredSelection selection;
	// the selected folder to save these generated files
	private IStructuredSelection targetLocationSelection;
	// bpmn pool id:name map
	private Map<String, String> idMap;
	private boolean isOverWrite = true;
	private boolean isDoTranslation = false;

	protected String bpmnFileName;
	protected String bpmnFileParentPath;
	protected List<String> poolIdList = new ArrayList<String>();
	// the list contains errors or warnings when generating
	protected List<String> errorList = new ArrayList<String>();

	public BpmnToWizard() {
		super();
		super.setWindowTitle(B2JMessages.Bpmn_Wizard_Title);
	}

	public void addPages() {
		super.addPages();

		bpmnResPage = new BPMNResourcesChoicePage(
				B2JMessages.Bpmn_File_Choose_WizardPage_Name,
				B2JMessages.Bpmn_File_Choose_WizardPage_Title,
				B2JMessages.Bpmn_File_Choose_WizardPage_ViewerTitle,
				B2JMessages.Bpmn_File_Choose_WizardPage_Message);
		bpmnResPage.setSelection(selection);
		poolsPage = new BpmnPoolsChoicePage(
				B2JMessages.Bpmn_Pool_Choose_WizardPage_Name,
				B2JMessages.Bpmn_Pool_Choose_WizardPage_Title,
				B2JMessages.Bpmn_Pool_Choose_WizardPage_ViewerTitle,
				B2JMessages.Bpmn_Pool_Choose_WizardPage_Message);
		errorPage = new ErrorMessagesPage(
				B2JMessages.Bpmn_Translate_Message_WizardPage_Name,
				B2JMessages.Bpmn_Translate_Message_WizardPage_Title,
				B2JMessages.Bpmn_Translate_Message_WizardpageViewer_Title,
				B2JMessages.Bpmn_Translate_Message_WizardPage_Message);
		locationPage = new GeneratedFileLocationPage(
				B2JMessages.Bpmn_GeneratedFile_Location_WizardPage_Name,
				B2JMessages.Bpmn_GeneratedFile_Location_WizardPage_Title,
				B2JMessages.Bpmn_GeneratedFile_Location_WizardPage_ViewerTitle,
				B2JMessages.Bpmn_GeneratedFile_Location_WizardPage_Message);
		locationPage.setSelection(selection);
		addPage(bpmnResPage);
		addPage(poolsPage);
		addPage(errorPage);
		addPage(locationPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		initialize();
	}

	/*
	 * do some initial tasks
	 */
	private void initialize() {
		if (selection.getFirstElement() instanceof IFile) {
			IFile bpmnFile = (IFile) selection.getFirstElement();
			bpmnFileName = bpmnFile.getName();
			bpmnFileParentPath = bpmnFile.getParent().getLocation()
					.toOSString();
			try {
				idMap = BPMNToUtil.getPoolIDsFromDocument(BPMNToUtil.parse(
						bpmnFileParentPath, bpmnFileName));
				poolIdList.clear();
			} catch (Exception e) {
				errorList.add(0, NLS.bind(
						B2JMessages.Translate_Error_File_CanNotRead,
						bpmnFileName));
				e.printStackTrace();
			}
		}
		if (poolsPage != null) {
			poolsPage.setIdMap(idMap);
		}
	}

	public IWizardPage getNextPage(IWizardPage page) {
		if (page.getName().equals(B2JMessages.Bpmn_Pool_Choose_WizardPage_Name)) {
			errorList = translateBpmnToStrings();
			isDoTranslation = true;
			if (errorList.size() == 0) {
				return locationPage;
			}
			errorPage.getListViewer().setInput(errorList);
			return super.getNextPage(page);
		} else {
			return super.getNextPage(page);
		}

	}

	public boolean performFinish() {
		if(!isDoTranslation){
			translateBpmnToStrings();
		}
		createGeneratedFile(this.isOverWrite());
		refreshWorkspace();
		return true;
	}

	/*
	 * do the translation from bpmn to string list the sub class need to create
	 * a string list to reserve these strings the return list is error or
	 * warning messages
	 */
	public abstract List<String> translateBpmnToStrings();

	/*
	 * write the generated strings to the files
	 */
	public abstract void createGeneratedFile(boolean b);

	/*
	 * get the path of the eclipse workspace container
	 */
	protected String getContainerPath(IContainer container) {
		return container.getLocation().toOSString();
	}


	/*
	 * refresh eclipse workspace
	 */
	public void refreshWorkspace() {
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(
					IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public List<String> getPoolIdList() {
		return poolIdList;
	}

	public void setPoolIdList(List<String> poolIdList) {
		this.poolIdList = poolIdList;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public IStructuredSelection getSelection() {
		return selection;
	}

	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
		initialize();
	}

	public String getBpmnFileName() {
		return bpmnFileName;
	}

	public void setBpmnFileName(String bpmnFileName) {
		this.bpmnFileName = bpmnFileName;
	}

	public String getBpmnFileParentPath() {
		return bpmnFileParentPath;
	}

	public void setBpmnFileParentPath(String bpmnFileParentPath) {
		this.bpmnFileParentPath = bpmnFileParentPath;
	}

	public IStructuredSelection getTargetLocationSelection() {
		return targetLocationSelection;
	}

	public void setTargetLocationSelection(
			IStructuredSelection targetLocationSelection) {
		this.targetLocationSelection = targetLocationSelection;
	}
	
	public boolean isOverWrite() {
		return isOverWrite;
	}

	public void setOverWrite(boolean isOverWrite) {
		this.isOverWrite = isOverWrite;
	}
}
