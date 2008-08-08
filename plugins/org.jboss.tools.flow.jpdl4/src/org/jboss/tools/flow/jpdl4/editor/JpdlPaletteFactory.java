package org.jboss.tools.flow.jpdl4.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.flow.common.editor.PaletteFactory;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.jpdl4.Activator;
import org.jboss.tools.flow.jpdl4.wrapper.EndStateWrapper;
import org.jboss.tools.flow.jpdl4.wrapper.StartStateWrapper;
import org.jboss.tools.flow.jpdl4.wrapper.StateWrapper;
import org.jboss.tools.flow.jpdl4.wrapper.SuperStateWrapper;

public class JpdlPaletteFactory extends PaletteFactory {
    
    protected List<PaletteEntry> createComponentEntries() {
        List<PaletteEntry> entries = new ArrayList<PaletteEntry>();
        
        CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
            "Start",
            "Create a new Start State",
            StartStateWrapper.class,
            ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.startState"),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/start.gif")),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/start.gif"))
        );
        entries.add(combined);
        
        combined = new CombinedTemplateCreationEntry(
                "State",
                "Create a new State",
                StateWrapper.class,
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.state"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/state.gif")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/state.gif"))
            );
        entries.add(combined);
                              
        combined = new CombinedTemplateCreationEntry(
                "End",
                "Create a new End State",
                EndStateWrapper.class,
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.endState"),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/end.gif")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/end.gif"))
            );
        entries.add(combined);
                                  
        combined = new CombinedTemplateCreationEntry(
                "Super State",
                "Create a new Super State",
                SuperStateWrapper.class,
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.superState"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/super.gif")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/super.gif"))
            );
        entries.add(combined);
                                  
        ToolEntry tool = new ConnectionCreationToolEntry(
                "Transition",
                "Creating a new Transition",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.transition"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/transition.gif")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/transition.gif"))
            );
        entries.add(tool);
            
        return entries;
    }
    
}
