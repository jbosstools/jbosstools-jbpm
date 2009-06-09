package org.jbpm.gd.jpdl.xml;

import java.beans.PropertyChangeEvent;

import org.jbpm.gd.common.model.SemanticElement;
import org.jbpm.gd.common.xml.XmlAdapter;
import org.jbpm.gd.jpdl.model.SubProcess;


public class SubProcessDomAdapter extends XmlAdapter {
	
	protected void initialize() {
		super.initialize();
		SubProcess subProcess = (SubProcess)getSemanticElement();
		if (subProcess != null) {
			setAttribute("name", subProcess.getName());
			setAttribute("version", subProcess.getVersion());
		}
	}
	
	public void initialize(SemanticElement jpdlElement) {
		super.initialize(jpdlElement);
		SubProcess subProcess = (SubProcess)jpdlElement;
		subProcess.setName(getAttribute("name"));
		subProcess.setVersion(getAttribute("version"));
		subProcess.addPropertyChangeListener(this);
	}
	
	protected void doPropertyChange(PropertyChangeEvent evt) {
		if ("name".equals(evt.getPropertyName())) {
			setAttribute("name", (String)evt.getNewValue());
		} else if ("version".equals(evt.getPropertyName())) {
			setAttribute("version", (String)evt.getNewValue());
		}
	}
	
	protected void doModelUpdate(String name, String newValue) {
		SubProcess subProcess = (SubProcess)getSemanticElement();
		if ("name".equals(name)) {
			subProcess.setName(newValue);
		} else if ("version".equals(name)) {
			subProcess.setVersion(newValue);
		}
	}
	
	protected void doModelAdd(XmlAdapter child) {
		// a subprocess cannot have any child nodes
	}
	
	protected void doModelRemove(XmlAdapter child) {
		// a subprocess cannot have any child nodes
	}
	
}
