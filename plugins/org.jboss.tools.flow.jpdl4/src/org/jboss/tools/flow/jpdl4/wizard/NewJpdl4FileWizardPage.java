package org.jboss.tools.flow.jpdl4.wizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class NewJpdl4FileWizardPage extends WizardNewFileCreationPage {
	
	private Combo combo;

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
    	buffer.append("<process xmlns=\"http://jbpm.org/"+ combo.getText().trim() + "/jpdl\" name=\"");
    	buffer.append(getFileName().substring(0, getFileName().lastIndexOf(getFileExtension()) - 1));
    	buffer.append("\">\n</process>");
    	return buffer.toString();
    }
    
	protected void createAdvancedControls(Composite parent) {
		createVersionControls(parent);
		super.createAdvancedControls(parent);
	}
	
	protected void createVersionControls(Composite parent) {
		Composite versionControlsParent = new Composite(parent, SWT.NONE);
		versionControlsParent.setFont(parent.getFont());
		versionControlsParent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		versionControlsParent.setLayout(layout);
		
		Label label = new Label(versionControlsParent, SWT.NONE);
		label.setText("Version:   ");
		label.setFont(parent.getFont());
		combo = new Combo(versionControlsParent, SWT.BORDER | SWT.READ_ONLY);
		combo.setFont(parent.getFont());
		combo.setSize(50, combo.getSize().y);
		combo.add("  4.3");
		combo.add("  4.2");
		combo.add("  4.1");
		combo.add("  4.0");
		combo.select(0);
	}
    
}
