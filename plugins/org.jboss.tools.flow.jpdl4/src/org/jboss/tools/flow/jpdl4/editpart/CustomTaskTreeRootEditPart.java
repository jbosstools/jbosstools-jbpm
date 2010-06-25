package org.jboss.tools.flow.jpdl4.editpart;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.Field;

public class CustomTaskTreeRootEditPart extends TaskTreeRootEditPart {
	public CustomTaskTreeRootEditPart(Wrapper wrapper) {
		super(wrapper);
	}

	protected List<Object> getModelChildren() {
		List<Object> result = new ArrayList<Object>();
		addFields(result, (Wrapper)getModel());
		result.addAll(super.getModelChildren());
		return result;
	}
	
	private void addFields(List<Object> list, Wrapper wrapper) {
		List<Element> fields = wrapper.getChildren(Field.FIELDS);
		if (fields != null && !fields.isEmpty()) {
			list.add(new FieldListTreeEditPart(fields));
		}
	}
}
