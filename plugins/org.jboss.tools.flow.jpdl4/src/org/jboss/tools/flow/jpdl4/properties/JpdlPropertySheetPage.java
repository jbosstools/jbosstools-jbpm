package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class JpdlPropertySheetPage extends TabbedPropertySheetPage {
	
	private CommandStack commandStack;

	public JpdlPropertySheetPage(
			ITabbedPropertySheetPageContributor tabbedPropertySheetPageContributor,
			CommandStack commandStack) {
		super(tabbedPropertySheetPageContributor);
		this.commandStack = commandStack;
	}
	
	protected CommandStack getCommandStack() {
		return commandStack;
	}
	
	

}
