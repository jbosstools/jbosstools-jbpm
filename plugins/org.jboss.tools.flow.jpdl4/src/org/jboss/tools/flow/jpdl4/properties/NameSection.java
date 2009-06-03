package org.jboss.tools.flow.jpdl4.properties;

import java.util.EventObject;

import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.flow.common.command.RenameElementCommand;
import org.jboss.tools.flow.common.properties.IPropertyId;

public class NameSection extends JpdlPropertySection implements IPropertyId {

	private Text nameText;
	private CLabel nameLabel;
	
	private ModifyListener nameTextModifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			IPropertySource input = getInput();
			if (input != null && getCommandStack() != null) {
				RenameElementCommand rec = new RenameElementCommand();
				rec.setSource(input);
				rec.setOldName((String)input.getPropertyValue(NAME));
				rec.setName(nameText.getText());
				getCommandStack().execute(rec);
			}
		}
	};
	
	private CommandStackListener commandStackListener = new CommandStackListener() {
		public void commandStackChanged(EventObject event) {
			refresh();
		}		
	};

	public void dispose() {
		if (getCommandStack() != null) {
			getCommandStack().removeCommandStackListener(commandStackListener);
		}
		super.dispose();
	}
	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		if (getCommandStack() != null) {
			getCommandStack().addCommandStackListener(commandStackListener);
		}
		Composite composite = getFlatFormComposite();
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
		data.left = new FormAttachment(JpdlPropertySection.SECOND_COLUMN_LEFT_LIMIT, 0);
		data.right = new FormAttachment(100, 0);
		nameText.setLayoutData(data);
	}

	protected void hookListeners() {
		nameText.addModifyListener(nameTextModifyListener);
	}

	protected void unhookListeners() {
		nameText.removeModifyListener(nameTextModifyListener);
	}

	protected void updateValues() {
		IPropertySource input = getInput();
		if (input != null) {
			nameText.setText(getValueNotNull((String)input.getPropertyValue(NAME)));
		} else {
			nameText.setText("");
		}
	}

}
