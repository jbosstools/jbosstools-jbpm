package org.jbpm.gd.jpdl.properties;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.properties.AbstractPropertySection;
import org.jbpm.gd.jpdl.model.DescribableElement;


public class DescribableElementSection extends AbstractPropertySection {
	
	private DescribableElementConfigurationComposite describableElementConfigurationComposite;
	private DescribableElement describableElement;
	
	public void createControls(Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite clientArea = getWidgetFactory().createFlatFormComposite(parent);
        describableElementConfigurationComposite = DescribableElementConfigurationComposite.create(getWidgetFactory(), clientArea);
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
        if (input instanceof DescribableElement) {
        	setDescribableElement((DescribableElement)input);
        }
    }
 	
 	private void setDescribableElement(DescribableElement describableElement) {
 		this.describableElement = describableElement;
 	}

 	public void refresh() {
 		if (describableElement != null) {
 			describableElementConfigurationComposite.setDescribableElement(describableElement);
 		}
    }
 	
	public boolean shouldUseExtraSpace() {
		return true;
	}
	
 	
}