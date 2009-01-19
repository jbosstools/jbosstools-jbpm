package org.jboss.tools.flow.jpdl4.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.flow.common.editor.GenericModelEditor;
import org.jboss.tools.flow.common.editpart.DefaultEditPartFactory;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class JpdlEditor extends GenericModelEditor implements ITabbedPropertySheetPageContributor {
	
	public static String ID = "org.jboss.tools.flow.jpdl4.editor";

    protected EditPartFactory createEditPartFactory() {
        return new DefaultEditPartFactory();
    }

    protected PaletteRoot createPalette() {
        return new JpdlPaletteFactory().createPalette();
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
    
	public String getContributorId() {
		return getSite().getId();
	}

    @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
        if (adapter == IPropertySheetPage.class)
            return new TabbedPropertySheetPage(this);
        return super.getAdapter(adapter);
    }
    
}
