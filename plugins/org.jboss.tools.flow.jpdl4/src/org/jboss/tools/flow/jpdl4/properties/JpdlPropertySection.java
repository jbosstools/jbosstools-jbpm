package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.flow.jpdl4.command.ChangePropertyCommand;

public abstract class JpdlPropertySection extends AbstractPropertySection {
	
	public static final int SECOND_COLUMN_LEFT_LIMIT = 30;

	private CommandStack commandStack;
	private Composite flatFormComposite;
	private IPropertySource input;
	
	protected abstract void hookListeners();
	protected abstract void updateValues();
	protected abstract void unhookListeners();
	
	protected CommandStack getCommandStack() {
		return commandStack;
	}
	
	protected Composite getFlatFormComposite() {
		return flatFormComposite;
	}
	
	protected String getValueNullsAllowed(String string) {
		return "".equals(string) ? null : string;
	}
	
	protected String getValueNotNull(String string) {
		return string == null ? "" : string;
	}
	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		if (aTabbedPropertySheetPage instanceof JpdlPropertySheetPage) {
			commandStack = ((JpdlPropertySheetPage)aTabbedPropertySheetPage).getCommandStack();
		}
		flatFormComposite = getWidgetFactory().createFlatFormComposite(parent);
	}

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if (selection instanceof IStructuredSelection) {
			Object object = ((IStructuredSelection)selection).getFirstElement();
			if (object instanceof IAdaptable) {
				object = ((IAdaptable)object).getAdapter(IPropertySource.class);
				if (object instanceof IPropertySource) {
					input = (IPropertySource)object;
					return;
				}
			}
		}
		input = null;
	}
	
	public IPropertySource getInput() {
		return input;
	}
	
	protected void changeProperty(Object propertyId, Object newValue) {
		if (commandStack == null || input == null) return;
		Object oldValue = input.getPropertyValue(propertyId);
		if (oldValue == newValue) return;
		ChangePropertyCommand changePropertyCommand = new ChangePropertyCommand();
		changePropertyCommand.setPropertyId(propertyId);
		changePropertyCommand.setTarget(input);
		changePropertyCommand.setNewValue(newValue);
			commandStack.execute(changePropertyCommand);
	}
	
	public void aboutToBeShown() {
		refresh();
	}
	
	public void refresh() {
		unhookListeners();
		updateValues();
		hookListeners();
	}

}
