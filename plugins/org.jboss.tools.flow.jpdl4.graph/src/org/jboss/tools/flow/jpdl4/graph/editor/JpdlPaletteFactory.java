package org.jboss.tools.flow.jpdl4.graph.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.flow.editor.PaletteFactory;
import org.jboss.tools.flow.jpdl4.graph.Activator;
import org.jboss.tools.flow.jpdl4.graph.wrapper.EndStateWrapper;
import org.jboss.tools.flow.jpdl4.graph.wrapper.StartStateWrapper;
import org.jboss.tools.flow.jpdl4.graph.wrapper.StateWrapper;
import org.jboss.tools.flow.jpdl4.graph.wrapper.SuperStateWrapper;
import org.jboss.tools.flow.jpdl4.graph.wrapper.TransitionWrapperFactory;

public class JpdlPaletteFactory extends PaletteFactory {
    
    public JpdlPaletteFactory() {
        super(new TransitionWrapperFactory());
    }

    protected List<PaletteEntry> createComponentEntries() {
        List<PaletteEntry> entries = new ArrayList<PaletteEntry>();
        
        CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
            "Start",
            "Create a new Start State",
            StartStateWrapper.class,
            new SimpleFactory(StartStateWrapper.class),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/start.gif")),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/start.gif"))
        );
        entries.add(combined);
        
        combined = new CombinedTemplateCreationEntry(
                "State",
                "Create a new State",
                StateWrapper.class,
                new SimpleFactory(StateWrapper.class),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/state.gif")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/state.gif"))
            );
        entries.add(combined);
                              
        combined = new CombinedTemplateCreationEntry(
                "End",
                "Create a new End State",
                EndStateWrapper.class,
                new SimpleFactory(EndStateWrapper.class),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/end.gif")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/end.gif"))
            );
        entries.add(combined);
                                  
        combined = new CombinedTemplateCreationEntry(
                "Super State",
                "Create a new Super State",
                SuperStateWrapper.class,
                new SimpleFactory(SuperStateWrapper.class),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/super.gif")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/super.gif"))
            );
        entries.add(combined);
                                  
        return entries;
    }
    
}
