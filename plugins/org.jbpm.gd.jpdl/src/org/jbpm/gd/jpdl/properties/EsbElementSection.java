package org.jbpm.gd.jpdl.properties;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.properties.AbstractPropertySection;
import org.jbpm.gd.jpdl.model.EsbElement;


public class EsbElementSection extends AbstractPropertySection {
	
	private EsbConfigurationComposite esbConfigurationComposite;
	private EsbElement esbElement;
	
	public void createControls(Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite clientArea = getWidgetFactory().createFlatFormComposite(parent);
        Composite esbInfoArea = getWidgetFactory().createComposite(clientArea);
        esbInfoArea.setLayout(new FormLayout());
        esbConfigurationComposite = EsbConfigurationComposite.create(getWidgetFactory(), esbInfoArea);
        esbInfoArea.setLayoutData(createEsbInfoAreaLayoutData());
    }
	
	private FormData createEsbInfoAreaLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.top = new FormAttachment(0, 0);
		result.right = new FormAttachment(100, 0);
		result.bottom = new FormAttachment(100, 0);
		return result;
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
        if (input instanceof EsbElement) {
        	esbElement = (EsbElement)input;
        	refresh();
        }
    }
 	
 	public void refresh() {
 		esbConfigurationComposite.setEsbElement(esbElement);
     }
 	
	public boolean shouldUseExtraSpace() {
		return true;
	}
		
}