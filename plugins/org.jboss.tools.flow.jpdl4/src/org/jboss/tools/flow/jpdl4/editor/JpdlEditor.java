package org.jboss.tools.flow.jpdl4.editor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.palette.PaletteRoot;
import org.jboss.tools.flow.common.editor.GenericModelEditor;
import org.jboss.tools.flow.common.editpart.DefaultEditPartFactory;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.ContainerWrapper;
import org.jboss.tools.flow.common.wrapper.NodeWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;

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
    
    private void write(Wrapper wrapper, OutputStream os) {
    	Object object = wrapper.getElement();
        if (object instanceof Element) {
        	Element element = (Element)object;
        	String xml = (String)element.getMetaData("xml");
            System.out.println(xml);
        }
        if (wrapper instanceof ContainerWrapper) {
        	ContainerWrapper containerWrapper = (ContainerWrapper)wrapper;
            List<NodeWrapper> children = containerWrapper.getElements();
            for (NodeWrapper nodeWrapper : children) {
    			write(nodeWrapper, os);
    		}
        }
        if (wrapper instanceof NodeWrapper) {
        	NodeWrapper nodeWrapper = (NodeWrapper)wrapper;
        	List<ConnectionWrapper> children = nodeWrapper.getOutgoingConnections();
        	for (ConnectionWrapper connectionWrapper : children) {
        		write(connectionWrapper, os);
        	}
        }
    }
    
    protected void writeModel(OutputStream os) throws IOException {
        Object object = getModel();
        if (object instanceof Wrapper) {
        	write((Wrapper)object, os);
        }
    }
    
    protected void createModel(InputStream is) {
        setModel(createModel());
    }
}
