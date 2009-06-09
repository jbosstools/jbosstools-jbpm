package org.jbpm.gd.jpdl.properties;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jbpm.gd.common.notation.AbstractNotationElement;
import org.jbpm.gd.common.part.NotationElementGraphicalEditPart;
import org.jbpm.gd.common.part.OutlineEditPart;
import org.jbpm.gd.common.properties.AbstractPropertySection;
import org.jbpm.gd.jpdl.model.Action;
import org.jbpm.gd.jpdl.model.Node;


public class NodeActionSection extends AbstractPropertySection {
	
	private ActionConfigurationComposite actionConfigurationComposite;
	private Node node;
	
	private Button configureActionButton;
	
	public void createControls(Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite clientArea = getWidgetFactory().createFlatFormComposite(parent);
        configureActionButton = getWidgetFactory().createButton(clientArea, "Configure Action", SWT.CHECK);
        configureActionButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleConfigureActionButtonSelected();
			}
        });
        Composite actionInfoArea = getWidgetFactory().createComposite(clientArea);
        actionInfoArea.setLayout(new FormLayout());
        actionConfigurationComposite = ActionConfigurationComposite.create(getWidgetFactory(), actionInfoArea);
        configureActionButton.setLayoutData(createConfigureActionButtonLayoutData());
        actionInfoArea.setLayoutData(createActionInfoAreaLayoutData());
    }
	
	private void handleConfigureActionButtonSelected() {
		if (node != null) {
			if (configureActionButton.getSelection()) {
				node.setAction((Action)node.getFactory().createById("org.jbpm.gd.jpdl.action"));
			} else {
				node.setAction(null);
			}
		}
		refresh();
	}
	
	private FormData createConfigureActionButtonLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 5);
		result.top = new FormAttachment(0, 5);
		return result;
	}
	
	private FormData createActionInfoAreaLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.top = new FormAttachment(configureActionButton, 5);
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
        if (input instanceof Node) {
        	node = (Node)input;
        	refresh();
        }
    }
 	
 	public void refresh() {
 		if (node == null){
 			configureActionButton.setSelection(false);
 			actionConfigurationComposite.setAction(null);
 		} else {
 			configureActionButton.setSelection(node.getAction() != null);
 			actionConfigurationComposite.setAction(node.getAction());
 		}
     }
 	
	public boolean shouldUseExtraSpace() {
		return true;
	}
		
}