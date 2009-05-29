package org.jboss.tools.flow.jpdl4.editpart;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.SubprocessTask;

public class SubprocessTaskTreeRootEditPart extends TaskTreeRootEditPart {

	public SubprocessTaskTreeRootEditPart(Wrapper wrapper) {
		super(wrapper);
	}

	protected List<Object> getModelChildren() {
		List<Object> result = new ArrayList<Object>();
		addInputParameters(result, (Wrapper)getModel());
		addOutputParameters(result, (Wrapper)getModel());
		result.addAll(super.getModelChildren());
		return result;
	}
	
	private void addInputParameters(List<Object> list, Wrapper wrapper) {
		List<Element> inputParameters = wrapper.getChildren(SubprocessTask.INPUT_PARAMETERS);
		if (inputParameters != null && !inputParameters.isEmpty()) {
			list.add(new InputParameterListTreeEditPart(inputParameters));
		}
	}
	
	private void addOutputParameters(List<Object> list, Wrapper wrapper) {
		List<Element> outputParameters = wrapper.getChildren(SubprocessTask.OUTPUT_PARAMETERS);
		if (outputParameters != null && !outputParameters.isEmpty()) {
			list.add(new OutputParameterListTreeEditPart(outputParameters));
		}
	}
	
	
}
