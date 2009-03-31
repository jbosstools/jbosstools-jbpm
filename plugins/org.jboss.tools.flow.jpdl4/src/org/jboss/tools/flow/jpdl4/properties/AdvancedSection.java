package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.properties.UndoablePropertySheetEntry;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.AdvancedPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class AdvancedSection extends AdvancedPropertySection {
	
	public void createControls(Composite parent,
			final TabbedPropertySheetPage atabbedPropertySheetPage) {
		super.createControls(parent, atabbedPropertySheetPage);
		if (atabbedPropertySheetPage instanceof JpdlPropertySheetPage) {
			CommandStack commandStack = ((JpdlPropertySheetPage)atabbedPropertySheetPage).getCommandStack();
			page.setRootEntry(new UndoablePropertySheetEntry(commandStack));
		}
	}
	
}
