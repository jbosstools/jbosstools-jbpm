package org.jboss.tools.flow.jpdl4.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

public class NewJpdl4FileWizard extends Wizard implements IWorkbenchWizard {

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
    
    @Override
    public boolean performFinish() {        
        IFile file = newFileWizardPage.createNewFile();
        if (file != null)
            return true;
        else
            return false;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }

}
