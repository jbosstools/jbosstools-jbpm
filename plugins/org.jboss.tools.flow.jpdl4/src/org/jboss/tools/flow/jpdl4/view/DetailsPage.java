package org.jboss.tools.flow.jpdl4.view;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.Page;
import org.jboss.tools.flow.jpdl4.editor.JpdlEditor;
import org.jboss.tools.flow.jpdl4.editpart.JpdlTreeEditPartFactory;

public class DetailsPage extends Page implements IDetailsPage, ISelectionChangedListener {
	
    private EditPartViewer viewer;
    private Control control;
    private JpdlEditor jpdlEditor;
    
    public DetailsPage(JpdlEditor jpdlEditor) {
        viewer = new TreeViewer();
		viewer.setEditPartFactory(new JpdlTreeEditPartFactory());
		this.jpdlEditor = jpdlEditor;
		jpdlEditor.getEditDomain().addViewer(viewer);
    }
    
    public Control getControl() {
    	return control;
    }
    
    protected EditPartViewer getViewer() {
    	return viewer;
    }
    
    public JpdlEditor getContributingEditor() {
    	return jpdlEditor;
    }
    
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        getViewer().addSelectionChangedListener(listener);
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        getViewer().removeSelectionChangedListener(listener);
    }

    public void createControl(Composite parent) {
    	control = getViewer().createControl(parent);
    }

    public ISelection getSelection() {
        if (getViewer() == null) {
			return StructuredSelection.EMPTY;
		}
        return getViewer().getSelection();
    }

    public void setFocus() {
    	if (getControl() != null) {
    		getControl().setFocus();
    	}
    }

    public void setSelection(ISelection selection) {
        if (getViewer() != null) {
			getViewer().setSelection(selection);
		}
    }

	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			Object object = ((IStructuredSelection)selection).getFirstElement();
			if (object instanceof EditPart) {
				object = ((EditPart)object).getModel();
			}
			if (getViewer() != null) {
				getViewer().setContents(object);
			}
		}
	}
	
	
}
