package org.jboss.tools.flow.jpdl4.properties;

import org.jboss.tools.flow.jpdl4.model.Parameter;

public class ParameterInnerVariableSection extends TextFieldPropertySection {
	
	public ParameterInnerVariableSection() {
		super(Parameter.SUBVAR, "Subprocess Variable Name");
	}

}
