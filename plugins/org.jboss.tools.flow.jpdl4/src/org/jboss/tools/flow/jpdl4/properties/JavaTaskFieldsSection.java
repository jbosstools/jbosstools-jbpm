package org.jboss.tools.flow.jpdl4.properties;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.flow.common.properties.IPropertyId;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.ModelListener;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.JavaTask;

public class JavaTaskFieldsSection extends JpdlPropertySection implements IPropertyId {
	
	private final static String NO_FIELDS = "The configured class has no fields that can be configured.";
	private final static String CONFIGURE_FIELDS = "Configure the value of the supported fields as needed.";
	
	private Wrapper input;

	private CLabel infoLabel;
	
	private ModelListener modelListener = new ModelListener() {
		public void modelChanged(ModelEvent event) {
			System.out.println("changeDiscriminator; " + event.getChangeDiscriminator());
			System.out.println("changeType " + event.getChangeType());
		}
		
	};
	
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
	public void dispose() {
		if (input != null) {
			input.removeListener(modelListener);
		}
		super.dispose();
	}
	
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getFlatFormComposite();
		createInfoLabel(composite);
		createFieldsComposite(composite);
	}
	
	
	private void createInfoLabel(Composite parent) {
		infoLabel = getWidgetFactory().createCLabel(parent, NO_FIELDS);
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

	public void setInput(IWorkbenchPart part, ISelection selection) {
		if (input != null) {
			input.removeListener(modelListener);
		}
		super.setInput(part, selection);
		if (selection instanceof IStructuredSelection) {
			Object object = ((IStructuredSelection)selection).getFirstElement();
			if (object instanceof EditPart && ((EditPart)object).getModel() instanceof Wrapper) {
				input = (Wrapper)((EditPart)object).getModel();
				input.addListener(modelListener);
				return;
			}
		}
		input = null;
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
