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
    	return new ByteArrayInputStream(getInitialContentsAsString().getBytes());
    }
    
    private String getInitialContentsAsString() {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
    	buffer.append("<process xmlns=\"http://jbpm.org/4.0/jpdl\" name=\"");
    	buffer.append(getFileName().substring(0, getFileName().lastIndexOf(getFileExtension()) - 1));
    	buffer.append("\">\n</process>");
    	return buffer.toString();
    }
}
