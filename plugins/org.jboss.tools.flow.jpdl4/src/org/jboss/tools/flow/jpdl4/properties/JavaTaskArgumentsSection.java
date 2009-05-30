package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.flow.common.properties.IPropertyId;

public class JavaTaskArgumentsSection extends JpdlPropertySection implements IPropertyId {
	
	private final static String NO_ARGUMENTS = "The configured method has no arguments that can be configured.";
	private final static String CONFIGURE_ARGUMENTS = "Configure the value of the supported arguments as needed.";

	private CLabel infoLabel;
	
//	private ModifyListener nameTextModifyListener = new ModifyListener() {
//		public void modifyText(ModifyEvent arg0) {
//			IPropertySource input = getInput();
//			if (input != null) {
//				RenameElementCommand rec = new RenameElementCommand();
//				rec.setSource(input);
//				rec.setOldName((String)input.getPropertyValue(NAME));
//				rec.setName(nameText.getText());
//				getCommandStack().execute(rec);
//			}
//		}
//	};
//	
//	private CommandStackListener commandStackListener = new CommandStackListener() {
//		public void commandStackChanged(EventObject event) {
//			refresh();
//		}		
//	};
//
//	public void dispose() {
//		if (getCommandStack() != null) {
//			getCommandStack().removeCommandStackListener(commandStackListener);
//		}
//		super.dispose();
//	}
	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getFlatFormComposite();
		createInfoLabel(composite);
		createFieldsComposite(composite);
	}
	
	
	private void createInfoLabel(Composite parent) {
		infoLabel = getWidgetFactory().createCLabel(parent, NO_ARGUMENTS);
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.top = new FormAttachment(0, 5);
		data.right = new FormAttachment(100, 0);
		infoLabel.setLayoutData(data);
	}
	
	private void createFieldsComposite(Composite parent) {
		Composite fieldsComposite = getWidgetFactory().createComposite(parent);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(JpdlPropertySection.SECOND_COLUMN_LEFT_LIMIT, 0);
		data.right = new FormAttachment(100, 0);
		fieldsComposite.setLayoutData(data);
	}

	protected void hookListeners() {
//		nameText.addModifyListener(nameTextModifyListener);
	}

	protected void unhookListeners() {
//		nameText.removeModifyListener(nameTextModifyListener);
	}

	protected void updateValues() {
//		IPropertySource input = getInput();
//		if (input != null) {
//			nameText.setText(getValueNotNull((String)input.getPropertyValue(NAME)));
//		} else {
//			nameText.setText("");
//		}
	}

}
