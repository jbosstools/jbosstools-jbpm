package org.jboss.tools.flow.jpdl4.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.jboss.tools.flow.common.editor.GenericModelEditor;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.editpart.JpdlEditPartFactory;
import org.jboss.tools.flow.jpdl4.properties.JpdlPropertySheetPage;

public class JpdlEditor extends GenericModelEditor implements ITabbedPropertySheetPageContributor {
	
	public static String ID = "org.jboss.tools.flow.jpdl4.editor";
	
    protected PaletteRoot createPalette() {
        return new JpdlPaletteFactory().createPalette();
    }
    
    protected org.eclipse.gef.EditPartFactory createEditPartFactory() {
        return new JpdlEditPartFactory();
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
	
	public CommandStack getCommandStack() {
		return super.getCommandStack();
	}
	
    @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
    	if (adapter == IPropertySheetPage.class)
            return new JpdlPropertySheetPage(this, getCommandStack());
        return super.getAdapter(adapter);
    }
    
}
