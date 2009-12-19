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
            "start",
            "Create a new Start Event",
            "org.jboss.tools.flow.jpdl4.startEvent",
            ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.startEvent"),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/start_event_empty.png")),
            ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/start_event_empty.png"))
        );
        entries.add(combined);        
        combined = new CombinedTemplateCreationEntry(
                "end",
                "Create a new terminating end event",
                "org.jboss.tools.flow.jpdl4.terminateEndEvent",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.terminateEndEvent"),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/end_event_terminate.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/end_event_terminate.png"))
            );
        entries.add(combined);                                  
        combined = new CombinedTemplateCreationEntry(
                "end-cancel",
                "Create a new cancel end event",
                "org.jboss.tools.flow.jpdl4.cancelEndEvent",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.cancelEndEvent"),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/end_event_cancel.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/end_event_cancel.png"))
            );
        entries.add(combined);                                  
        combined = new CombinedTemplateCreationEntry(
                "end-error",
                "Create a new error end event",
                "org.jboss.tools.flow.jpdl4.errorEndEvent",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.errorEndEvent"),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/end_event_error.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/end_event_error.png"))
            );
        entries.add(combined);                                  
        return entries;
    }
    
    protected List<PaletteEntry> createTaskEntries() {
        List<PaletteEntry> entries = new ArrayList<PaletteEntry>();        
        CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
                "state",
                "Create a new State Task",
                "org.jboss.tools.flow.jpdl4.waitTask",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.waitTask"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/task_empty.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/task_empty.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "hql",
                "Create a new HQL Task",
                "org.jboss.tools.flow.jpdl4.hqlTask",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.hqlTask"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/task_empty.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/task_empty.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "sql",
                "Create a new SQL Task",
                "org.jboss.tools.flow.jpdl4.sqlTask",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.sqlTask"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/task_empty.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/task_empty.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "jms",
                "Create a new JMS Node",
                "org.jboss.tools.flow.jpdl4.jmsTask",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.jmsTask"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/task_empty.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/task_empty.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "custom",
                "Create a new Custom Node",
                "org.jboss.tools.flow.jpdl4.customTask",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.customTask"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/task_empty.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/task_empty.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "java",
                "Create a new Java Task",
                "org.jboss.tools.flow.jpdl4.javaTask",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.javaTask"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/task_empty.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/task_empty.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "script",
                "Create a new Script Task",
                "org.jboss.tools.flow.jpdl4.scriptTask",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.scriptTask"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/task_empty.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/task_empty.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "rule",
                "Create a new Rule Task",
                "org.jboss.tools.flow.jpdl4.ruleTask",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.ruleTask"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/task_empty.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/task_empty.png"))
            );
        entries.add(combined);
//        combined = new CombinedTemplateCreationEntry(
//                "esb",
//                "Create a new Service Task",
//                "org.jboss.tools.flow.jpdl4.serviceTask",
//                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.serviceTask"),                
//                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/task_empty.png")),
//                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/task_empty.png"))
//            );
//        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "task",
                "Create a new Human Task",
                "org.jboss.tools.flow.jpdl4.humanTask",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.humanTask"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/task_empty.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/task_empty.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "sub-process",
                "Create a new Subprocess",
                "org.jboss.tools.flow.jpdl4.subprocess",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.subprocessTask"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/task_empty.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/task_empty.png"))
            );
        entries.add(combined);
        return entries;
    }
    
    protected List<PaletteEntry> createGatewayEntries() {
        List<PaletteEntry> entries = new ArrayList<PaletteEntry>();        
        CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
                "decision",
                "Create a new Decision",
                "org.jboss.tools.flow.jpdl4.exclusiveGateway",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.exclusiveGateway"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/gateway_exclusive.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/gateway_exclusive.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "rule",
                "Create a new Rules Decision",
                "org.jboss.tools.flow.jpdl4.rulesDecision",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.exclusiveGateway"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/gateway_exclusive.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/gateway_exclusive.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "fork",
                "Create a new Fork",
                "org.jboss.tools.flow.jpdl4.parallelForkGateway",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.parallelForkGateway"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/gateway_parallel.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/gateway_parallel.png"))
            );
        entries.add(combined);
        combined = new CombinedTemplateCreationEntry(
                "join",
                "Create a new Join",
                "org.jboss.tools.flow.jpdl4.parallelJoinGateway",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.parallelJoinGateway"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/gateway_parallel.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/gateway_parallel.png"))
            );
        entries.add(combined);
        return entries;
    }
    
    protected List<PaletteEntry> createFlowEntries() {
        List<PaletteEntry> entries = new ArrayList<PaletteEntry>();        
        ToolEntry tool = new ConnectionCreationToolEntry(
                "transition",
                "Create a new Transition",
                ElementRegistry.getCreationFactory("org.jboss.tools.flow.jpdl4.sequenceFlow"),                
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/16/flow_sequence.png")),
                ImageDescriptor.createFromURL(Activator.getDefault().getBundle().getEntry("icons/32/flow_sequence.png"))
            );
        entries.add(tool);
        return entries;
    }
    
}
