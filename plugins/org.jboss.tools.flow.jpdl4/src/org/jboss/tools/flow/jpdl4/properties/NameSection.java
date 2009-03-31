package org.jboss.tools.flow.jpdl4.properties;

import java.util.EventObject;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.flow.common.command.RenameElementCommand;
import org.jboss.tools.flow.common.properties.IPropertyId;

public class NameSection extends AbstractPropertySection implements IPropertyId {

	private Text nameText;
	private CLabel nameLabel;
	
	private IPropertySource input;
	private CommandStack commandStack;

	private ModifyListener nameTextModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			if (input != null) {
				RenameElementCommand rec = new RenameElementCommand();
				rec.setSource(input);
				rec.setOldName((String)input.getPropertyValue(NAME));
				rec.setName(nameText.getText());
				commandStack.execute(rec);
			}
		}
	};
	
	private CommandStackListener commandStackListener = new CommandStackListener() {
		public void commandStackChanged(EventObject event) {
			refresh();
		}		
	};

	public void dispose() {
		if (commandStack != null) {
			commandStack.removeCommandStackListener(commandStackListener);
		}
		super.dispose();
	}
	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		if (aTabbedPropertySheetPage instanceof JpdlPropertySheetPage) {
			commandStack = ((JpdlPropertySheetPage)aTabbedPropertySheetPage).getCommandStack();
			commandStack.addCommandStackListener(commandStackListener);
		}
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);
		createNameLabel(composite);
		createNameText(composite);
	}
	
	
	private void createNameLabel(Composite parent) {
		nameLabel = getWidgetFactory().createCLabel(parent, "Name");
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 5);
		nameLabel.setLayoutData(data);
	}
	
	private void createNameText(Composite parent) {
		nameText = getWidgetFactory().createText(parent, "");
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(nameLabel, 0, SWT.RIGHT);
		data.right = new FormAttachment(100, 0);
		nameText.setLayoutData(data);
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

	public void refresh() {
		if (input != null) {
			String value = (String)input.getPropertyValue(NAME);
			nameText.removeModifyListener(nameTextModifyListener);
			nameText.setText(value == null ? "" : value);
			nameText.addModifyListener(nameTextModifyListener);
		} else {
			nameText.setText("");
		}
	}

}
