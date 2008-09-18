package org.jboss.tools.flow.jpdl4.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.PaletteRoot;
import org.jboss.tools.flow.common.editor.GenericModelEditor;
import org.jboss.tools.flow.common.editpart.DefaultEditPartFactory;
import org.jboss.tools.flow.common.registry.ElementRegistry;

public class JpdlEditor extends GenericModelEditor {

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
        // TODO
    }
    
    protected void createModel(InputStream is) {
        setModel(createModel());
    }
}
