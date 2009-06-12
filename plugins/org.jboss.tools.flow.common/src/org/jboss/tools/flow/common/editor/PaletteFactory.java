package org.jboss.tools.flow.common.editor;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;

/**
 * Factory for creating a RuleFlow palette.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class PaletteFactory {
    
    public PaletteRoot createPalette() {
        PaletteRoot palette = new PaletteRoot();
        palette.addAll(createCategories(palette));
        return palette;
    }
    
    protected List<PaletteContainer> createCategories(PaletteRoot root) {
        List<PaletteContainer> categories = new ArrayList<PaletteContainer>();
        categories.add(createControlGroup(root));
        categories.add(createComponentsDrawer());
        return categories;
    }

    protected PaletteContainer createControlGroup(PaletteRoot root) {
        PaletteGroup controlGroup = new PaletteGroup("Control Group");

        List<PaletteEntry> entries = new ArrayList<PaletteEntry>();

        ToolEntry tool = new SelectionToolEntry();
        entries.add(tool);
        root.setDefaultEntry(tool);

        tool = new MarqueeToolEntry();
        entries.add(tool);
        
        controlGroup.addAll(entries);
        return controlGroup;
    }
    
    protected PaletteContainer createComponentsDrawer() {
        PaletteDrawer drawer = new PaletteDrawer("Components", null);
        List<PaletteEntry> entries = createComponentEntries();
        drawer.addAll(entries);
        return drawer;
    }
    
    protected List<PaletteEntry> createComponentEntries() {
    	return new ArrayList<PaletteEntry>();
    }
    
}
