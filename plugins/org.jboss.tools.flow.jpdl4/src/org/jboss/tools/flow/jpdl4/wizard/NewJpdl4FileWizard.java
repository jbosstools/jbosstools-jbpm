package org.jboss.tools.flow.jpdl4.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.jboss.tools.flow.jpdl4.Logger;

public class NewJpdl4FileWizard extends Wizard implements IWorkbenchWizard, INewWizard {
	
	private static String ID_PROP_SHEET = "org.eclipse.ui.views.PropertySheet";

    private IStructuredSelection selection;
    private NewJpdl4FileWizardPage newFileWizardPage;

    public NewJpdl4FileWizard() {
        setWindowTitle("New jPDL4 File");
    } 
    
    @Override
    public void addPages() {
        newFileWizardPage = new NewJpdl4FileWizardPage(selection);
        addPage(newFileWizardPage);
    }
    
    private IWorkbenchWindow getActiveWindow() {
    	IWorkbench workbench = PlatformUI.getWorkbench();
    	return workbench == null ? null : workbench.getActiveWorkbenchWindow();
    }
    
    private IWorkbenchPage getActivePage() {
    	IWorkbenchWindow window = getActiveWindow();
    	return window == null ? null: window.getActivePage();
    }
    
    @Override
    public boolean performFinish() {        
        IFile file = newFileWizardPage.createNewFile();
        IWorkbenchPage activePage = getActivePage();
        if (file != null && activePage != null) {
        	try {
        		IDE.openEditor(activePage, file);
        		activePage.showView(ID_PROP_SHEET);
        		BasicNewResourceWizard.selectAndReveal(file, getActiveWindow());
        		return true;
        	} catch (PartInitException e) {
        		Logger.logError("Exception while opening jPDL 4 editor", e);
        	}
        }
        return false;
    }
    
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }

}
