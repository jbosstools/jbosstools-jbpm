package org.jboss.tools.flow.jpdl4.graph.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.PaletteRoot;
import org.jboss.tools.flow.editor.GenericModelEditor;
import org.jboss.tools.flow.jpdl4.graph.editpart.JpdlEditPartFactory;
import org.jboss.tools.flow.jpdl4.graph.wrapper.ProcessWrapper;

public class JpdlEditor extends GenericModelEditor {

    protected EditPartFactory createEditPartFactory() {
        return new JpdlEditPartFactory();
    }

    protected PaletteRoot createPalette() {
        return new JpdlPaletteFactory().createPalette();
    }

    protected Object createModel() {
        return new ProcessWrapper();
    }
    
    protected void writeModel(OutputStream os) throws IOException {
        // TODO
    }
    
    protected void createModel(InputStream is) {
        // TODO
        setModel(createModel());
    }
}
