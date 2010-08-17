package org.jbpm.gd.jpdl.refactoring;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.model.WorkbenchViewerComparator;

public class MoveProcessWizard extends RefactoringWizard {

	public MoveProcessWizard(IResource resource) {
		super(new MoveRefactoring(new MoveProcessProcessor(resource)), DIALOG_BASED_USER_INTERFACE);
		setDefaultPageTitle("Move Process");
		setWindowTitle("Move Process");
	}

	protected void addUserInputPages() {
		MoveProcessProcessor processor= (MoveProcessProcessor) getRefactoring().getAdapter(MoveProcessProcessor.class);
		addPage(new MoveProcessRefactoringConfigurationPage(processor));
	}
	
	private static class MoveProcessRefactoringConfigurationPage extends UserInputWizardPage {

		private final MoveProcessProcessor refactoringProcessor;
		private TreeViewer destinationField;

		public MoveProcessRefactoringConfigurationPage(MoveProcessProcessor processor) {
			super("MoveResourcesRefactoringConfigurationPage"); 
			refactoringProcessor= processor;
		}

		public void createControl(Composite parent) {
			initializeDialogUnits(parent);
			Composite composite= new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			composite.setFont(parent.getFont());
			Label label= new Label(composite, SWT.NONE);
			IResource[] resourcesToMove= refactoringProcessor.getResourcesToMove();
			label.setText("&Choose destination for " + refactoringProcessor.getProcessToMove() + " :");
			label.setLayoutData(new GridData());
			destinationField= new TreeViewer(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
			GridData gd= new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
			gd.widthHint= convertWidthInCharsToPixels(40);
			gd.heightHint= convertHeightInCharsToPixels(15);
			destinationField.getTree().setLayoutData(gd);
			destinationField.setLabelProvider(new WorkbenchLabelProvider());
			destinationField.setContentProvider(new BaseWorkbenchContentProvider());
			destinationField.setComparator(new WorkbenchViewerComparator());
			destinationField.setInput(ResourcesPlugin.getWorkspace());
			destinationField.addFilter(new ViewerFilter() {
				public boolean select(Viewer viewer, Object parentElement, Object element) {
					if (element instanceof IProject) {
						IProject project= (IProject) element;
						return project.isAccessible();
					} else if (element instanceof IFolder) {
						return true;
					}
					return false;
				}
			});
			destinationField.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					validatePage();
				}
			});
			if (resourcesToMove.length > 0) {
				destinationField.setSelection(new StructuredSelection(resourcesToMove[0].getParent()));
			}
			setPageComplete(false);
			setControl(composite);
		}

		public void setVisible(boolean visible) {
			if (visible) {
				destinationField.getTree().setFocus();
				if (getErrorMessage() != null) {
					setErrorMessage(null); 
				}

			}
			super.setVisible(visible);
		}

		private final void validatePage() {
			RefactoringStatus status;

			IStructuredSelection selection= (IStructuredSelection) destinationField.getSelection();
			Object firstElement= selection.getFirstElement();
			if (firstElement instanceof IContainer) {
				status= refactoringProcessor.validateDestination((IContainer) firstElement);

			} else {
				status= new RefactoringStatus();
				status.addError("Select a process.");
			}
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
			IContainer container= (IContainer) ((IStructuredSelection) destinationField.getSelection()).getFirstElement();
			refactoringProcessor.setDestination(container);
		}
	}
}