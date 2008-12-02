package org.jboss.tools.flow.jpdl4.wizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewJpdl4FileWizardPage extends WizardNewFileCreationPage {

	public NewJpdl4FileWizardPage(IStructuredSelection selection) {
		super("NewJpdl4FileWizardPage", selection);
        setTitle("Jpdl4 File");
        setDescription("Creates a new jpdl4 File");
        setFileExtension("jpdl.xml");
	}

    @Override
    protected InputStream getInitialContents() {
    	return new ByteArrayInputStream("<process>\n</process>".getBytes());
    }
}
