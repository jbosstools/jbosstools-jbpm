package org.jbpm.gd.jpdl.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.jbpm.gd.jpdl.model.ProcessState;
import org.jbpm.gd.jpdl.model.SubProcess;

public class SubProcessConfigurationComposite implements SelectionListener {
	
	public static SubProcessConfigurationComposite create(TabbedPropertySheetWidgetFactory widgetFactory, Composite parent) {
		SubProcessConfigurationComposite result = new SubProcessConfigurationComposite();
		result.widgetFactory = widgetFactory;
		result.parent = parent;
		result.create();
		return result;
	}
		
	private TabbedPropertySheetWidgetFactory widgetFactory;
	private Composite parent;
	
    private ProcessState processState;
    
    private Label subProcessNameLabel;
    private Text subProcessNameText;
    private Button subProcessVersionButton;
    private Text subProcessVersionText;
    private Composite variableContainerHolder;
    private VariableContainerConfigurationComposite variableContainerConfigurationComposite;
    
	private SubProcessConfigurationComposite() {}
	
	public void setProcessState(ProcessState processState) {
		if (this.processState == processState) return;
		unhookListeners();
		this.processState = processState;
		clearControls();
		if (processState != null) {
			updateControls();
			hookListeners();
		}
	}
	
	private void hookListeners() {
		subProcessNameText.addSelectionListener(this);
		subProcessVersionButton.addSelectionListener(this);
		subProcessVersionText.addSelectionListener(this);
	}
	
	private void unhookListeners() {
		subProcessNameText.removeSelectionListener(this);
		subProcessVersionButton.removeSelectionListener(this);
		subProcessVersionText.removeSelectionListener(this);
	}
	
	private void clearControls() {
		subProcessNameText.setText("");
		subProcessVersionButton.setSelection(false);
		subProcessVersionText.setEnabled(false);
		subProcessVersionText.setText("");
		variableContainerConfigurationComposite.setVariableContainer(null);
	}
	
	private void updateControls() {
		SubProcess subProcess = processState.getSubProcess();
		subProcessNameText.setText(subProcess.getName());
		boolean versionEnabled = subProcess.getVersion() != null;
		subProcessVersionButton.setSelection(versionEnabled);
		subProcessVersionText.setEnabled(versionEnabled);
		subProcessVersionText.setText(versionEnabled ? subProcess.getVersion() : "");
		variableContainerConfigurationComposite.setVariableContainer(processState);
	}
	
	private void create() {
		subProcessNameLabel = widgetFactory.createLabel(parent, "Subprocess Name");
		subProcessNameText = widgetFactory.createText(parent, "");
		subProcessVersionButton = widgetFactory.createButton(parent, "Version", SWT.CHECK);
		subProcessVersionText = widgetFactory.createText(parent, "");
		subProcessVersionText.setEnabled(false);
		variableContainerHolder = widgetFactory.createComposite(parent);
		variableContainerHolder.setLayout(new FormLayout());
		variableContainerConfigurationComposite = VariableContainerConfigurationComposite.create(widgetFactory, variableContainerHolder);
		subProcessNameLabel.setLayoutData(createSubProcessNameLabelLayoutData());
		subProcessNameText.setLayoutData(createSubProcessNameTextLayoutData());
		subProcessVersionButton.setLayoutData(createSubProcessVersionButtonLayoutData());
		subProcessVersionText.setLayoutData(createSubProcessVersionTextLayoutData());
		variableContainerHolder.setLayoutData(createVariableContainerHolderLayoutData());
	}
		
	private FormData createSubProcessNameLabelLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 5);
		result.top = new FormAttachment(0, 2);
		return result;
	}
	
	private FormData createSubProcessNameTextLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(subProcessNameLabel, 0);
		result.top = new FormAttachment(0, 0);
		result.right = new FormAttachment(50, 0);
		return result;
	}
	
	private FormData createSubProcessVersionButtonLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(subProcessNameText, 0);
		result.top = new FormAttachment(0, 2);
		return result;
	}
	
	private FormData createSubProcessVersionTextLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(subProcessVersionButton, 0);
		result.right = new FormAttachment(100, -5);
		result.top = new FormAttachment(0, 0);
		return result;
	}
	
	private FormData createVariableContainerHolderLayoutData() {
		FormData result = new FormData();
		result.left = new FormAttachment(0, 0);
		result.top = new FormAttachment(subProcessNameText, 0);
		result.right = new FormAttachment(100, 0);
		result.bottom = new FormAttachment(100, 0);
		result.height = 100;
		return result;
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		if (e.widget == subProcessNameText) {
			processState.getSubProcess().setName(subProcessNameText.getText());
		} else if (e.widget == subProcessVersionText) {
			processState.getSubProcess().setVersion(subProcessVersionText.getText());
		}
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.widget == subProcessVersionButton) {
			if (subProcessVersionButton.getSelection()) {
				processState.getSubProcess().setVersion(subProcessVersionText.getText());
				subProcessVersionText.setEnabled(true);
				subProcessVersionText.setFocus();
				subProcessVersionText.selectAll();
			} else {
				processState.getSubProcess().setVersion(null);
				subProcessVersionText.setEnabled(false);
				subProcessVersionText.setText("");
			}
		}
	}
	
}
