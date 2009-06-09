package org.jbpm.gd.common.properties;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jbpm.gd.common.model.NamedElement;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;


public class NamedElementSection extends AbstractPropertySection {
	
	private NamedElementConfigurationComposite namedElementConfigurationComposite;
	private NamedElement namedElement;
	
	public void createControls(Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite clientArea = getWidgetFactory().createFlatFormComposite(parent);
        namedElementConfigurationComposite = NamedElementConfigurationComposite.create(getWidgetFactory(), clientArea);
    }
 
 	public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        if (!(selection instanceof IStructuredSelection)) return;
        Object input = ((IStructuredSelection)selection).getFirstElement();
        if (input instanceof NotationElementGraphicalEditPart) {
        	AbstractNotationElement notationElement = ((NotationElementGraphicalEditPart)input).getNotationElement();
        	input = notationElement.getSemanticElement();
        } else if (input instanceof OutlineEditPart) {
        	input = ((OutlineEditPart)input).getModel();
        }
        if (input instanceof NamedElement) {
        	setNamedElement((NamedElement)input);
        }
    }
 	
 	private void setNamedElement(NamedElement namedElement) {
 		this.namedElement = namedElement;
 	}

 	public void refresh() {
 		if (namedElement != null) {
 			namedElementConfigurationComposite.setNamedElement(namedElement);
 		}
    }
}