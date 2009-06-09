package org.jbpm.gd.jpdl.model;

import org.jbpm.gd.common.model.AbstractNamedElement;

public class CancelTimer extends AbstractNamedElement implements ActionElement {
	
	public String getName() {
		String result = super.getName();
		if (result == null) {
			result = "";
		}
		return result;
	}

}
