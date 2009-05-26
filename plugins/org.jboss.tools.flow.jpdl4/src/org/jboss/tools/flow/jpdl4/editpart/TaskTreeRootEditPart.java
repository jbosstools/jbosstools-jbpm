package org.jboss.tools.flow.jpdl4.editpart;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.ModelEvent;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class TaskTreeRootEditPart extends JpdlTreeEditPart {
	
	public TaskTreeRootEditPart(Wrapper wrapper) {
		super(wrapper);
	}
	
	protected List<Object> getModelChildren() {
		List<Object> result = new ArrayList<Object>();
		Wrapper wrapper = (Wrapper)getModel();
		if (wrapper == null) return null;
		addEventListeners(result, wrapper);
		return result;
	}
	
	private void addEventListeners(List<Object> list, Wrapper wrapper) {
		List<Element> eventListeners = wrapper.getChildren("eventListener");
		if (eventListeners == null) return;
		for (Element element : eventListeners) {
			if (element instanceof Wrapper) {
				list.add((Wrapper)element);
			}
		}
	}
	
    public void modelChanged(ModelEvent event) {
    	if (event.getChangeType() == Wrapper.ADD_ELEMENT) {
    		refreshChildren();
    		Object object = event.getNewValue();
    		EditPart editPart = (EditPart)getViewer().getEditPartRegistry().get(object);
    		if (editPart != null) {
    			getViewer().select(editPart);
    		}
    	} else if (event.getChangeType() == Wrapper.REMOVE_ELEMENT) {
    		refreshChildren();
    	}
    }
    
    public void activate() {
        super.activate();
        ((Wrapper)getModel()).addListener(this);
    }

    public void deactivate() {
    	((Wrapper)getModel()).removeListener(this);
        super.deactivate();
    }

}
