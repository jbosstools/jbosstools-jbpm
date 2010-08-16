package org.jbpm.gd.jpdl.refactoring;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RenameProcessWizard extends RefactoringWizard {

	public RenameProcessWizard(IResource resource) {
		super(new RenameRefactoring(new RenameProcessProcessor(resource)), DIALOG_BASED_USER_INTERFACE);
		setDefaultPageTitle("Rename Process");
		setWindowTitle("Rename Process");
	}

	protected void addUserInputPages() {
		RenameProcessProcessor processor= (RenameProcessProcessor) getRefactoring().getAdapter(RenameProcessProcessor.class);
		addPage(new RenameProcessRefactoringConfigurationPage(processor));
	}

	private static class RenameProcessRefactoringConfigurationPage extends UserInputWizardPage {

		private final RenameProcessProcessor refactoringProcessor;
		private Text nameField;

		public RenameProcessRefactoringConfigurationPage(RenameProcessProcessor processor) {
			super("RenameResourceRefactoringInputPage"); 
			refactoringProcessor= processor;
		}

		public void createControl(Composite parent) {
			Composite composite= new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			composite.setFont(parent.getFont());

			Label label= new Label(composite, SWT.NONE);
			label.setText("New na&me");
			label.setLayoutData(new GridData());

			nameField= new Text(composite, SWT.BORDER);
			nameField.setText(refactoringProcessor.getNewProcessName());
			nameField.setFont(composite.getFont());
			nameField.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			nameField.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					validatePage();
				}
			});

			nameField.selectAll();
			setPageComplete(false);
			setControl(composite);
		}

		public void setVisible(boolean visible) {
			if (visible) {
				nameField.setFocus();
			}
			super.setVisible(visible);
		}

		protected final void validatePage() {
			String text= nameField.getText();
			RefactoringStatus status= refactoringProcessor.validateNewElementName(text);
			setPageComplete(status);
		}

		protected boolean performFinish() {
			initializeRefactoring();
			storeSettings();
			return super.performFinish();
		}

		public IWizardPage getNextPage() {
			initializeRefactoring();
			storeSettings();
			return super.getNextPage();
		}

		private void storeSettings() {
		}

		private void initializeRefactoring() {
			refactoringProcessor.setNewProcessName(nameField.getText());
		}
	}
}
