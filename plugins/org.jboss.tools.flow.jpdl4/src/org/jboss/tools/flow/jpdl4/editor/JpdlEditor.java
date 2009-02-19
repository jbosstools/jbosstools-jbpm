package org.jboss.tools.flow.jpdl4.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.jboss.tools.flow.common.editor.GenericModelEditor;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.editpart.JpdlGraphicalEditPartFactory;
import org.jboss.tools.flow.jpdl4.editpart.JpdlTreeEditPartFactory;
import org.jboss.tools.flow.jpdl4.properties.JpdlPropertySheetPage;
import org.jboss.tools.flow.jpdl4.view.DetailsPage;
import org.jboss.tools.flow.jpdl4.view.IDetailsPage;

public class JpdlEditor extends GenericModelEditor implements ITabbedPropertySheetPageContributor {
	
	public static String ID = "org.jboss.tools.flow.jpdl4.editor";
	
	protected SelectionSynchronizer selectionSynchronizer;
	private DetailsPage detailsPage;
	
    protected PaletteRoot createPalette() {
        return new JpdlPaletteFactory().createPalette();
    }
    
    protected org.eclipse.gef.EditPartFactory createEditPartFactory() {
        return new JpdlGraphicalEditPartFactory();
    }

    protected Object createModel() {
        return ElementRegistry.createWrapper("org.jboss.tools.flow.jpdl4.process");
    }
    
    protected void writeModel(OutputStream os) throws IOException {
        Object object = getModel();
        if (object instanceof Wrapper) {
        	new JpdlSerializer().serialize((Wrapper)object, os);
        }
    }
    
    protected void createModel(InputStream is) {
    	boolean empty = true;
    	try {
    		empty = is.available() == 0;
    	} catch (IOException e) {
    		// ignored
    	}
    	setModel(empty ? createModel() : new JpdlDeserializer().deserialize(is));
    }
    
    public SelectionSynchronizer getSelectionSynchronizer() {
    	if (selectionSynchronizer == null) {
    		selectionSynchronizer = new JpdlSelectionSynchronizer();
    		selectionSynchronizer.addViewer(getGraphicalViewer());
    	}
    	return selectionSynchronizer;
    }
    
	public String getContributorId() {
		return getSite().getId();
	}
	
	public CommandStack getCommandStack() {
		return super.getCommandStack();
	}
	
	public DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}
	
	protected DetailsPage getDetailsPage() {
		if (detailsPage == null) {
			initDetailsPage();
		}
		return detailsPage;
	}

	protected void initDetailsPage() {
		TreeViewer treeViewer = new TreeViewer();
		treeViewer.setEditPartFactory(new JpdlTreeEditPartFactory());
		getEditDomain().addViewer(treeViewer);
		getSelectionSynchronizer().addViewer(treeViewer);
		detailsPage = new DetailsPage(treeViewer) ;
		if (((Wrapper)getModel()).getElement() == null) {
			System.out.println("Ha!");
		}
		treeViewer.setContents(((Wrapper)getModel()).getElement());
		getSite().getSelectionProvider().addSelectionChangedListener(detailsPage);
	}
	
    @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
    	if (adapter == IPropertySheetPage.class)
            return new JpdlPropertySheetPage(this, getCommandStack());
    	else if (adapter == IDetailsPage.class)
    		return getDetailsPage();
        return super.getAdapter(adapter);
    }
    
}
