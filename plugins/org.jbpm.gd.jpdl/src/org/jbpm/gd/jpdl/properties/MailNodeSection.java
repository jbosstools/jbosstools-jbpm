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
import org.jbpm.gd.jpdl.model.MailNode;


public class MailNodeSection extends AbstractPropertySection {
	
	private MailNodeConfigurationComposite mailConfigurationComposite;
	private MailNode mailNode;
	
	public void createControls(Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite clientArea = getWidgetFactory().createFlatFormComposite(parent);
        mailConfigurationComposite = MailNodeConfigurationComposite.create(getWidgetFactory(), clientArea);
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
        if (input instanceof MailNode) {
        	setMailNode((MailNode)input);
        }
    }
 	
 	private void setMailNode(MailNode mailNode) {
 		this.mailNode = mailNode;
 	}

 	public void refresh() {
 		if (mailNode != null) {
 			mailConfigurationComposite.setMailNode(mailNode);
 		}
    }
 	
	public boolean shouldUseExtraSpace() {
		return true;
	}
	
}