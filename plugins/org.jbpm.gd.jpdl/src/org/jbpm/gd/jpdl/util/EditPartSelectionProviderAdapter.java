package org.jbpm.gd.jpdl.util;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;

public class EditPartSelectionProviderAdapter implements ISelectionProvider {
	
	ISelectionProvider target;
	
	public EditPartSelectionProviderAdapter(ISelectionProvider target) {
		this.target = target;
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
	}

	public ISelection getSelection() {
		ISelection selection = target.getSelection();
		if (selection != null && selection instanceof StructuredSelection) {
			Object object = ((StructuredSelection)selection).getFirstElement();
			return new StructuredSelection(((EditPart)object).getModel());
		}
		return target.getSelection();
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
	}

	public void setSelection(ISelection selection) {
	}
	
}

