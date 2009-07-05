package org.jboss.tools.flow.jpdl4.editpart;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.QueryTask;

public class QueryTaskTreeRootEditPart extends TaskTreeRootEditPart {

	public QueryTaskTreeRootEditPart(Wrapper wrapper) {
		super(wrapper);
	}

	protected List<Object> getModelChildren() {
		List<Object> result = new ArrayList<Object>();
		addParameters(result, (Wrapper)getModel());
		result.addAll(super.getModelChildren());
		return result;
	}
	
	private void addParameters(List<Object> list, Wrapper wrapper) {
		List<Element> parameters = wrapper.getChildren(QueryTask.PARAMETERS);
		if (parameters != null && !parameters.isEmpty()) {
			list.add(new ParameterListTreeEditPart(parameters));
		}
	}
		
}
