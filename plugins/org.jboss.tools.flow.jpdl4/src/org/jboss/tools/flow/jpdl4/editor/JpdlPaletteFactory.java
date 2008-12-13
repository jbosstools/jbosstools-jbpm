package org.jboss.tools.flow.jpdl4.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.flow.common.editor.PaletteFactory;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.jpdl4.Activator;

public class JpdlPaletteFactory extends PaletteFactory {
    
//    protected List<PaletteContainer> createCategories(PaletteRoot root) {
//        List<PaletteContainer> categories = new ArrayList<PaletteContainer>();
//        categories.add(createControlGroup(root));
//        categories.add(createEventsDrawer());
//        categories.add(createTasksDrawer());
//        categories.add(createGatewayDrawer());
//        categories.add(createFlowDrawer());
//        return categories;
//    }
	
    protected List<PaletteEntry> createComponentEntries() {
    	ArrayList<PaletteEntry> componentEntries = new ArrayList<PaletteEntry>();
    	componentEntries.addAll(createFlowEntries());
    	componentEntries.addAll(createEventEntries());
    	componentEntries.addAll(createTaskEntries());
    	componentEntries.addAll(createGatewayEntries());
    	return componentEntries;
    }
    
    protected PaletteContainer createEventsDrawer() {
        PaletteDrawer drawer = new PaletteDrawer("Events", null);
        List<PaletteEntry> entries = createEventEntries();
        drawer.addAll(entries);
        return drawer;
    }
    
    protected PaletteContainer createTasksDrawer() {
        PaletteDrawer drawer = new PaletteDrawer("Tasks", null);
        List<PaletteEntry> entries = createTaskEntries();
        drawer.addAll(entries);
        return drawer;
    }
    
    protected PaletteContainer createGatewayDrawer() {
        PaletteDrawer drawer = new PaletteDrawer("Gateways", null);
        List<PaletteEntry> entries = createGatewayEntries();
        drawer.addAll(entries);
        return drawer;
    }
    
    protected PaletteContainer createFlowDrawer() {
        PaletteDrawer drawer = new PaletteDrawer("Flows", null);
        List<PaletteEntry> entries = createFlowEntries();
        drawer.addAll(entries);
        return drawer;
    }
    
    protected List<PaletteEntry> createEventEntries() {
        List<PaletteEntry> entries = new ArrayList<PaletteEntry>();       
        CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
            "Start",
            "Create a new Start Event",
            "org.jboss.tools.flow.jpdl4.startEvent",
            ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.startEvent"),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/start_event_empty.png")),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/start_event_empty.png"))
        );
        entries.add(combined);        
        combined = new CombinedTemplateCreationEntry(
                "End",
                "Create a new End Event",
                "org.jboss.tools.flow.jpdl4.endEvent",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.endEvent"),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/end_event_terminate.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/end_event_terminate.png"))
            );
        entries.add(combined);                                  
        return entries;
    }
    
    protected List<PaletteEntry> createTaskEntries() {
        List<PaletteEntry> entries = new ArrayList<PaletteEntry>();        
        CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
                "Wait State",
                "Create a new Wait State Task",
                "org.jboss.tools.flow.jpdl4.stateTask",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.stateTask"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/task_empty.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/task_empty.png"))
            );
        entries.add(combined);
        return entries;
    }
    
    protected List<PaletteEntry> createGatewayEntries() {
        List<PaletteEntry> entries = new ArrayList<PaletteEntry>();        
        CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
                "Exclusive",
                "Create a new Exclusive Gateway",
                "org.jboss.tools.flow.jpdl4.exclusiveGateway",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.exclusiveGateway"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/gateway_exclusive.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/gateway_exclusive.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "Parallel Fork",
                "Create a new Parallel Fork Gateway",
                "org.jboss.tools.flow.jpdl4.parallelForkGateway",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.parallelForkGateway"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/gateway_parallel.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/gateway_parallel.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "Parallel Join",
                "Create a new Parallel Join Gateway",
                "org.jboss.tools.flow.jpdl4.parallelJoinGateway",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.parallelJoinGateway"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/gateway_parallel.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/gateway_parallel.png"))
            );
        entries.add(combined);
        return entries;
    }
    
    protected List<PaletteEntry> createFlowEntries() {
        List<PaletteEntry> entries = new ArrayList<PaletteEntry>();        
        ToolEntry tool = new ConnectionCreationToolEntry(
                "Sequence",
                "Creating a new Sequence Flow",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.sequenceFlow"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/flow_sequence.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/small/flow_sequence.png"))
            );
        entries.add(tool);
        return entries;
    }
    
}
